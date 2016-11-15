package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.daily.policy.ActivityChangeListener;

public class AddExecutionAssertionCommand extends TDMCommand {

	private final long testId;
	private final TimeSlot slot;
	private final long id;
	private ExecutionAssertion addedAssertion;
	private ActivityChangeListener listener;
	private final long activityId;

	public AddExecutionAssertionCommand(long testId, long activityId, TimeSlot slot, long id) {
		this.testId = testId;
		this.activityId = activityId;
		this.slot = slot;
		this.id = id;
	}

	@Override
	public void execute() {
		Workspace workspace = getWorkspace(testId);
		Day day = workspace.getDay(slot.getStart());
		DeclarativeActivity activity = getActivity(activityId);
		addedAssertion = new ExecutionAssertion(day, slot, activity, id);
		workspace.addAssertion(addedAssertion);

		listener = new ActivityChangeListener(addedAssertion);
		Services.getModificationService().addListener(listener, activity);
	}

	@Override
	public void undo() {
		Workspace workspace = getWorkspace(testId);
		workspace.removeAssertion(addedAssertion);
		DeclarativeActivity activity = getActivity(activityId);
		Services.getModificationService().removeListener(listener, activity);
	}

}
