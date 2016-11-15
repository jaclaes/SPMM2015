package org.cheetahplatform.common.logging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Delegating implementation of the PROM writer. Tries to find the most suitable writer and delegates the logging accordingly.
 * 
 * @author Stefan Zugal
 * 
 */
public class PromLogger extends AbstractPromLogger {
	public static final String GROUP_EVENT_START = "GROUP_EVENT_START";
	public static final String GROUP_EVENT_END = "GROUP_EVENT_END";

	public static void addHost(DataContainer container) {
		String host = getHost();
		container.setAttribute(CommonConstants.ATTRIBUTE_HOST, host);
	}

	/**
	 * Asks the user for a writable file. The user is asked until he specifies a valid, writable file or he dismisses the dialog.
	 * 
	 * @param originalFile
	 *            the original file
	 * @return a writable file, <code>null</code> if the user aborted
	 */
	private static File askForWritableFile(File originalFile) {
		File file = originalFile;
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		while (!file.canWrite()) {
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFileName(file.getName());
			dialog.setText("Specify log file");

			String result = dialog.open();
			if (result == null) {
				return null;
			}

			file = new File(result);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					continue;
				}
			} else {
				if (!MessageDialog.openQuestion(shell, "Overwrite File?",
						"The file you specified already exists and will be overwritten.\nContinue?")) {
					file = originalFile;
				}
			}
		}

		return file;
	}

	public static String getHost() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress() + " (" + addr.getHostName() + ")";
		} catch (UnknownHostException e1) {
			return "N/A";
		}
	}

	/**
	 * @return the default location where log files will be stored to.
	 */
	private static File getLogLocation() {
		return Platform.getLocation().toFile();
	}

	public static File getNewLogFile() {
		String extension = ".mxml";
		String fileName = System.currentTimeMillis() + extension;

		File newLogFile = null;
		try {
			if (Platform.isRunning()) {
				newLogFile = new File(getLogLocation(), fileName);
			} else {
				newLogFile = File.createTempFile(fileName, extension);
			}
			while (!newLogFile.canWrite()) {
				if (!isLegalFilename(fileName) || newLogFile.exists() || !newLogFile.createNewFile())
					newLogFile = askForWritableFile(newLogFile);
				if (newLogFile == null) // user abort
					return null;

			}
		} catch (IOException e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "Error", "Could not open the logfile.");
		}

		return newLogFile;
	}

	/**
	 * Checks if the passed name is a valid file name.
	 * 
	 * @param fileName
	 *            the name
	 * @return <code>true</code> if the name is valid, <code>false</code> otherwise
	 */
	private static boolean isLegalFilename(String fileName) {
		if (!PlatformUI.isWorkbenchRunning()) {
			// call comes from some testing environment
			return true;
		}

		IStatus status = ResourcesPlugin.getWorkspace().validateName(fileName, IResource.FILE);
		if (status.getSeverity() == IStatus.OK) {
			return true;
		}

		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Invalid Filename",
				MessageFormat.format("{0} is not a legal filename.", fileName));
		return false;
	}

	protected IPromWriter promWriter;
	private List<AuditTrailEntry> entries;
	protected Process process;
	private ProcessInstance processInstance;
	protected IPromWriterProvider provider;

	public PromLogger() {
		this(new DefaultPromWriterProvider());
	}

	public PromLogger(IPromWriterProvider provider) {
		this.provider = provider;
		this.entries = new ArrayList<AuditTrailEntry>();

		acquireWriter();
	}

	/**
	 * Tries to acquire the most suitable logger.
	 * 
	 * @param gameType
	 */
	protected void acquireWriter() {
		close();

		try {
			promWriter = provider.acquireWriter();
		} catch (Exception e) {
			Activator.logError("Could not acquire the file writer.", e);
		}
	}

	@Override
	public void close() {
		if (promWriter != null) {
			promWriter.close();
		}
	}

	@Override
	protected IStatus doAppend(AuditTrailEntry entry) {
		entries.add(entry);

		IStatus status = promWriter.append(entry);
		if (status.getSeverity() == IStatus.ERROR) {
			// could not log the entry --> try to acquire another logger and re-log the entries
			acquireWriter();
			promWriter.append(process, processInstance);
			for (AuditTrailEntry old : entries) {
				promWriter.append(old);
			}

			if (promWriter instanceof XMLPromWriter) {
				return new Status(IStatus.WARNING, Activator.PLUGIN_ID,
						"Please contact your supervisor as the modeler switched the logger to \"" + promWriter.getClass()
								+ "\". All upcoming events are logged to this logger.");
			}
		}

		return Status.OK_STATUS;
	}

	@Override
	protected IStatus doAppend(Process process, ProcessInstance instance) {
		this.process = process;
		this.processInstance = instance;

		if (!instance.isAttributeDefined(CommonConstants.ATTRIBUTE_TIMESTAMP)) {
			instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
		}

		IStatus status = promWriter.append(process, instance);
		if (status.getSeverity() == IStatus.ERROR) {
			// could not log the entry --> try to acquire another logger and re-log the entries
			acquireWriter();

			promWriter.append(process, instance);
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Switched the logger to \"" + promWriter.getClass()
					+ "\". All upcoming events are logged to this logger.");
		}

		return Status.OK_STATUS;
	}

	@Override
	public String getProcessInstanceId() {
		if (processInstance != null)
			return processInstance.getId();

		return null;
	}

	public void logError(IStatus status) {
		if (!Activator.getDatabaseConnector().checkConnection()) {
			return;
		}

		try {
			Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
			logError(status, connection);
		} catch (SQLException e) {
			// ignore
		}
	}

	/**
	 * Tries to log the given status to a database.
	 * 
	 * @param status
	 *            the status to be logged
	 */
	public void logError(IStatus status, Connection connection) {
		try {
			String message = "";
			if (status.getMessage() != null) {
				message = status.getMessage();
			}
			String trace = "";
			if (status.getException() != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(out);
				status.getException().printStackTrace(writer);
				writer.flush();
				trace = new String(out.toByteArray());
			}
			List<Attribute> attributes = new ArrayList<Attribute>();
			if (process != null) {
				attributes.add(new Attribute("process id", process.getId()));
			}
			if (processInstance != null) {
				attributes.add(new Attribute("instance id", processInstance.getId()));
			}
			String attributesString = DatabaseUtil.toDatabaseRepresentation(attributes);

			PreparedStatement statement = connection
					.prepareStatement("insert into log (message, trace, attributes, host) values (?, ?, ?, ?)");
			statement.setString(1, message);
			statement.setString(2, trace);
			statement.setString(3, attributesString);
			statement.setString(4, getHost());
			// statement.setTimestamp(5, new Timestamp(new Date().getTime()));

			statement.execute();
			statement.close();
		} catch (Exception e) {
			// can't use the eclipse logger as this would invoke this logger again
			System.err.println(status.getMessage());
			if (status.getException() != null) {
				status.getException().printStackTrace();
			}

			e.printStackTrace();
		}
	}

	public void setProvider(IPromWriterProvider provider) {
		this.provider = provider;
	}

}
