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
public class XorGraphOptimizer extends AbstractGraphOptimizer {

	public XorGraphOptimizer(Graph graph) {
		super(graph);
	}

	@Override
	protected boolean doOptimize() {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (!isXorGateway(node) || !isSplit(node)) {
				continue;
			}

			List<Edge> sourceConnections = node.getSourceConnections();
			Edge edge1 = sourceConnections.get(0);
			Edge edge2 = sourceConnections.get(1);

			if (edge1.getTarget().equals(edge2.getTarget())) {
				Edge incomingEdge = node.getTargetConnections().get(0);
				Node xorjoin = edge1.getTarget();
				Edge outgoingEdge = xorjoin.getSourceConnections().get(0);
				add(new ReconnectEdgeCommand(incomingEdge, incomingEdge.getSource(), outgoingEdge.getTarget()));
				add(new DeleteEdgeCommand(edge1));
				add(new DeleteEdgeCommand(edge2));
				add(new DeleteEdgeCommand(outgoingEdge));
				add(new DeleteNodeCommand(xorjoin));
				add(new DeleteNodeCommand(node));
				return true;
			}
		}
		return false;
	}
}
