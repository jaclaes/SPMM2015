package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.problemview.ProblemView;
import org.eclipse.ui.PlatformUI;

public class RevealProblemCommand extends TDMCommand {

	private final long testId;
	private final int problemIndex;

	public RevealProblemCommand(long testId, int problemIndex) {
		this.testId = testId;
		this.problemIndex = problemIndex;
	}

	@Override
	public void execute() {
		ProblemView view = (ProblemView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProblemView.ID);
		view.revealProblem(testId, problemIndex);
	}

}
