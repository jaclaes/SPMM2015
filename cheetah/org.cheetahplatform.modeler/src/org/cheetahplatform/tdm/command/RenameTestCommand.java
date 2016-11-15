package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.engine.TDMTest;

public class RenameTestCommand extends TDMCommand {

	private final String newTestName;
	private String oldName;
	private final long testId;

	public RenameTestCommand(long testId, String newTestName) {
		this.testId = testId;
		this.newTestName = newTestName;
	}

	@Override
	public void execute() {
		TDMTest test = getTest(testId);
		oldName = test.getName();
		test.setName(newTestName);
	}

	@Override
	public void undo() {
		TDMTest test = getTest(testId);
		test.setName(oldName);
	}
}
