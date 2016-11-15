package org.cheetahplatform.modeler.graph;

public class EndNodeWithoutIncomingEdgesSyntaxError implements ISyntaxError {

	@Override
	public String getDescription() {
		return "End node without incoming edges";
	}
}
