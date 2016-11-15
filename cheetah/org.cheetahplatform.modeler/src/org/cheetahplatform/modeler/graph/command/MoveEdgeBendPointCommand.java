package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.geometry.Point;

public class MoveEdgeBendPointCommand extends EdgeCommand {

	private final int toMove;
	private final Point newLocation;
	private Point oldLocation;

	public MoveEdgeBendPointCommand(Edge edge, int toMove, Point newLocation) {
		super(edge.getGraph(), edge);

		this.toMove = toMove;
		this.newLocation = newLocation;
	}

	@Override
	public void execute() {
		Edge edge = (Edge) element;
		if (edge.getBendPointCount() == 0) {
			return;
		}

		oldLocation = edge.getBendPoint(toMove).getCopy();
		edge.moveBendPoint(toMove, newLocation);

		AuditTrailEntry entry = createAuditrailEntry(MOVE_EDGE_BENDPOINT);
		entry.setAttribute(INDEX, toMove);
		entry.setAttribute(X, newLocation.x);
		entry.setAttribute(Y, newLocation.y);
		log(entry);
	}

	@Override
	public void undo() {
		new MoveEdgeBendPointCommand((Edge) element, toMove, oldLocation).execute();
	}
}
