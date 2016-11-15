package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.modeler.ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.decserflow.CreateMultiActivityConstraintCommand;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class DeleteMultiActivityConstraintCommand extends CompoundCommandWithAttributes {

	protected final Edge toDelete;
	protected List<Edge> incomingEdges;
	protected List<Edge> outgoingEdges;
	protected List<Node> nodes;
	protected Node auxiliaryNode;
	protected MultiActivityConstraintDescriptor descriptor;

	public DeleteMultiActivityConstraintCommand(Edge toDelete, MultiActivityConstraintDescriptor descriptor) {
		this.toDelete = toDelete;
		this.descriptor = descriptor;

		setAttribute(ATTRIBUTE_COMPOUND_COMMAND_NAME, "Delete " + descriptor.getName());
	}

	@Override
	public void execute() {
		getCommands().clear();
		nodes = new ArrayList<Node>();
		incomingEdges = new ArrayList<Edge>();
		outgoingEdges = new ArrayList<Edge>();

		if (toDelete.getSource().getDescriptor() instanceof AuxiliaryNodeDescriptor) {
			auxiliaryNode = toDelete.getSource();
		} else {
			auxiliaryNode = toDelete.getTarget();
		}

		for (Edge connectedWithAuxiliaryNode : auxiliaryNode.getTargetConnections()) {
			add(new DeleteEdgeCommand(connectedWithAuxiliaryNode));
			incomingEdges.add(connectedWithAuxiliaryNode);
			nodes.add(connectedWithAuxiliaryNode.getSource());
		}
		for (Edge connectedWithAuxiliaryNode : auxiliaryNode.getSourceConnections()) {
			add(new DeleteEdgeCommand(connectedWithAuxiliaryNode));
			outgoingEdges.add(connectedWithAuxiliaryNode);
			nodes.add(connectedWithAuxiliaryNode.getTarget());
		}
		add(new DeleteNodeCommand(auxiliaryNode));

		super.execute();
	}

	@Override
	public void undo() {
		new CreateMultiActivityConstraintCommand(toDelete.getGraph(), nodes, incomingEdges, outgoingEdges, auxiliaryNode, descriptor)
				.execute();
	}
}
