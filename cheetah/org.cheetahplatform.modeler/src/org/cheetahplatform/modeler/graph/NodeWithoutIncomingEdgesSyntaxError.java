package org.cheetahplatform.modeler.graph;

public class NodeWithoutIncomingEdgesSyntaxError implements ISyntaxError {

	@Override
	public String getDescription() {
		return "Node without incoming edges";
	}

}
