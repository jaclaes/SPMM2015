package org.cheetahplatform.modeler.action;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.DatabaseIdGenerator;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.dialog.SelectMultipleProcessInstancesDialog;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class DuplicateProcessAction extends Action {
	private static class DuplicateExporter extends AbstractExporter {

		private PromLogger modelingProcessLogger;
		private PromLogger experimentalProcessLogger;
		private PreparedStatement getProcessIdStatement;

		public DuplicateExporter() throws SQLException {
			getProcessIdStatement = org.cheetahplatform.common.Activator
					.getDatabaseConnector()
					.getDatabaseConnection()
					.prepareStatement(
							"select process.id from process, process_instance where process_instance.process = process.database_id and process_instance.database_id = ?");
		}

		@Override
		protected void doExportAuditTrailEntry(AuditTrailEntry entry) {
			experimentalProcessLogger.append(entry);
		}

		@Override
		protected void doExportExperimentalProcessInstance(ProcessInstanceDatabaseHandle instance) {
			try {
				experimentalProcessLogger = new PromLogger();
				getProcessIdStatement.setLong(1, instance.getDatabaseId());
				ResultSet resultSet = getProcessIdStatement.executeQuery();
				resultSet.next();
				String processId = resultSet.getString(1);
				Process process = ProcessRepository.getProcess(processId);

				String newProcessInstanceId = new DatabaseIdGenerator().generateId();
				instance.getInstance().setId(newProcessInstanceId);
				experimentalProcessLogger.append(process, instance.getInstance());
				resultSet.close();
			} catch (SQLException e) {
				Activator.logError("Could not retrieve an id.", e);
			}
		}

		@Override
		protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstanceHandle, AuditTrailEntry entry) {
			String newProcessInstanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();

			modelingProcessLogger = new PromLogger();
			ProcessInstance modelingInstance = modelingInstanceHandle.getInstance();
			String processId = modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
			Process process = ProcessRepository.getProcess(processId);
			modelingInstance.setId(newProcessInstanceId);
			modelingProcessLogger.append(process, modelingInstance);
			entry.setAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, newProcessInstanceId);
			experimentalProcessLogger.append(entry);

			for (AuditTrailEntry toAppend : modelingInstance.getEntries()) {
				modelingProcessLogger.append(toAppend);
			}
		}

		@Override
		public void exportFinished() {
			super.exportFinished();

			try {
				getProcessIdStatement.close();
			} catch (SQLException e) {
				org.cheetahplatform.common.Activator.logError("Could not close the statement.", e);
			}
		}

	}

	public static final String ID = "org.cheetahplatform.modeler.action.DuplicateProcessAction";

	public DuplicateProcessAction() {
		setId(ID);
		setText("Duplicate Process");
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		SelectMultipleProcessInstancesDialog dialog = new SelectMultipleProcessInstancesDialog(shell);
		for (Process process : ProcessRepository.getExperimentalProcesses()) {
			dialog.addIncludedProcess(process.getId());
		}

		if (dialog.open() != Window.OK) {
			return;
		}

		try {
			List<ProcessInstanceDatabaseHandle> selection = dialog.getSelection();
			DuplicateExporter exporter = new DuplicateExporter();
			for (ProcessInstanceDatabaseHandle handle : selection) {
				exporter.export(handle, new NullProgressMonitor());
			}
		} catch (Exception e) {
			Activator.logError("Could not duplicate a process.", e);
		}
	}
}
