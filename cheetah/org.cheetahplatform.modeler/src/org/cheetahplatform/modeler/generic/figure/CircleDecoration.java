/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class CircleDecoration extends PolygonDecoration {
	public static final int SOURCE = 0;
	public static final int TARGET = 1;

	protected int mode;

	public CircleDecoration() {
		this(SOURCE);
	}

	public CircleDecoration(int mode) {
		this.mode = mode;

		setBackgroundColor(SWTResourceManager.getColor(0, 0, 0));
	}

	@Override
	protected void fillShape(Graphics g) {
		outlineShape(g);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		PolylineConnection parent = (PolylineConnection) getParent();
		PointList points = parent.getPoints();
		Point first = points.getFirstPoint();
		Point second = points.getPoint(1);

		if (mode == TARGET) {
			first = points.getLastPoint();
			second = points.getPoint(points.size() - 2);
		}

		Point vertex = second.getCopy().translate(first.getCopy().negate());
		double length = Math.sqrt(Math.pow(vertex.x, 2) + Math.pow(vertex.y, 2));
		double factor = 3 / length;

		Point location = first.getCopy();
		vertex.scale(factor);
		location.translate(vertex);

		location.translate(-4, -4);

		graphics.pushState();
		graphics.setAntialias(SWT.ON);
		Color backgroundColor = getBackgroundColor();
		if (backgroundColor.getRGB().equals(new RGB(240, 240, 240))) {
			backgroundColor = SWTResourceManager.getColor(0, 0, 0);
		}
		graphics.setBackgroundColor(backgroundColor);
		graphics.setClip(new Rectangle(0, 0, 10000, 10000));
		graphics.fillOval(location.x, location.y, 10, 10);
		graphics.popState();
	}

}
