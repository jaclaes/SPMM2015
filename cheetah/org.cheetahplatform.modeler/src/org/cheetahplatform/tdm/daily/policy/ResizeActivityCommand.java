package org.cheetahplatform.tdm.daily.policy;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.eclipse.gef.commands.Command;

public class ResizeActivityCommand extends Command {

	private final Activity activity;
	private final TimeSlot newSlot;

	public ResizeActivityCommand(Activity activity, TimeSlot newSlot) {
		this.activity = activity;
		this.newSlot = newSlot;
	}

	@Override
	public void execute() {
		activity.setTimeSlot(newSlot);
	}

}
