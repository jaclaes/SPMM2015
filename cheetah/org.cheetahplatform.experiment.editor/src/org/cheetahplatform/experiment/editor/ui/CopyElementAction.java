package org.cheetahplatform.experiment.editor.ui;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class CopyElementAction extends SelectionAction {

	public CopyElementAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);// force calculateEnabled() to be called in every context
	}

	@Override
	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("Copy");
		setId(ActionFactory.COPY.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}
	
	private Command createCopyCommand(List<EditPart> selectedObjects) {
		if (selectedObjects == null || selectedObjects.isEmpty()) {
			return null;
		}
		CopyElementCommand cmd = new CopyElementCommand();

		for (Object obj : selectedObjects){
			if (! (obj instanceof EditPart)) continue;
			EditPart ep = (EditPart) obj;
			
			Object element = ep.getModel();
			if (!cmd.isCopyable(element)){
				cmd = null; 
				return null;
			}
			cmd.addElement(element);			
		}
		return cmd;
	}
		
	@Override
	protected boolean calculateEnabled() {
		@SuppressWarnings("unchecked")
		Command cmd = createCopyCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	@Override
	public void run() {
		@SuppressWarnings("unchecked")
		Command cmd = createCopyCommand(getSelectedObjects());
		if (cmd != null && cmd.canExecute()) {
			cmd.execute();
		}
	}

}
