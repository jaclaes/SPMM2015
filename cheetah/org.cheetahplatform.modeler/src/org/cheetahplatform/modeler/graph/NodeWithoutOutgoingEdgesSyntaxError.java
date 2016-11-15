package org.cheetahplatform.modeler.graph;

public class NodeWithoutOutgoingEdgesSyntaxError implements ISyntaxError {

	@Override
	public String getDescription() {
		return "Node without outgoing edges";
	}

}
