package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;

public class VerticalScrollCommand extends AbstractScrollCommand {

	public VerticalScrollCommand(Graph graph, GraphElement element, int min, int max, int extent, int value) {
		super(graph, element, min, max, extent, value);
	}

	@Override
	protected String getAffectedElementName() {
		return "Vertical Scroll";
	}

	@Override
	public RangeModel getRangeModel(Viewport viewport) {
		return viewport.getVerticalRangeModel();
	}
}
