/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class GEFUtils {
	public static Point getAbsoluteLocation(AbstractGraphicalEditPart part) {
		IFigure figure = part.getFigure();
		return getAbsoluteLocation(figure);
	}

	public static Point getAbsoluteLocation(IFigure figure) {
		Point location = figure.getBounds().getLocation();

		while (figure.getParent() != null) {
			figure = figure.getParent();
			location.translate(figure.getBounds().getLocation());
		}

		return location;
	}

	public static org.eclipse.draw2d.geometry.Point getDropLocation(EditPartViewer viewer) {
		org.eclipse.swt.graphics.Point location = Display.getDefault().getCursorLocation();
		org.eclipse.swt.graphics.Point relativeLocation = viewer.getControl().toControl(location.x, location.y);
		org.eclipse.draw2d.geometry.Point dropLocation = new org.eclipse.draw2d.geometry.Point(relativeLocation);
		return dropLocation;
	}

	public static Point getLocation(Control control) {
		Point location = new Point();

		Control current = control;
		while (current != null) {
			org.eclipse.swt.graphics.Point relativeLocation = current.getLocation();
			location.x += relativeLocation.x;
			location.y += relativeLocation.y;
			current = current.getParent();
		}

		return location;
	}

	private static void paintChild(Graphics graphics, IFigure child) {
		Rectangle clip = Rectangle.SINGLETON;
		if (child.isVisible() && child.intersects(graphics.getClip(clip))) {
			graphics.clipRect(child.getBounds());
			child.paint(graphics);
			graphics.restoreState();
		}
	}

	/**
	 * Allows to paint children in a certain order, determined by their class.
	 * 
	 * @param figure
	 *            the figure whose children should be painted
	 * @param graphics
	 *            the graphics object to draw the children
	 * @param ordering
	 *            the ordering
	 */
	@SuppressWarnings("unchecked")
	public static void paintChildren(IFigure figure, Graphics graphics, List<Class<?>> ordering) {
		List<IFigure> children = new ArrayList<IFigure>(figure.getChildren());
		ordering.add(IFigure.class);

		for (Class<?> type : ordering) {
			Iterator<IFigure> iterator = children.iterator();
			while (iterator.hasNext()) {
				IFigure child = iterator.next();
				if (type.isAssignableFrom(child.getClass())) {
					paintChild(graphics, child);
					iterator.remove();
				}
			}
		}
	}
}
