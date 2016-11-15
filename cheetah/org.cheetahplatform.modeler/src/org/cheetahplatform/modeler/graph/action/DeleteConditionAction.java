package org.cheetahplatform.modeler.graph.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.editpart.EdgeLabelEditPart;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class DeleteConditionAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.graph.action.DeleteConditionAction"; //$NON-NLS-1$

	private final EdgeLabelEditPart editPart;

	public DeleteConditionAction(EdgeLabelEditPart editPart) {
		this.editPart = editPart;

		setId(ID);
		setText(Messages.DeleteConditionAction_1);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/delete.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		RenameCommand command = new RenameCommand((GraphElement) editPart.getParent().getModel(), null);
		editPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}
}
