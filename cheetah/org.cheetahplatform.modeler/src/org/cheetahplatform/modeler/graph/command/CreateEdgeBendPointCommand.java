package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.geometry.Point;

public class CreateEdgeBendPointCommand extends EdgeCommand {

	private final Point location;
	private final int index;

	public CreateEdgeBendPointCommand(Edge edge, Point location, int index) {
		super(edge.getGraph(), edge);

		this.location = location;
		this.index = index;
	}

	@Override
	public void execute() {
		((Edge) element).addBendPoint(location, index);

		AuditTrailEntry entry = createAuditrailEntry(CREATE_EDGE_BENDPOINT);
		entry.setAttribute(X, location.x);
		entry.setAttribute(Y, location.y);
		entry.setAttribute(INDEX, index);
		log(entry);
	}

	@Override
	public void undo() {
		new DeleteEdgeBendPointCommand((Edge) element, index).execute();
	}

}
