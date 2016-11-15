package org.cheetahplatform.shared;

import java.util.ArrayList;
import java.util.List;

public class GraphHandle extends TypedHandle {
	private List<NodeHandle> nodes;
	private List<EdgeHandle> edges;

	public GraphHandle(long id, String name, String type) {
		super(id, name, type);

		nodes = new ArrayList<NodeHandle>();
		edges = new ArrayList<EdgeHandle>();
	}

	public void addEdge(EdgeHandle edge) {
		edges.add(edge);
	}

	public void addNode(NodeHandle node) {
		nodes.add(node);
	}

	/**
	 * @return the edges
	 */
	public List<EdgeHandle> getEdges() {
		return edges;
	}

	/**
	 * @return the nodes
	 */
	public List<NodeHandle> getNodes() {
		return nodes;
	}

}
