package org.cheetahplatform.modeler.graph.action;

import java.util.Date;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class RenameGraphElementAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.graph.action.RenameNodeAction"; //$NON-NLS-1$
	private final EditPart editPart;

	public RenameGraphElementAction(EditPart editPart) {
		this.editPart = editPart;
		setId(ID);
		setText(Messages.RenameGraphElementAction_1);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/rename.gif")); //$NON-NLS-1$
	}

	@SuppressWarnings("cast")
	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		GraphElement element = (GraphElement) editPart.getModel();
		String name = element.getName();
		Date startTime = new Date();
		InputDialog dialog = new InputDialog(shell, Messages.RenameGraphElementAction_3, Messages.RenameGraphElementAction_4, name, element
				.getDescriptor().createRenameValidator(element));

		if (dialog.open() == Window.OK) {
			RenameCommand command = new RenameCommand((Node) element, dialog.getValue());
			command.setStartTime(startTime);
			editPart.getViewer().getEditDomain().getCommandStack().execute(command);
		}
	}
}
