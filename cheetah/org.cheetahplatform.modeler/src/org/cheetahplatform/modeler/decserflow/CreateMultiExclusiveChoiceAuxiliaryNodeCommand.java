package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;

public class CreateMultiExclusiveChoiceAuxiliaryNodeCommand extends CreateNodeCommand {
	public static final String MINIMUM = "minimum";

	private int minimum;

	public CreateMultiExclusiveChoiceAuxiliaryNodeCommand(Graph graph, Node node, Point location, String name, int minimum) {
		super(graph, node, location, name);

		this.minimum = minimum;
	}

	@Override
	public void execute() {
		MultiExclusiveChoiceAuxiliaryNode node = (MultiExclusiveChoiceAuxiliaryNode) getNode();
		node.setMinimum(minimum);

		super.execute();
	}
}
