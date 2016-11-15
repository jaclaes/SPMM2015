package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class EdgeCommand extends AbstractGraphCommand {

	protected EdgeCommand(Graph graph, GraphElement element) {
		super(graph, element);
	}

	@Override
	protected String getAffectedElementName() {
		return getEdgeName((Edge) element, null);
	}

	public Edge getEdge() {
		return (Edge) element;
	}

}
