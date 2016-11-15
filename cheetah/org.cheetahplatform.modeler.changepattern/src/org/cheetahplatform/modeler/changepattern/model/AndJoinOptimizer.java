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
 *         29.06.2010
 */
public class AndJoinOptimizer extends AbstractGraphOptimizer {

	public AndJoinOptimizer(Graph graph) {
		super(graph);
	}

	@Override
	protected boolean doOptimize() {
		List<Node> nodes = graph.getNodes();
		for (Node outerAndSplit : nodes) {
			if (!isAndGateway(outerAndSplit) || !isSplit(outerAndSplit)) {
				continue;
			}

			List<Edge> sourceConnections = outerAndSplit.getSourceConnections();
			for (Edge edge : sourceConnections) {
				Node innerAndSplit = edge.getTarget();
				if (isAndGateway(innerAndSplit) && isSplit(innerAndSplit)) {
					Node innerAndJoin = findCorrespondingAndJoin(innerAndSplit);
					Node outerAndJoin = innerAndJoin.getSourceConnections().get(0).getTarget();
					if (isAndGateway(outerAndJoin) && isJoin(outerAndJoin)) {
						SESEChecker seseChecker = new SESEChecker(outerAndSplit, outerAndJoin);
						if (seseChecker.isSESEFragment()) {
							performOptimization(innerAndSplit, innerAndJoin);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private void optimizeAndJoin(Node innerAndJoin) {
		Edge andJoinEdge = innerAndJoin.getSourceConnections().get(0);
		Node outerAndJoin = andJoinEdge.getTarget();

		List<Edge> targetConnections = innerAndJoin.getTargetConnections();
		for (Edge edge : targetConnections) {
			add(new ReconnectEdgeCommand(edge, edge.getSource(), outerAndJoin));
		}

		add(new DeleteEdgeCommand(andJoinEdge));
		add(new DeleteNodeCommand(innerAndJoin));
	}

	private void optimizeAndSplit(Node innerAndSplit) {
		Edge andSplitEdge = innerAndSplit.getTargetConnections().get(0);
		Node outerAndSplit = andSplitEdge.getSource();
		List<Edge> sourceConnections = innerAndSplit.getSourceConnections();
		for (Edge edge : sourceConnections) {
			add(new ReconnectEdgeCommand(edge, outerAndSplit, edge.getTarget()));
		}

		add(new DeleteEdgeCommand(andSplitEdge));
		add(new DeleteNodeCommand(innerAndSplit));
	}

	/**
	 * @param innerAndSplit
	 * @param innerAndJoin
	 */
	private void performOptimization(Node innerAndSplit, Node innerAndJoin) {
		optimizeAndSplit(innerAndSplit);
		optimizeAndJoin(innerAndJoin);
	}
}
