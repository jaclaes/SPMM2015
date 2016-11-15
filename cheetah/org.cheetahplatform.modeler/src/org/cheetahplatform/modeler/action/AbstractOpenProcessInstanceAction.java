package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.common.ui.dialog.DefaultExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.dialog.SelectSingleProcessInstanceDialog;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractOpenProcessInstanceAction extends Action {

	private final IExtraColumnProvider provider;

	public AbstractOpenProcessInstanceAction() {
		this(new DefaultExtraColumnProvider());
	}

	public AbstractOpenProcessInstanceAction(IExtraColumnProvider provider) {
		this.provider = provider;
	}

	/**
	 * Open the selected handle.
	 * 
	 * @param handle
	 *            the handle
	 * @param experimentalWorkflowElementHandle
	 */
	protected abstract void doRun(ProcessInstanceDatabaseHandle handle,
			ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle);

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		SelectSingleProcessInstanceDialog dialog = new SelectSingleProcessInstanceDialog(shell, provider);

		for (Process process : ProcessRepository.getExperimentalProcesses()) {
			dialog.addIncludedProcess(process.getId());
		}

		int returnCode = dialog.open();
		if (returnCode != Window.OK || dialog.getSelection() == null) {
			return;
		}

		ProcessInstanceDatabaseHandle handle = dialog.getSelection();
		ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle = dialog.getExperimentalWorkflowElementHandle();

		try {
			ProcessInstance instance = DatabasePromReader.readProcessInstance(handle.getDatabaseId(), Activator.getDatabaseConnector()
					.getDatabaseConnection());
			handle.setInstance(instance);
		} catch (Exception e) {
			Activator.logError("Could not load a process instance.", e);
		}

		doRun(handle, experimentalWorkflowElementHandle);
	}

}
