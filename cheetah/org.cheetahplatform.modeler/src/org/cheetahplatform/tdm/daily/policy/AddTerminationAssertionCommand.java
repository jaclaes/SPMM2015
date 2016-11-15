package org.cheetahplatform.tdm.daily.policy;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.gef.commands.Command;

public class AddTerminationAssertionCommand extends Command {

	private final Workspace workspace;
	private final TimeSlot slot;

	public AddTerminationAssertionCommand(Workspace workspace, TimeSlot slot) {
		this.workspace = workspace;
		this.slot = slot;
	}

	@Override
	public void execute() {
		workspace.addAssertion(new TerminationAssertion(workspace.getDay(slot.getStart()), slot));
	}

}
