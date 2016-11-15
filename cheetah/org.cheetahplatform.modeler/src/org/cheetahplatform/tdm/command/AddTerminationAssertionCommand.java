package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;

public class AddTerminationAssertionCommand extends TDMCommand {

	private final long testId;
	private final TimeSlot slot;
	private final long id;
	private TerminationAssertion addedAssertion;

	public AddTerminationAssertionCommand(long testId, TimeSlot slot, long id) {
		this.testId = testId;
		this.slot = slot;
		this.id = id;
	}

	@Override
	public void execute() {
		Workspace workspace = getWorkspace(testId);
		Day day = workspace.getDay(slot.getStart());
		addedAssertion = new TerminationAssertion(day, slot, id);
		workspace.addAssertion(addedAssertion);
	}

	@Override
	public void undo() {
		Workspace workspace = getWorkspace(testId);
		workspace.removeAssertion(addedAssertion);
	}

}
