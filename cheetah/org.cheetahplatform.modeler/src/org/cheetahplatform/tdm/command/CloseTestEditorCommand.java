package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditor;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class CloseTestEditorCommand extends TDMCommand {

	private final IPromLogger logger;
	private final long testId;

	public CloseTestEditorCommand(long testId, IPromLogger logger) {
		this.testId = testId;
		this.logger = logger;
	}

	@Override
	public void execute() {
		TDMTest test = TDMCommand.getCurrentProcess().getTest(testId);
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart toClose = activePage.findEditor(new TDMTestEditorInput(test, logger));
		activePage.closeEditor(toClose, false);
	}

	@Override
	public void undo() {
		TDMTest test = TDMCommand.getCurrentProcess().getTest(testId);
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		TDMTestEditorInput input = new TDMTestEditorInput(test, logger);

		try {
			page.openEditor(input, TDMTestEditor.ID);
		} catch (PartInitException e) {
			Activator.logError("Could not open the test editor.", e);
		}
	}
}
