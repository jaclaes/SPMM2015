package org.cheetahplatform.modeler.graph.editpart;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class DeleteBendPointsAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.graph.editpart.DeleteBendPointsAction";

	private final EdgeEditPart editPart;

	public DeleteBendPointsAction(EdgeEditPart editPart) {
		setId(ID);
		setText("Delete Bendpoints");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/delete_bend_points.gif"));

		this.editPart = editPart;
	}

	@Override
	public void run() {
		Edge edge = editPart.getModel();
		if (edge.getBendPointCount() == 0) {
			return;
		}

		CompoundCommandWithAttributes compoundCommand = new CompoundCommandWithAttributes();
		compoundCommand.setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, "Delete all bendpoints of edge");
		for (int i = 0; i < edge.getBendPointCount(); i++) {
			DeleteEdgeBendPointCommand command = new DeleteEdgeBendPointCommand(edge, 0);
			compoundCommand.add(command);
		}

		editPart.getViewer().getEditDomain().getCommandStack().execute(compoundCommand);
	}

}
