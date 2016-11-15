package org.cheetahplatform.modeler.changepattern.model;

import java.util.List;

import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.06.2010
 */
public class LoopGraphOptimizer extends AbstractGraphOptimizer {

	public LoopGraphOptimizer(Graph graph) {
		super(graph);
	}

	@Override
	protected boolean doOptimize() {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (!isXorGateway(node) || !isJoin(node)) {
				continue;
			}

			Edge edge = node.getSourceConnections().get(0);
			Node target = edge.getTarget();
			if (!isXorGateway(target) || !isSplit(target)) {
				continue;
			}

			List<Edge> sourceConnections = target.getSourceConnections();
			Edge edge2 = sourceConnections.get(0);
			Edge edge3 = sourceConnections.get(1);

			if (edge2.getTarget().equals(node)) {
				remove(node, edge, target, edge2, edge3);
				return true;
			}
			if (edge3.getTarget().equals(node)) {
				remove(node, edge, target, edge3, edge2);
				return true;
			}
		}
		return false;
	}

	private void remove(Node node, Edge edge, Node target, Edge edge2, Edge outgoingEdge) {
		Edge incomingEdge = getEdgeToNotComingFromNode(node, target);
		add(new ReconnectEdgeCommand(incomingEdge, incomingEdge.getSource(), outgoingEdge.getTarget()));
		add(new DeleteEdgeCommand(edge));
		add(new DeleteEdgeCommand(edge2));
		add(new DeleteEdgeCommand(outgoingEdge));
		add(new DeleteNodeCommand(node));
		add(new DeleteNodeCommand(target));
	}
}
