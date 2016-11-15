package org.cheetahplatform.modeler.decserflow.figure;

import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class FilledArrowCircleDecoration extends CircleDecoration {

	private FilledArrowFigure arrow;

	public FilledArrowCircleDecoration() {
		super();
	}

	public FilledArrowCircleDecoration(int mode) {
		super(mode);
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds().getCopy();
		if (arrow == null) {
			return bounds;
		}

		return bounds.getCopy().expand(bounds.width, bounds.height);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		super.outlineShape(graphics);

		PolylineConnection parent = (PolylineConnection) getParent();
		PointList points = parent.getPoints();
		Point first = points.getFirstPoint();
		Point second = points.getPoint(1);

		if (mode == TARGET) {
			first = points.getPoint(points.size() - 2);
			second = points.getLastPoint();
		}

		Point vector = second.getCopy().translate(first.getCopy().negate());
		double length = vector.getDistance(new Point());
		double desiredLength = 8;
		if (length != 0.0) {
			vector.scale(desiredLength / length);
		}

		Point referencePoint = first;
		Point arrowLocation = first.getCopy().translate(vector);
		if (mode == TARGET) {
			arrowLocation = second.getCopy().translate(vector.getCopy().negate());
		}

		graphics.setClip(new Rectangle(0, 0, 10000, 10000));
		arrow = new FilledArrowFigure();
		Color backgroundColor = getBackgroundColor();
		if (backgroundColor.getRGB().equals(new RGB(240, 240, 240))) {
			backgroundColor = SWTResourceManager.getColor(0, 0, 0);
		}
		arrow.setBackgroundColor(backgroundColor);
		arrow.setLocation(arrowLocation);
		arrow.setReferencePoint(referencePoint);
		arrow.setScale(10, 6);
		arrow.paint(graphics);
	}

}
