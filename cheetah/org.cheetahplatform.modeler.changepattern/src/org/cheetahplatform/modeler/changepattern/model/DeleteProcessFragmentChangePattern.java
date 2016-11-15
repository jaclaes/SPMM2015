package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         18.06.2010
 */
public class DeleteProcessFragmentChangePattern extends AbstractChangePattern {

	public static final String DELETE_PROCESS_FRAGMENT_CHANGE_PATTERN = "DELETE_PROCESS_FRAGMENT";

	public DeleteProcessFragmentChangePattern(Graph graph, Edge incomingEdge, Edge outgoingEdge, Set<Node> nodes) {
		super(graph);
		add(new ReconnectEdgeCommand(incomingEdge, incomingEdge.getSource(), outgoingEdge.getTarget()));
		
		List<Edge> removedEdges = new ArrayList<Edge>();

		for (Node node : nodes) {
			List<Edge> sourceConnections = node.getSourceConnections();
			for (Edge edge : sourceConnections) {
				add(new DeleteEdgeCommand(edge));
				removedEdges.add(edge);
			}

			add(new DeleteNodeCommand(node));
		}

		Point incoming = incomingEdge.getSource().getBounds().getTopRight().getCopy();
		incoming.y = 0;
		Point outgoing = outgoingEdge.getTarget().getBounds().getTopLeft().getCopy();
		outgoing.y = 0;

		if (otherNodesFound(incoming, outgoing, nodes)) {
			return;
		}

		Point delta = incoming.translate(outgoing.getNegated()).translate(20, 0);
		moveHorizontally(outgoing.x, delta, removedEdges);
	}

	@Override
	protected List<IGraphOptimizer> getGraphOptimizers() {
		List<IGraphOptimizer> optimizers = new ArrayList<IGraphOptimizer>();
		optimizers.add(new XorGraphOptimizer(graph));
		optimizers.add(new LoopGraphOptimizer(graph));
		optimizers.add(new AndGraphOptimizer(graph));
		return optimizers;
	}

	@Override
	public String getName() {
		return DELETE_PROCESS_FRAGMENT_CHANGE_PATTERN;
	}

	/**
	 * @param incoming
	 * @param outgoing
	 * @param nodes
	 * @return
	 */
	private boolean otherNodesFound(Point incoming, Point outgoing, Set<Node> removedNodes) {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			if (removedNodes.contains(node)) {
				continue;
			}

			Rectangle bounds = node.getBounds();
			if (bounds.x > incoming.x && bounds.x < outgoing.x) {
				return true;
			}
		}
		return false;
	}
}
