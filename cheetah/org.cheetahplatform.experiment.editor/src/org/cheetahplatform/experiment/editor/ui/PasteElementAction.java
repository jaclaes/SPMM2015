package org.cheetahplatform.experiment.editor.ui;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class PasteElementAction extends SelectionAction {

	public PasteElementAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);// force calculateEnabled() to be called in every context
	}
	
	@Override
	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setText("Paste");
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}
	
	private Command createPasteCommand() {
		return new PasteElementCommand();
	}
	
	@Override
	protected boolean calculateEnabled() {
		Command command = createPasteCommand();
		 return command != null && command.canExecute();
	}
	
	@Override
	public void run() {
		Command command = createPasteCommand();
		if (command != null && command.canExecute())
			execute(command);
	}
	
}
