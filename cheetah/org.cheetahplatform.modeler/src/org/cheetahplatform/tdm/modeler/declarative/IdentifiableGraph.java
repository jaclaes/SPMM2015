package org.cheetahplatform.tdm.modeler.declarative;

import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.modeler.graph.model.Graph;

public class IdentifiableGraph extends IdentifiableObject {

	private static final long serialVersionUID = 2327154555323478341L;

	private Graph graph;

	public IdentifiableGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

}
