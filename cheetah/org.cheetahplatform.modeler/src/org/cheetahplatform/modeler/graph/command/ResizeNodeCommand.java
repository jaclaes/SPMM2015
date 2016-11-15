package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Dimension;

public class ResizeNodeCommand extends NodeCommand {

	private final Dimension newSize;
	private Dimension oldSize;

	public ResizeNodeCommand(Graph graph, GraphElement element, Dimension newSize) {
		super(graph, element);

		this.newSize = newSize;
	}

	@Override
	public void execute() {
		Node node = getNode();
		oldSize = node.getBounds().getSize();
		if (newSize.width < 30) {
			newSize.width = 30;
		}
		if (newSize.height < 30) {
			newSize.height = 30;
		}
		node.setSize(newSize);

		AuditTrailEntry entry = createAuditrailEntry(AbstractGraphCommand.RESIZE_NODE);
		entry.setAttribute(WIDTH, newSize.width);
		entry.setAttribute(HEIGHT, newSize.height);
		log(entry);
	}

	@Override
	public void undo() {
		new ResizeNodeCommand(getGraph(), getNode(), oldSize).execute();
	}

}
