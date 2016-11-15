package org.cheetahplatform.modeler.graph.model;

import org.eclipse.draw2d.geometry.Point;

public interface ILocated {
	/**
	 * Determine the location of the object.
	 * 
	 * @return the location
	 */
	Point getLocation();

	/**
	 * Move the object.
	 * 
	 * @param delta
	 *            the delta to be moved
	 */
	void move(Point delta);
}
