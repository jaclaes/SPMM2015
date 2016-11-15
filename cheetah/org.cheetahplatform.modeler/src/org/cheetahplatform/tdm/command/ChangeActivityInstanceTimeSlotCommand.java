package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.eclipse.core.runtime.Assert;

public class ChangeActivityInstanceTimeSlotCommand extends TDMCommand {

	private final TimeSlot slot;
	private TimeSlot oldSlot;
	private final long activityId;
	private final long testId;

	public ChangeActivityInstanceTimeSlotCommand(long testId, long activityId, TimeSlot slot) {
		this.testId = testId;
		Assert.isNotNull(slot);

		this.activityId = activityId;
		this.slot = slot;
	}

	@Override
	public void execute() {
		Activity activity = getWorkspace(testId).getActivity(activityId);
		oldSlot = activity.getTimeSlot();
		activity.setTimeSlot(slot);
	}

	@Override
	public void undo() {
		Activity activity = getWorkspace(testId).getActivity(activityId);
		activity.setTimeSlot(oldSlot);
	}

}
