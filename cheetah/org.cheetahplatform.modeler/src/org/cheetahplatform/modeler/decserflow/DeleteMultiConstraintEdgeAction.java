package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class DeleteMultiConstraintEdgeAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.decserflow.DeleteMultiConstraintEdgeAction";
	private final EdgeEditPart editPart;

	public DeleteMultiConstraintEdgeAction(EdgeEditPart editPart) {
		this.editPart = editPart;

		setId(ID);
		setText("Delete Constraint Branch");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/decserflow/delete_branch.png"));
	}

	@Override
	public void run() {
		DeleteEdgeCommand command = new DeleteEdgeCommand(editPart.getModel());
		editPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}
}
