package org.cheetahplatform.client.ui.action;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.jms.TerminateDeclarativeProcessInstanceService;
import org.cheetahplatform.client.model.WorklistModel;
import org.cheetahplatform.common.ui.SelectionSensitiveAction;
import org.cheetahplatform.shared.DeclarativeProcessInstanceHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class TerminateDeclarativeProcessInstanceAction extends SelectionSensitiveAction<DeclarativeProcessInstanceHandle> {

	private final WorklistModel model;

	public TerminateDeclarativeProcessInstanceAction(StructuredViewer viewer, WorklistModel model) {
		super(viewer, DeclarativeProcessInstanceHandle.class);

		this.model = model;
		setText("Terminate Process Instance");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/terminate_process_instance_16x16.png"));
	}

	@Override
	public void run() {
		DeclarativeProcessInstanceHandle instance = getSelection();
		TerminateDeclarativeProcessInstanceService service = new TerminateDeclarativeProcessInstanceService(instance);
		try {
			IStatus status = service.synchronousRequest();
			if (status.getSeverity() != IStatus.OK) {
				throw new JMSException(status.getMessage());
			}

			model.queryActiveTasks();
		} catch (JMSException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not terminate a declarative process instance.", e);
			Activator.getDefault().getLog().log(status);

			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "Error", "Could not terminate the process instance.");
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		if (getSelection() != null) {
			DeclarativeProcessInstanceHandle selection = getSelection();
			setEnabled(selection.canTerminate());
		}
	}

}
