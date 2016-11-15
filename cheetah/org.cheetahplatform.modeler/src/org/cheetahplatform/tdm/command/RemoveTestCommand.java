package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.explorer.TDMProjectExplorerView;
import org.eclipse.ui.PlatformUI;

public class RemoveTestCommand extends TDMCommand {

	private final long testId;
	private TDMTest removedTest;

	public RemoveTestCommand(long testId) {
		this.testId = testId;
	}

	@Override
	public void execute() {
		TDMProjectExplorerView view = (TDMProjectExplorerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(TDMProjectExplorerView.ID);
		removedTest = view.removeTest(testId);
	}

	@Override
	public void undo() {
		TDMProjectExplorerView view = (TDMProjectExplorerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(TDMProjectExplorerView.ID);
		view.addTest(removedTest);
	}

}
