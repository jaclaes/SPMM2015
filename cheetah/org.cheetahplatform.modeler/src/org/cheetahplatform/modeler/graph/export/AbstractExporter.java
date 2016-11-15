package org.cheetahplatform.modeler.graph.export;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.action.AbstractReplayAction;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractExporter {
	private PreparedStatement getDatabaseIdQuery;

	/**
	 * Export the given audit trail entry of an *experimental* process instance.
	 * 
	 * @param entry
	 *            the entry to be exported
	 */
	@SuppressWarnings("unused")
	protected void doExportAuditTrailEntry(AuditTrailEntry entry) {
		// nothing to export
	}

	/**
	 * Export the given *experimental* process instance, i.e., the process instance describing the experiment. Please note that the audit
	 * trail entries do not need to be processed as {@link #doExportAuditTrailEntry(AuditTrailEntry)} is called for that purpose.
	 * 
	 * @param current
	 *            the instance to be exported
	 */
	@SuppressWarnings("unused")
	protected void doExportExperimentalProcessInstance(ProcessInstanceDatabaseHandle current) {
		// ignore
	}

	/**
	 * Export the given process instance.
	 * 
	 * @param modelingInstance
	 *            the instance to be exported
	 * @param entry
	 *            the entry created for the modeling process
	 */
	protected abstract void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry);

	public void export(ProcessInstanceDatabaseHandle toExport, IProgressMonitor monitor) throws Exception {
		AbstractReplayAction.REPLAY_ACTIVE = true;

		ProcessInstance instance = DatabasePromReader.readProcessInstance(toExport.getDatabaseId(), Activator.getDatabaseConnector()
				.getDatabaseConnection());
		toExport.setInstance(instance);
		monitor.setTaskName("Exporting instance " + instance.getId());

		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.addAll(toExport.getAttributes());
		for (AuditTrailEntry entry : instance.getEntries()) {
			attributes.addAll(entry.getAttributes());
		}

		instance.addAttributesNonOverwrite(attributes);
		doExportExperimentalProcessInstance(toExport);

		for (AuditTrailEntry entry : instance.getEntries()) {
			if (EditorRegistry.isModelingActivityId(entry.getEventType())) {
				PreparedStatement query = getDatabaseIdQuery();
				query.setString(1, entry.getAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE));
				ResultSet resultSet = query.executeQuery();

				// it might be the case that the instance has been logged via XML --> there is no corresponding database entry
				if (resultSet.next()) {
					long databaseId = resultSet.getLong(1);

					ProcessInstance modelingInstance = DatabasePromReader.readProcessInstance(databaseId, Activator.getDatabaseConnector()
							.getDatabaseConnection());
					modelingInstance.addAttributesNonOverwrite(attributes);
					ProcessInstanceDatabaseHandle handle = new ProcessInstanceDatabaseHandle(databaseId, modelingInstance.getId(),
							(String) null, null);
					handle.setInstance(modelingInstance);

					doExportModelingProcessInstance(handle, entry);
				}

				resultSet.close();
			} else {
				doExportAuditTrailEntry(entry);
			}
		}

		AbstractReplayAction.REPLAY_ACTIVE = false;
	}

	/**
	 * Allows subclasses to perform any clean-up work after the export is done. The default implementation does nothing.
	 */
	public void exportFinished() {
		try {
			if (getDatabaseIdQuery != null) {
				getDatabaseIdQuery.close();
			}
		} catch (SQLException e) {
			Activator.logError("Could not close a statement.", e);
		}
	}

	private PreparedStatement getDatabaseIdQuery() {
		if (getDatabaseIdQuery != null) {
			return getDatabaseIdQuery;
		}

		try {
			getDatabaseIdQuery = Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("select database_id from process_instance where id = ? order by database_id desc");
		} catch (SQLException e) {
			Activator.logError("Could not initialize the exporter.", e);
		}

		return getDatabaseIdQuery;
	}

	protected String getPathToFile(File target, ProcessInstance modelingInstance, String fileExtension) {
		String process = modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		File file = new File(target.getAbsolutePath() + "/" + process);
		if (!file.exists()) {
			file.mkdir();
		}

		return file.getAbsolutePath() + "/" + modelingInstance.getId() + "." + fileExtension;
	}

	protected String getTimeRelativeToStartTime(Date date, ProcessInstance instance) {
		long instanceStartTime = instance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP);
		long time = date.getTime();
		long relativeTime = (time - instanceStartTime) / 1000;

		long minutes = relativeTime / 60;
		long seconds = relativeTime % 60;
		return "" + minutes + ":" + seconds;

	}

	/**
	 * Initialize the export, the default implementation does nothing.
	 * 
	 * @param target
	 *            the file to be exported to
	 */
	@SuppressWarnings("unused")
	public void initializeExport(File target) {
		// do nothing
	}

	protected void showErrorMessage(final String error) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", error);
			}
		});
	}
}
