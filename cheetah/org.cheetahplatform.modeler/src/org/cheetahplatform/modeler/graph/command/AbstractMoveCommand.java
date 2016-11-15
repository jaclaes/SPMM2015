package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.ILocated;
import org.eclipse.draw2d.geometry.Point;

public abstract class AbstractMoveCommand extends NodeCommand {

	protected final Point moveDelta;

	private final String logType;

	protected AbstractMoveCommand(GraphElement element, Point moveDelta, String logType) {
		super(element.getGraph(), element);

		this.moveDelta = moveDelta;
		this.logType = logType;
	}

	@Override
	public void execute() {
		ILocated located = getLocated();
		Point location = located.getLocation().getCopy();
		location.translate(moveDelta);
		validateNewLocation(location);
		located.move(moveDelta);

		AuditTrailEntry entry = createAuditrailEntry(logType);
		entry.setAttribute(X, moveDelta.x);
		entry.setAttribute(Y, moveDelta.y);
		log(entry);
	}

	protected ILocated getLocated() {
		return (ILocated) element;
	}

	public Point getMoveDelta() {
		return moveDelta;
	}

	/**
	 * Determine whether the given new location is appropriate, if not adapt the moveDelta.
	 * 
	 * @param location
	 *            the new location
	 */
	protected void validateNewLocation(@SuppressWarnings("unused") Point location) {
		// subclasses may overwrite
	}
}
