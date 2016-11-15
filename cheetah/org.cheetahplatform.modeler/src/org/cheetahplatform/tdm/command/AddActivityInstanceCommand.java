package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.daily.policy.ActivityChangeListener;

public class AddActivityInstanceCommand extends TDMCommand {

	private final long testId;
	private final TimeSlot slot;
	private final long id;
	private final long activityId;

	private Activity addedActivity;
	private ActivityChangeListener listener;

	public AddActivityInstanceCommand(long testId, long activityId, TimeSlot slot, long id) {
		this.testId = testId;
		this.activityId = activityId;
		this.slot = slot;
		this.id = id;
	}

	@Override
	public void execute() {
		DeclarativeActivity activity = getActivity(activityId);
		Workspace workspace = getWorkspace(testId);
		DeclarativeActivityInstance instance = activity.instantiate(workspace.getProcessInstance());
		instance.setTimeSlot(slot);

		addedActivity = workspace.addActivityInstance(instance);
		addedActivity.setCheetahId(id);
		listener = new ActivityChangeListener(addedActivity);
		Services.getModificationService().addListener(listener, activity);
	}

	@Override
	public void undo() {
		getWorkspace(testId).removeActivity(addedActivity);
		DeclarativeActivity activity = getActivity(activityId);
		Services.getModificationService().removeListener(listener, activity);
	}

}
