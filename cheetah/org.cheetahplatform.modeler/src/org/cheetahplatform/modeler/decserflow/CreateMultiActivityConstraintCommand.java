package org.cheetahplatform.modeler.decserflow;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.decserflow.descriptor.DeleteMultiActivityConstraintCommand;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;

public class CreateMultiActivityConstraintCommand extends CompoundCommandWithAttributes {

	protected final Graph graph;
	protected final List<Node> nodes;
	protected final List<Edge> incomingEdges;
	private final List<Edge> outgoingEdges;
	protected final Node auxiliaryNode;
	private final MultiActivityConstraintDescriptor descriptor;

	public CreateMultiActivityConstraintCommand(Graph graph, List<Node> nodes, List<Edge> incomingEdges, List<Edge> outgoingEdges,
			Node auxiliaryNode, MultiActivityConstraintDescriptor descriptor) {
		Assert.isTrue(incomingEdges.size() + outgoingEdges.size() == nodes.size());

		this.graph = graph;
		this.nodes = nodes;
		this.incomingEdges = incomingEdges;
		this.outgoingEdges = outgoingEdges;
		this.auxiliaryNode = auxiliaryNode;
		this.descriptor = descriptor;

		setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, "Create " + descriptor.getName());
	}

	protected Point computeAuxiliaryNodeLocation() {
		Point center = new Point();
		for (Node node : nodes) {
			center.translate(node.getLocation());
		}

		center.scale(1.0 / nodes.size());
		return center;
	}

	protected CreateNodeCommand createCreateAuxiliaryNodeCommand() {
		return new CreateNodeCommand(graph, auxiliaryNode, computeAuxiliaryNodeLocation());
	}

	@Override
	public void execute() {
		getCommands().clear();

		add(createCreateAuxiliaryNodeCommand());

		for (int i = 0; i < incomingEdges.size(); i++) {
			CreateEdgeCommand createEdgeCommand = new CreateEdgeCommand(graph, incomingEdges.get(i), nodes.get(i), auxiliaryNode, "");
			add(createEdgeCommand);
		}
		for (int i = 0; i < outgoingEdges.size(); i++) {
			CreateEdgeCommand createEdgeCommand = new CreateEdgeCommand(graph, outgoingEdges.get(i), auxiliaryNode, nodes.get(i
					+ incomingEdges.size()), "");
			add(createEdgeCommand);
		}

		super.execute();
	}

	@Override
	public void undo() {
		// it is sufficient to delete an arbitrary edge as all other edges and the auxiliary node is deleted automatically by the command
		new DeleteMultiActivityConstraintCommand(incomingEdges.get(0), descriptor).execute();
	}
}
