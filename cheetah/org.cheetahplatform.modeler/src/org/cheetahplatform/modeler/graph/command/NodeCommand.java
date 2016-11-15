package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;

public abstract class NodeCommand extends AbstractGraphCommand {

	protected NodeCommand(Graph graph, GraphElement element) {
		super(graph, element);
	}

	@Override
	protected String getAffectedElementName() {
		return getFullName(element);
	}

	public Node getNode() {
		return (Node) element;
	}

}
