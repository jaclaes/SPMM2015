package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;

public class RemoveTerminationAssertionCommand extends TDMCommand {

	private final long testId;
	private final long assertionId;

	public RemoveTerminationAssertionCommand(long testId, long assertionId) {
		this.testId = testId;
		this.assertionId = assertionId;
	}

	@Override
	public void execute() {
		Workspace workspace = getWorkspace(testId);
		TerminationAssertion assertion = workspace.getTerminationAssertion(assertionId);
		workspace.removeAssertion(assertion);
	}

	@Override
	public void undo() {
		Workspace workspace = getWorkspace(testId);
		TerminationAssertion assertion = workspace.getTerminationAssertion(assertionId);
		workspace.addAssertion(assertion);
	}

}
