package org.cheetahplatform.client.ui.editor;

import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;

public class ActivityEditorModel {
	private ProcessInstanceHandle processInstance;

	private ActivityInstanceHandle activityInstance;

	/**
	 * @return the activityInstance
	 */
	public ActivityInstanceHandle getActivityInstance() {
		return activityInstance;
	}

	/**
	 * @return the processInstance
	 */
	public ProcessInstanceHandle getProcessInstance() {
		return processInstance;
	}

	public void setActivityInstance(ActivityInstanceHandle activity) {
		activityInstance = activity;
	}

	public void setProcessInstance(ProcessInstanceHandle processInstance) {
		this.processInstance = processInstance;
	}
}
