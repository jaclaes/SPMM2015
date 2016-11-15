package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.explorer.TDMProjectExplorerView;
import org.eclipse.ui.PlatformUI;

public class CreateTestCommand extends TDMCommand {

	private final String testName;
	private final long id;

	public CreateTestCommand(String testName, long id) {
		this.testName = testName;
		this.id = id;
	}

	@Override
	public void execute() {
		TDMProjectExplorerView view = (TDMProjectExplorerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(TDMProjectExplorerView.ID);
		view.addTest(testName, id);
	}

	@Override
	public void undo() {
		TDMProjectExplorerView view = (TDMProjectExplorerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(TDMProjectExplorerView.ID);
		view.removeTest(id);
	}
}
