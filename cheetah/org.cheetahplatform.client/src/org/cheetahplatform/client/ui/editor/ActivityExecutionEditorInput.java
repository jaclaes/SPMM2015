package org.cheetahplatform.client.ui.editor;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.swtdesigner.ResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class ActivityExecutionEditorInput implements IEditorInput {

	private final ActivityInstanceHandle activity;

	private final ProcessInstanceHandle processInstance;

	public ActivityExecutionEditorInput(ProcessInstanceHandle processInstance, ActivityInstanceHandle activity) {
		Assert.isNotNull(processInstance);
		Assert.isNotNull(activity);
		this.processInstance = processInstance;
		this.activity = activity;
	}

	public boolean exists() {
		return true;
	}

	/**
	 * Returns the activity.
	 * 
	 * @return the activity
	 */
	public ActivityInstanceHandle getActivity() {
		return activity;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public ImageDescriptor getImageDescriptor() {
		return ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/activity-16x16.gif");
	}

	public String getName() {
		return activity.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * Returns the processInstance.
	 * 
	 * @return the processInstance
	 */
	public ProcessInstanceHandle getProcessInstance() {
		return processInstance;
	}

	public String getToolTipText() {
		return processInstance.getName() + ">" + activity.getName();
	}
}
