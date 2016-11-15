package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.daily.model.Assertion;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;

public class RemoveExecutionAssertionCommand extends TDMCommand {

	private final long testId;
	private final long assertionId;

	public RemoveExecutionAssertionCommand(long testId, long assertionId) {
		this.testId = testId;
		this.assertionId = assertionId;
	}

	@Override
	public void execute() {
		Workspace workspace = getWorkspace(testId);
		Assertion assertion = workspace.getAssertion(assertionId);
		workspace.removeAssertion(assertion);
	}

	@Override
	public void undo() {
		Workspace workspace = getWorkspace(testId);
		Assertion assertion = workspace.getAssertion(assertionId);
		if (assertion instanceof TerminationAssertion) {
			workspace.addAssertion((TerminationAssertion) assertion);
		} else {
			workspace.addAssertion((ExecutionAssertion) assertion);
		}
	}

}
