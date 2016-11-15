package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.ResourceManager;

public class EditSelectionConstraintAction extends Action {
	public static final String ID = "";
	private final EdgeEditPart editPart;

	public EditSelectionConstraintAction(EdgeEditPart editPart) {
		Assert.isNotNull(editPart);

		this.editPart = editPart;
		setId(ID);
		setText("Set Minimum/Maximum");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/edit_condition.gif"));
	}

	@Override
	public void run() {
		Shell shell = editPart.getViewer().getControl().getShell();
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) editPart.getModel();

		// little bit weird here ;-) The dialog directly sets minimum/maximum in the constraint (which is nice for most cases). However,
		// we want to log these changes (and provide redo support), so we need to encapsulate the changes in a command.
		EditSelectionConstraintDialog dialog = new EditSelectionConstraintDialog(shell, constraint);
		int oldMinimum = constraint.getMinimum();
		int oldMaximum = constraint.getMaximum();

		if (dialog.open() == Window.OK) {
			int minimum = constraint.getMinimum();
			int maximum = constraint.getMaximum();

			constraint.setMinimum(oldMinimum);
			constraint.setMaximum(oldMaximum);

			// execute the command to log the changes
			EditSelectionConstraintCommand command = new EditSelectionConstraintCommand(constraint, minimum, maximum);
			editPart.getViewer().getEditDomain().getCommandStack().execute(command);
		}
	}
}
