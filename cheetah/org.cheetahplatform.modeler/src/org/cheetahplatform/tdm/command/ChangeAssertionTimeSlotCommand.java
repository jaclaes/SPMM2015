package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.model.Assertion;

public class ChangeAssertionTimeSlotCommand extends TDMCommand {

	private final TimeSlot slot;
	private TimeSlot oldSlot;
	private final long assertionId;
	private final long testId;

	public ChangeAssertionTimeSlotCommand(long testId, long assertionId, TimeSlot slot) {
		this.testId = testId;
		this.assertionId = assertionId;
		this.slot = slot;
	}

	@Override
	public void execute() {
		Assertion assertion = getWorkspace(testId).getAssertion(assertionId);
		oldSlot = assertion.getTimeSlot();
		assertion.setTimeSlot(slot);
	}

	@Override
	public void undo() {
		Assertion assertion = getWorkspace(testId).getAssertion(assertionId);
		assertion.setTimeSlot(oldSlot);
	}

}
