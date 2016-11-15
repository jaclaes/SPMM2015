package org.cheetahplatform.modeler.decserflow;

import java.util.List;

import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class CreateMultiExclusionConstraintCommand extends CreateMultiActivityConstraintCommand {

	private final int minimum;

	public CreateMultiExclusionConstraintCommand(Graph graph, List<Node> nodes, List<Edge> incomingEdges, List<Edge> outgoingEdges,
			Node auxiliaryNode, MultiActivityConstraintDescriptor descriptor, int mimimum) {
		super(graph, nodes, incomingEdges, outgoingEdges, auxiliaryNode, descriptor);

		this.minimum = mimimum;
	}

	@Override
	protected CreateNodeCommand createCreateAuxiliaryNodeCommand() {
		String name = minimum + " out of " + nodes.size();
		return new CreateMultiExclusiveChoiceAuxiliaryNodeCommand(graph, auxiliaryNode, computeAuxiliaryNodeLocation(), name, minimum);
	}

	@Override
	public void undo() {
		new DeleteMultiExclusionConstraintCommand(incomingEdges.get(0)).execute();
	}

}
