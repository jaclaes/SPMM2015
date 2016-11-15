package org.cheetahplatform.modeler.changepattern.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.06.2010
 */
public class SESEChecker {
	private final Set<Node> nodes;
	private List<Edge> incomingEdges;
	private List<Edge> outgoingEdges;

	public SESEChecker(Node firstNode, Node lastNode) {
		nodes = new HashSet<Node>();
		addNodes(firstNode, lastNode);
		calculate();
	}

	public SESEChecker(Set<Node> nodes) {
		Assert.isNotNull(nodes);
		this.nodes = nodes;
		calculate();
	}

	private void addNodes(Node firstNode, Node lastNode) {
		if (nodes.contains(firstNode)) {
			return;
		}

		nodes.add(firstNode);
		if (firstNode.equals(lastNode)) {
			return;
		}
		List<Edge> sourceConnections = firstNode.getSourceConnections();
		for (Edge edge : sourceConnections) {
			addNodes(edge.getTarget(), lastNode);
		}
	}

	private void calculate() {
		incomingEdges = new ArrayList<Edge>();
		outgoingEdges = new ArrayList<Edge>();
		for (Node node : nodes) {
			for (Edge edge : node.getSourceConnections()) {
				if (!nodes.contains(edge.getTarget())) {
					outgoingEdges.add(edge);
				}
			}

			List<Edge> targetConnections = node.getTargetConnections();
			for (Edge edge : targetConnections) {
				if (!nodes.contains(edge.getSource())) {
					incomingEdges.add(edge);
				}
			}
		}
	}

	public Node getFirstNode() {
		return getIncomingEdge().getTarget();
	}

	public Edge getIncomingEdge() {
		if (getIncomingEdges().size() != 1) {
			throw new IllegalStateException("Not a valid SESE fragment. See isSESEFragment()");
		}

		return getIncomingEdges().get(0);
	}

	/**
	 * Returns the incomingEdges.
	 * 
	 * @return the incomingEdges
	 */
	public List<Edge> getIncomingEdges() {
		return Collections.unmodifiableList(incomingEdges);
	}

	public Node getLastNode() {
		return getOutgoingEdge().getSource();
	}

	public Set<Node> getNodes() {
		return Collections.unmodifiableSet(nodes);
	}

	public Edge getOutgoingEdge() {
		if (getOutgoingEdges().size() != 1) {
			throw new IllegalStateException("Not a valid SESE fragment. See isSESEFragment()");
		}
		return getOutgoingEdges().get(0);
	}

	/**
	 * Returns the outgoingEdges.
	 * 
	 * @return the outgoingEdges
	 */
	public List<Edge> getOutgoingEdges() {
		return Collections.unmodifiableList(outgoingEdges);
	}

	public boolean isSESEFragment() {
		return incomingEdges.size() == 1 && outgoingEdges.size() == 1;
	}
}
