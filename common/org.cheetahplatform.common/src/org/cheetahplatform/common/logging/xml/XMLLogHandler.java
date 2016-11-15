package org.cheetahplatform.common.logging.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.Messages;
import org.cheetahplatform.common.logging.Process;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class XMLLogHandler {
	private class UploadRunnable implements IRunnableWithProgress {

		private boolean success;

		public boolean isSuccess() {
			return success;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			success = uploadData(monitor);
		}

	}

	public static String getFilenameBase() {
		return filenameBase;
	}

	public static XMLLogHandler getInstance() {
		return INSTANCE;
	}

	public static List<Process> readLogFrom(int logId, InputStream input) throws Exception {
		List<Process> instances = new ArrayList<Process>();

		// ZipInputStream in = new ZipInputStream(input);
		// ZipEntry entry = in.getNextEntry();
		byte[] buffer = new byte[1024];

		// while (entry != null) {
		// int readCount = in.read(buffer, 0, buffer.length);
		int readCount = input.read(buffer, 0, buffer.length);

		MemoryByteStorage storage = new MemoryByteStorage();
		while (readCount != -1) {
			// do not write the entire buffer if it could not be filled entirely
			if (readCount != buffer.length) {
				byte[] temp = new byte[readCount];
				System.arraycopy(buffer, 0, temp, 0, readCount);
				storage.write(temp);
			} else {
				storage.write(buffer);
			}

			// readCount = in.read(buffer, 0, buffer.length);
			readCount = input.read(buffer, 0, buffer.length);
		}

		if (storage.getPointer() != 0) {
			XMLPromReader reader = new XMLPromReader(new ByteArrayInputStream(storage.getStorage()));
			Process process = reader.getProcess();
			instances.add(process);
		} else {
			throw new Exception(
					"Found an empty logfile. This may have two reasons: 1) A new logfile has been requested, but not used, e.g., by calling new PromLogger() and not using the logger afterwards\n2)The experimental workflow or one of its step might not have been completed successufully. Log Id: "
							+ logId);
		}

		// entry = in.getNextEntry();
		// }

		// in.close();
		return instances;
	}

	private static void save() {
		try {
			((IPersistentPreferenceStore) Activator.getDefault().getPreferenceStore()).save();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not save the preferences.", e); //$NON-NLS-1$
			Activator.getDefault().getLog().log(status);
		}
	}

	public static void setFilenameBase(String s) {
		filenameBase = s;
	}

	private static final String XML_LOG_HANDLER_FILES_KEY = "xml_log_handler_files"; //$NON-NLS-1$

	private static final String SEPARATOR = "\n"; //$NON-NLS-1$
	private static XMLLogHandler INSTANCE = new XMLLogHandler();
	private String email;
	private String url;
	private String user;
	private String password;
	private boolean toZip;

	private List<File> logFiles;

	private List<File> oldLogFiles;

	static private String filenameBase = "log";

	private XMLLogHandler() {
		super();

		logFiles = new ArrayList<File>();
		oldLogFiles = new ArrayList<File>();

		toZip = false;

		readLogFilesFromPrefeferences();
	}

	public void clearOldLogFiles() {
		oldLogFiles.clear();
	}

	private List<File> getAllFiles() {
		List<File> allFiles = new ArrayList<File>();
		allFiles.addAll(logFiles);
		allFiles.addAll(oldLogFiles);

		return allFiles;
	}

	private String getLogFiles() throws IOException {
		String result = "";
		for (File logFile : getAllFiles()) {
			BufferedReader in = new BufferedReader(new FileReader(logFile));
			String line = "";
			while (line != null) {
				line = in.readLine();
				if (line == null)
					break;
				result += line + '\n';
			}
			in.close();
		}
		return result;
	}

	public String getUnusedFilename() {
		String guess = "";
		if ("win32".equals(SWT.getPlatform()))
			guess = /* System.getProperty("user.home") + "\\" */"H:\\" + filenameBase;//$NON-NLS-1$
		else
			guess = System.getProperty("user.home") + "/" + filenameBase;//$NON-NLS-1$

		while (new File(guess + ".zip").exists()) { //$NON-NLS-1$
			guess += Math.abs(new Random().nextInt(10));
		}

		return guess + ".zip"; //$NON-NLS-1$
	}

	public boolean isEnabled() {
		return toZip || url != null || email != null;
	}

	public Connection openConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	private void readLogFilesFromPrefeferences() {
		if (!Platform.isRunning()) {
			return; // no platform available
		}

		String storedFiles = Activator.getDefault().getPreferenceStore().getString(XML_LOG_HANDLER_FILES_KEY);
		String[] files = storedFiles.split(SEPARATOR);
		for (String path : files) {
			if (path.trim().isEmpty()) {
				continue;
			}

			oldLogFiles.add(new File(path));
		}
	}

	public void register(File logfile) {
		logFiles.add(logfile);
		saveFilesToPreferences();
	}

	private void saveFilesToPreferences() {
		StringBuilder filesAsString = new StringBuilder();
		boolean first = true;
		for (File file : getAllFiles()) {
			if (!first) {
				filesAsString.append(SEPARATOR);
			}

			first = false;
			filesAsString.append(file.getAbsolutePath());
		}

		Activator.getDefault().getPreferenceStore().putValue(XML_LOG_HANDLER_FILES_KEY, filesAsString.toString());
		save();
	}

	public void sendDataManually() {
		FileOutputStream out = null;
		File target = new File(getUnusedFilename());

		try {
			out = new FileOutputStream(target);
			out.write(zipLogFiles());
			out.close();
		} catch (Exception e) {
			Activator.logError("Could not store the log.", e); //$NON-NLS-1$

			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					// ignore
				}
			}
		}

		String rawMessage = Messages.XMLLogHandler_8;
		String message = MessageFormat.format(rawMessage, target.getAbsolutePath()) + Messages.XMLLogHandler_8b;
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages.XMLLogHandler_9, message);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setToZip(boolean b) {
		toZip = b;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean toZip() {
		return toZip;
	}

	public void uploadData() {
		Shell shell = Display.getDefault().getActiveShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		UploadRunnable runnable = new UploadRunnable();

		try {
			dialog.run(false, false, runnable);
		} catch (InvocationTargetException e) {
			Activator.logError("Could not upload data.", e); //$NON-NLS-1$
		} catch (InterruptedException e) {
			// ignore
		}

		if (runnable.isSuccess()) {
			logFiles.clear();
			oldLogFiles.clear();

			saveFilesToPreferences();
			return;
		}

		boolean retry = MessageDialog.openQuestion(shell, Messages.XMLLogHandler_11, Messages.XMLLogHandler_12);
		if (retry) {
			uploadData();
		} else {
			sendDataManually();
			logFiles.clear();
			oldLogFiles.clear();
			saveFilesToPreferences();
		}
	}

	private boolean uploadData(IProgressMonitor monitor) {
		Assert.isTrue(isEnabled(), "XML Log Handler is not enabled."); //$NON-NLS-1$
		int tries = 3;
		monitor.beginTask(Messages.XMLLogHandler_14, tries);

		for (int i = 0; i < tries; i++) {
			try {
				Connection connection = openConnection();
				PreparedStatement statement = connection.prepareStatement("insert into xml_log (log) values (?);"); //$NON-NLS-1$
				statement.setString(1, getLogFiles());
				statement.execute();

				connection.close();
				return true;
			} catch (Exception e) {
				// ignore and retry
				e.printStackTrace();
				Activator.logError("Could not upload data.", e);
			}

			monitor.worked(1);
		}

		return false;
	}

	private byte[] zipLogFiles() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(buffer);
		byte[] readBuffer = new byte[10240];

		for (File logFile : getAllFiles()) {
			out.putNextEntry(new ZipEntry(logFile.getName()));
			FileInputStream in = new FileInputStream(logFile);

			int read = in.read(readBuffer, 0, readBuffer.length);
			while (read != -1) {
				out.write(readBuffer, 0, read);
				read = in.read(readBuffer, 0, readBuffer.length);
			}

			in.close();
			out.closeEntry();
		}

		out.close();
		return buffer.toByteArray();
	}
}
