package org.cheetahplatform.modeler.graph.command;

import java.util.Date;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class EditConditionAction extends Action {
	private static final String ID = "org.cheetahplatform.modeler.graph.command.EditConditionAction"; //$NON-NLS-1$
	private final EdgeEditPart editPart;

	public EditConditionAction(EdgeEditPart editPart) {
		this.editPart = editPart;

		setId(ID);
		setText(Messages.EditConditionAction_1);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/edit_condition.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		GraphElement model = editPart.getModel();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		InputDialog dialog = new InputDialog(shell, Messages.EditConditionAction_3, Messages.EditConditionAction_4, model.getName(), null);

		Date startTime = new Date();
		if (dialog.open() == Window.CANCEL) {
			return;
		}

		String newCondition = dialog.getValue();
		RenameCommand command = new RenameCommand(model, newCondition);
		command.setStartTime(startTime);
		editPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}
}
