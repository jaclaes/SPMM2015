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
public class AndGraphOptimizer extends AbstractGraphOptimizer {

	public AndGraphOptimizer(Graph graph) {
		super(graph);
	}

	@Override
	protected boolean doOptimize() {
		List<Node> nodes = graph.getNodes();
		for (Node andSplit : nodes) {
			if (!isSplit(andSplit) || !isAndGateway(andSplit)) {
				continue;
			}

			List<Edge> sourceConnections = andSplit.getSourceConnections();
			if (sourceConnections.size() == 2) {
				return handleAndWithTwoBranches(andSplit);
			}

			for (Edge edge : sourceConnections) {
				Node target = edge.getTarget();
				SESEChecker seseChecker = new SESEChecker(andSplit, target);
				if (seseChecker.isSESEFragment()) {
					add(new DeleteEdgeCommand(edge));
					return true;
				}
			}
		}

		return false;
	}

	private boolean handleAndWithTwoBranches(Node node) {
		Edge edge1 = node.getSourceConnections().get(0);
		Edge edge2 = node.getSourceConnections().get(1);
		Edge incomingEdge = node.getTargetConnections().get(0);

		Node target1 = edge1.getTarget();
		if (isJoin(target1) && isAndGateway(target1)) {
			remove(node, edge1, edge2, incomingEdge);
			return true;
		}

		Node target2 = edge2.getTarget();
		if (isJoin(target2) && isAndGateway(target2)) {
			remove(node, edge2, edge1, incomingEdge);
			return true;
		}

		return false;
	}

	private void remove(Node node, Edge edge1, Edge edge2, Edge incomingEdge) {
		Node target1 = edge1.getTarget();
		Node target2 = edge2.getTarget();
		add(new ReconnectEdgeCommand(incomingEdge, incomingEdge.getSource(), target2));

		Edge edge = getEdgeToNotComingFromNode(target1, node);
		Edge outgoingEdge = target1.getSourceConnections().get(0);
		add(new ReconnectEdgeCommand(edge, edge.getSource(), outgoingEdge.getTarget()));

		add(new DeleteEdgeCommand(edge1));
		add(new DeleteEdgeCommand(edge2));
		add(new DeleteEdgeCommand(outgoingEdge));
		add(new DeleteNodeCommand(node));
		add(new DeleteNodeCommand(target1));
	}

}
