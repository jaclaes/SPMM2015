package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class CreateMultiConstraintIncomingEdgeAction extends AbstractExpandMultiConstraintEdgeAction {
	public static final String ID = "org.cheetahplatform.modeler.decserflow.CreateMultiConstraintIncomingEdgeAction";

	public CreateMultiConstraintIncomingEdgeAction(NodeEditPart editPart, MultiActivityConstraintDescriptor descriptor) {
		super(editPart, descriptor);

		setId(ID);
		setText("Add Incoming Branch");
	}

	@Override
	public CreateEdgeCommand createCreateCommand(Node node, Edge edge, Node selectedNode) {
		return new CreateEdgeCommand(node.getGraph(), edge, selectedNode, node, "");
	}

}
