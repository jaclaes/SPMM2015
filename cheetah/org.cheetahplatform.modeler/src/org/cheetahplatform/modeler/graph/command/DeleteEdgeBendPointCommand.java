package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.geometry.Point;

public class DeleteEdgeBendPointCommand extends EdgeCommand {

	private int index;
	private Point deletedPoint;

	public DeleteEdgeBendPointCommand(Edge edge, int index) {
		super(edge.getGraph(), edge);

		this.index = index;
	}

	@Override
	public void execute() {
		Edge edge = (Edge) element;
		deletedPoint = edge.removeBendPoint(index);

		AuditTrailEntry entry = createAuditrailEntry(AbstractGraphCommand.DELETE_EDGE_BENDPOINT);
		entry.setAttribute(INDEX, index);
		log(entry);
	}

	@Override
	public void undo() {
		new CreateEdgeBendPointCommand((Edge) element, deletedPoint, index).execute();
	}

}
