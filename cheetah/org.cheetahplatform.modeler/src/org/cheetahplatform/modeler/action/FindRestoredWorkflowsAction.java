package org.cheetahplatform.modeler.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.ui.dialog.DefaultExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.dialog.SelectProcessInstanceModel;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.engine.configurations.IExperimentConfiguration;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.cheetahplatform.tdm.dialog.EditMessageDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class FindRestoredWorkflowsAction extends Action {

	private static class FindRestoredWorkflowsExporter extends AbstractExporter {

		private List<ProcessInstanceDatabaseHandle> abortedProcesses;
		private List<ProcessInstanceDatabaseHandle> continuedProcesses;

		private FindRestoredWorkflowsExporter() {
			this.abortedProcesses = new ArrayList<ProcessInstanceDatabaseHandle>();
			this.continuedProcesses = new ArrayList<ProcessInstanceDatabaseHandle>();
		}

		@Override
		protected void doExportExperimentalProcessInstance(ProcessInstanceDatabaseHandle current) {
			String processId = current.getProcessId();
			IExperimentConfiguration experiment = ProcessRepository.getExperiment(processId);
			Assert.isNotNull(experiment, "No experiment for id: " + processId); // should not happen, we are processing experimental
																				// workflows only
			String configurationAsString = current.getAttributeSafely(ExperimentalWorkflowEngine.WORKFLOW_CONFIGURATION_ID);
			if (configurationAsString.isEmpty()) {
				continuedProcesses.add(current);
				return; // a restored process
			}

			int configurationId = Integer.parseInt(configurationAsString);
			List<WorkflowConfiguration> configurations = experiment.createConfigurations();
			for (WorkflowConfiguration configuration : configurations) {
				if (configuration.getId() == configurationId) {
					if (configuration.getActivites().size() != current.getInstance().getEntries().size()) {
						abortedProcesses.add(current);
					}

					break;
				}
			}
		}

		@Override
		protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
			// ignore
		}

		public List<ProcessInstanceDatabaseHandle> getAbortedProcesses() {
			return abortedProcesses;
		}

		public List<ProcessInstanceDatabaseHandle> getContinuedProcesses() {
			return continuedProcesses;
		}

	}

	private static class FindRestoredWorkflowsRunnable implements IRunnableWithProgress {

		private FindRestoredWorkflowsExporter exporter;

		public FindRestoredWorkflowsExporter getExporter() {
			return exporter;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			SelectProcessInstanceModel model = new SelectProcessInstanceModel(new DefaultExtraColumnProvider());
			for (Process process : ProcessRepository.getExperimentalProcesses()) {
				model.addIncludedProcess(process.getId());
			}

			List<ProcessInstanceDatabaseHandle> allProcessInstances = model.loadAllProcessInstances();

			try {
				exporter = new FindRestoredWorkflowsExporter();
				monitor.beginTask("Finding restored workflows", allProcessInstances.size());

				for (ProcessInstanceDatabaseHandle handle : allProcessInstances) {
					exporter.export(handle, monitor);
					monitor.worked(1);
				}
			} catch (Exception e) {
				throw new InvocationTargetException(e);
			}

			model.dispose();
		}
	}

	public static final String ID = " org.cheetahplatform.modeler.action.FindRestoredWorkflowsAction";

	public FindRestoredWorkflowsAction() {
		setId(ID);
		setText("Find Restored Experimental Workflows");
	}

	private String assembleMessage(FindRestoredWorkflowsRunnable runnable) {
		StringBuilder message = new StringBuilder();
		message.append("The following workflows have been aborted:\n");
		for (ProcessInstanceDatabaseHandle aborted : runnable.getExporter().getAbortedProcesses()) {
			message.append(aborted.getId() + "\t" + aborted.getHost() + "\n");
		}

		message.append("\n\n");
		message.append("The following workflows result from restoring:\n");
		for (ProcessInstanceDatabaseHandle restored : runnable.getExporter().getContinuedProcesses()) {
			message.append(restored.getId() + "\t" + restored.getHost() + "\n");
		}

		return message.toString();
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		FindRestoredWorkflowsRunnable runnable = new FindRestoredWorkflowsRunnable();

		try {
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
			progressDialog.run(true, false, runnable);
		} catch (Exception e) {
			Activator.logError("Could not find the restored process instancess.", e);
			return;
		}

		String message = assembleMessage(runnable);
		EditMessageDialog dialog = new EditMessageDialog(shell, message, true, "Restored Experimental Workflows",
				"Please find the restored experimental workflows in the texterea below.");
		dialog.open();
	}
}
