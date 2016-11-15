package org.cheetahplatform.modeler.generic.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Path;

/**
 * Interface for all figures described by a path object.
 * 
 * @author Stefan Zugal
 * 
 */
public interface IPathFigure extends IFigure {

	/**
	 * Compute the path outlining the figure.
	 * 
	 * @return the path
	 */
	Path computePath();

	/**
	 * Compute the path outlining the figure.
	 * 
	 * @param point
	 *            the location where the path should start
	 * 
	 * @return the path
	 */
	Path computePath(Point point);
}
