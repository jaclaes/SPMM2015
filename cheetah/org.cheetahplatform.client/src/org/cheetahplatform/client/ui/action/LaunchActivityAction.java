package org.cheetahplatform.client.ui.action;

import static org.cheetahplatform.core.common.modeling.INode.TYPE_ACTIVITY;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_LATE_BINDING_NODE;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_LATE_MODELING_NODE;

import java.util.List;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.ModificationService;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.jms.LaunchActivityService;
import org.cheetahplatform.client.jms.RetrieveLateBindingBoxSubProcessesService;
import org.cheetahplatform.client.jms.RetrieveLateModelingBoxService;
import org.cheetahplatform.client.jms.SelectLateBindingSequenceService;
import org.cheetahplatform.client.jms.SelectLateModelingBoxContentService;
import org.cheetahplatform.client.model.WorklistModel;
import org.cheetahplatform.client.ui.LateModelingDialog;
import org.cheetahplatform.client.ui.SelectSubProcessDialog;
import org.cheetahplatform.client.ui.editor.ActivityExecutionEditor;
import org.cheetahplatform.client.ui.editor.ActivityExecutionEditorInput;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.ui.SelectionSensitiveAction;
import org.cheetahplatform.shared.ActivityHandle;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

/**
 * An action responsible for executing activities.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class LaunchActivityAction extends SelectionSensitiveAction<ActivityInstanceHandle> {

	private final WorklistModel model;

	/**
	 * Creates a new action.
	 * 
	 * @param worklistViewer
	 *            the corresponding viewer
	 * @param model
	 *            the {@link WorklistModel}
	 */
	public LaunchActivityAction(TreeViewer worklistViewer, WorklistModel model) {
		super(worklistViewer, ActivityInstanceHandle.class);
		Assert.isNotNull(worklistViewer);
		Assert.isNotNull(model);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/launch-16x16.gif"));
		setText("Launch Activity");
		this.model = model;
		setToolTipText("Launches the Activity");
		setEnabled(false);
	}

	private void handleActivity(ActivityInstanceHandle activity, ProcessInstanceHandle processInstance) throws JMSException,
			PartInitException {
		LaunchActivityService service = new LaunchActivityService(processInstance, activity);
		IStatus status = service.synchronousRequest();
		if (status.equals(Status.OK_STATUS)) {
			ModificationService.broadcastChanges(activity, ModificationService.ACTIVITY_EXECUTED_PROPERTY);

			activity.setId(service.getNewActivityInstanceId());
			ActivityExecutionEditorInput input = new ActivityExecutionEditorInput(processInstance, activity);
			Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, ActivityExecutionEditor.id);
		}
	}

	private void handleLateBinding(ActivityInstanceHandle activity, ProcessInstanceHandle processInstance) throws JMSException {
		RetrieveLateBindingBoxSubProcessesService service = new RetrieveLateBindingBoxSubProcessesService(activity, processInstance);
		IStatus result = service.synchronousRequest();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		if (result.getSeverity() == IStatus.OK) {
			List<ProcessSchemaHandle> subProcesses = service.getSubProcesses();
			SelectSubProcessDialog dialog = new SelectSubProcessDialog(shell, subProcesses);
			if (dialog.open() != Window.OK) {
				return;
			}

			ProcessSchemaHandle selection = dialog.getSelection();
			SelectLateBindingSequenceService selectLateBindingSequenceService = new SelectLateBindingSequenceService(processInstance,
					activity, selection);
			selectLateBindingSequenceService.synchronousRequest();
			model.queryActiveTasks();
		} else {
			MessageDialog.openError(shell, "Error", "An error occurred while retrieving the subprocesses:\n" + result.getMessage());
		}
	}

	private void handleLateModeling(ActivityInstanceHandle activity, ProcessInstanceHandle processInstance) throws JMSException {
		RetrieveLateModelingBoxService service = new RetrieveLateModelingBoxService(processInstance, activity);
		IStatus status = service.synchronousRequest();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		if (status.getSeverity() != IStatus.OK) {
			MessageDialog.openError(shell, "Error", "Error while retrieving late modeling activities.");
			return;
		}

		List<ActivityHandle> activities = service.getActivities();
		LateModelingDialog dialog = new LateModelingDialog(shell, activities);
		if (dialog.open() != Window.OK) {
			return;
		}

		GraphHandle modeledSchema = dialog.getModeledProcessSchema();
		SelectLateModelingBoxContentService selectLateModelingBoxContentService = new SelectLateModelingBoxContentService(processInstance,
				activity, modeledSchema);
		selectLateModelingBoxContentService.synchronousRequest();

		model.queryActiveTasks();
	}

	@Override
	public void run() {
		ActivityInstanceHandle activity = getSelection();

		try {
			ProcessInstanceHandle processInstance = activity.getParent();
			if (activity.getType().equals(TYPE_ACTIVITY)) {
				handleActivity(activity, processInstance);
			} else if (activity.getType().equals(TYPE_LATE_BINDING_NODE)) {
				handleLateBinding(activity, processInstance);
			} else if (activity.getType().equals(TYPE_LATE_MODELING_NODE)) {
				handleLateModeling(activity, processInstance);
			}
		} catch (JMSException e) {
			UiUtils.showAndLogError("Failed to execute activity " + activity.getName(), e);
		} catch (PartInitException e) {
			UiUtils.showAndLogError("Failed to open editor for activity " + activity.getName(), e);
		}
	}
}
