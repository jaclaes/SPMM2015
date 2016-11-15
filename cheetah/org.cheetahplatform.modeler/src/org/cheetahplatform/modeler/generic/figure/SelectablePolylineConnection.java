package org.cheetahplatform.modeler.generic.figure;

import java.util.Map;
import java.util.WeakHashMap;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class SelectablePolylineConnection extends PolylineConnection implements IGraphElementFigure {

	public static class PointPair {
		private final Point source;
		private final Point target;

		protected PointPair(Point source, Point target) {
			this.source = source;
			this.target = target;
		}

		public Point getSource() {
			return source;
		}

		public Point getTarget() {
			return target;
		}

	}

	private static final Map<Edge, PointPair> EDGE_TO_POINTS = new WeakHashMap<Edge, PointPair>();

	public static void clearPoints() {
		EDGE_TO_POINTS.clear();
	}

	public static PointPair getPoints(Edge edge) {
		PointPair pair = EDGE_TO_POINTS.get(edge);
		if (pair == null) {
			return new PointPair(new Point(), new Point());
		}

		return pair;
	}

	protected Edge edge;

	public SelectablePolylineConnection(Edge edge) {
		this.edge = edge;
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds2 = super.getBounds();
		bounds.union(0, 0, 20000, 20000);

		return bounds2;
	}

	@Override
	protected void outlineShape(Graphics g) {
		g.setBackgroundColor(getBackgroundColor());

		super.outlineShape(g);
	}

	@Override
	public void setBackgroundColor(Color bg) {
		if (getSourceDecoration() != null) {
			getSourceDecoration().setBackgroundColor(bg);
		}
		if (getTargetDecoration() != null) {
			getTargetDecoration().setBackgroundColor(bg);
		}

		super.setBackgroundColor(bg);

	}

	@Override
	public void setName(String name) {
		// ignore
	}

	@Override
	public void setPoint(Point pt, int index) {
		super.setPoint(pt, index);
		updatePoints();
	}

	@Override
	public void setPoints(PointList points) {
		super.setPoints(points);
		updatePoints();
	}

	@Override
	public void setSelected(boolean state) {
		// ignore
	}

	private void updatePoints() {
		Point source = new Point();
		Point target = new Point();
		PointList points = getPoints();

		if (points.size() != 0) {
			source = points.getFirstPoint();
			target = points.getLastPoint();
		}

		EDGE_TO_POINTS.put(edge, new PointPair(source, target));
	}

}
