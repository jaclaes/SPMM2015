package org.cheetahplatform.tdm.command;

import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditor;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ShowPartCommand extends TDMCommand {

	private final String partId;
	private IWorkbenchPart oldActivePart;
	private final IPromLogger logger;
	private final long testId;

	public ShowPartCommand(String partId, long testId, IPromLogger logger) {
		this.partId = partId;
		this.testId = testId;
		this.logger = logger;
	}

	@Override
	public void execute() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		oldActivePart = page.getActivePart();

		try {
			if (partId.equals(TDMTestEditor.ID)) {
				TDMTest test = TDMCommand.getCurrentProcess().getTest(testId);
				TDMTestEditorInput input = new TDMTestEditorInput(test, logger);
				IEditorPart testEditor = page.findEditor(input);
				if (testEditor == null) {
					page.openEditor(input, TDMTestEditor.ID);
				} else {
					page.activate(testEditor);
				}
			} else {
				page.showView(partId);
			}
		} catch (PartInitException e) {
			Activator.logError("Could not open a part.", e);
		}
	}

	@Override
	public void undo() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.activate(oldActivePart);
	}

}
