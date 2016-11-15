package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.tdm.dialog.EditMessageDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

public class EditNodeDescriptionAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.decserflow.descriptor.EditNodeDescriptionAction";

	private final NodeEditPart editPart;

	public EditNodeDescriptionAction(NodeEditPart editPart) {
		setId(ID);
		setText("Edit Description");

		this.editPart = editPart;
	}

	@Override
	public void run() {
		String message = (String) editPart.getModel().getProperty(EditNodeDescriptionCommand.ATTRIBUTE_DESCRIPTION);
		EditMessageDialog dialog = new EditMessageDialog(Display.getDefault().getActiveShell(), message);
		if (dialog.open() != Window.OK) {
			return;
		}

		String newMessage = dialog.getMessage();
		EditNodeDescriptionCommand command = new EditNodeDescriptionCommand(editPart.getModel(), newMessage);
		editPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}

}
