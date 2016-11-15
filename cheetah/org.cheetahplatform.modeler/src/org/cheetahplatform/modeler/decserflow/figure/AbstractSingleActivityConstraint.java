package org.cheetahplatform.modeler.decserflow.figure;

import java.lang.reflect.Field;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public abstract class AbstractSingleActivityConstraint extends SelectablePolylineConnection {
	private static final int HEIGHT = 20;
	private static final int WIDTH = 50;

	private Rectangle constraintBounds;

	public AbstractSingleActivityConstraint(Edge edge) {
		super(edge);

		setBackgroundColor(SWTResourceManager.getColor(0, 0, 0));
		setForegroundColor(SWTResourceManager.getColor(0, 0, 0));
	}

	/**
	 * Compute the outline of the constraint.
	 * 
	 * @return the points indicating the constraint's outline
	 */
	private PointList computePoints() {
		Node source = edge.getSource();
		Edge concurrentConstraint = null;

		List<Edge> sourceConnections = source.getSourceConnections();
		for (Edge current : sourceConnections) {
			if (current.getTarget().equals(source) && !current.equals(edge)) {
				concurrentConstraint = current;
				break;
			}
		}

		ConnectionAnchor anchor = getSourceAnchor();
		IFigure targetedActivity = anchor.getOwner();
		Point startingPoint = anchor.getReferencePoint().getCopy();

		if (targetedActivity != null) {
			Rectangle bounds = targetedActivity.getBounds();
			startingPoint.y = bounds.y;
			startingPoint.x = bounds.x + (bounds.width - WIDTH) / 2;
		}

		if (concurrentConstraint != null) {
			boolean isConcurrentBefore = sourceConnections.indexOf(edge) > sourceConnections.indexOf(concurrentConstraint);
			int offset = 3;
			if (isConcurrentBefore) {
				startingPoint.x = startingPoint.x + WIDTH / 2 + offset;
			} else {
				startingPoint.x = startingPoint.x - WIDTH / 2 - offset;
			}
		}

		Point bottomLeft = new Point(startingPoint.x, startingPoint.y + 1);
		Point topLeft = new Point(startingPoint.x, startingPoint.y - HEIGHT);
		Point topRight = new Point(startingPoint.x + WIDTH, startingPoint.y - HEIGHT);
		Point bottomRight = new Point(startingPoint.x + WIDTH, startingPoint.y);

		PointList points = new PointList(4);
		points.addPoint(bottomLeft);
		points.addPoint(topLeft);
		points.addPoint(topRight);
		points.addPoint(bottomRight);
		constraintBounds = new Rectangle(topLeft.x, topLeft.y, topRight.x - topLeft.x, bottomLeft.y - topLeft.y);

		return points;
	}

	@Override
	public boolean containsPoint(int x, int y) {
		if (constraintBounds == null) {
			return super.containsPoint(x, y);
		}

		return constraintBounds.contains(x, y);
	}

	/**
	 * Compute the constraint's text.
	 * 
	 * @return the text
	 */
	protected abstract String getText();

	@Override
	protected void outlineShape(Graphics g) {
		PointList points = computePoints();
		Point bottomLeft = points.getPoint(0);
		Point topLeft = points.getPoint(1);
		Point topRight = points.getPoint(2);
		Point bottomRight = points.getPoint(3);

		Color color = getBackgroundColor();
		if (color.getRGB().equals(new RGB(240, 240, 240))) {
			color = SWTResourceManager.getColor(0, 0, 0);
		}
		g.setBackgroundColor(color);
		g.setForegroundColor(color);

		g.drawLine(bottomLeft, topLeft);
		g.drawLine(topLeft, topRight);
		g.drawLine(topRight, bottomRight);

		// draw text
		String text = getText();
		int textWidth = g.getFontMetrics().getAverageCharWidth() * text.length();

		// try a more precise computation of the text length
		try {
			Field gcField = g.getClass().getDeclaredField("gc");
			gcField.setAccessible(true);
			GC gc = (GC) gcField.get(g);
			textWidth = gc.textExtent(text).x;
		} catch (Exception e) {
			Activator.logError("Could not acquire the gc for more precise text placement.", e);
		}

		int xText = bottomLeft.x + (WIDTH - textWidth) / 2;
		int yText = bottomLeft.y - g.getFontMetrics().getHeight();
		g.drawString(text, xText, yText);
	}

	@Override
	public void setPoints(PointList points) {
		points = computePoints();

		super.setPoints(points);
	}

	@Override
	public void setTargetAnchor(ConnectionAnchor anchor) {
		// needed, as we only have a source, but never a target ... still some methods try to set/get the target anchor (which never exists)
		super.setSourceAnchor(anchor);
		super.setTargetAnchor(anchor);
	}

}
