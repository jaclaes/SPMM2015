package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.daily.model.Assertion;
import org.cheetahplatform.tdm.daily.model.Workspace;

public class SwitchAssertionExceptedStateCommand extends TDMCommand {

	private final boolean state;
	private final long testId;
	private final long assertionId;

	public SwitchAssertionExceptedStateCommand(long testId, long assertionId, boolean state) {
		this.testId = testId;
		this.assertionId = assertionId;
		this.state = state;
	}

	@Override
	public void execute() {
		Workspace workspace = getWorkspace(testId);
		Assertion assertion = workspace.getAssertion(assertionId);
		assertion.setPositive(state);
	}

	@Override
	public void undo() {
		Workspace workspace = getWorkspace(testId);
		Assertion assertion = workspace.getAssertion(assertionId);
		assertion.setPositive(!state);
	}
}
