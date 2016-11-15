package org.cheetahplatform.modeler.decserflow.figure;

import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class SelectableTriplePolyLineConnection extends SelectablePolylineConnection {

	private int targetOffset;

	public SelectableTriplePolyLineConnection(Edge edge, int targetOffet) {
		super(edge);

		targetOffset = targetOffet;
		setAntialias(SWT.ON);
	}

	@Override
	protected void outlineShape(Graphics g) {
		int scale = 3;
		Color backgroundColor = getBackgroundColor();
		if (backgroundColor.getRGB().equals(new RGB(240, 240, 240))) {
			backgroundColor = SWTResourceManager.getColor(0, 0, 0);
		}
		g.setBackgroundColor(getBackgroundColor());
		PointList list = getPoints();

		for (int i = -1; i <= 1; i++) {
			PointList current = new PointList();

			for (int j = 0; j < list.size(); j++) {
				Point point = list.getPoint(j).getCopy();

				if (j == 0 || j == list.size() - 1) {
					PrecisionPoint currentVector = null;

					if (j == 0) {
						currentVector = new PrecisionPoint(list.getPoint(j + 1).getCopy().translate(point.getCopy().negate()));
					} else {
						currentVector = new PrecisionPoint(list.getPoint(j - 1).getCopy().negate().translate(point.getCopy()));
						double scaleOffset = -1 / currentVector.getDistance(new Point(0, 0)) * targetOffset;
						Point scaled = currentVector.getCopy();
						scaled.performScale(scaleOffset);
						point.translate(scaled);
					}

					currentVector = new PrecisionPoint(currentVector.y, -currentVector.x);
					currentVector.performScale((i * (scale - 1)) / currentVector.getDistance(new Point(0, 0)));
					point.translate((int) Math.round(currentVector.preciseX), (int) Math.round(currentVector.preciseY));
					current.addPoint(point);
				} else {
					Point previous = list.getPoint(j - 1);
					Point next = list.getPoint(j + 1);
					Point fromPrevious = point.getCopy().translate(previous.getCopy().negate());
					Point toNext = next.getCopy().translate(point.getCopy().negate());

					double fromPreviousLength = fromPrevious.getDistance(new Point(0, 0));
					double toNextLength = toNext.getDistance(new Point(0, 0));
					Point halfAnglePoint = point.getCopy().translate(toNext.getCopy().scale(1 / toNextLength * fromPreviousLength));
					halfAnglePoint.translate(fromPrevious.getCopy().negate());
					PrecisionPoint vector = new PrecisionPoint(halfAnglePoint.negate().translate(point));

					// compute the cross product to avoid crossing lines
					double x1 = fromPrevious.x;
					double y1 = fromPrevious.y;
					double x2 = toNext.x;
					double y2 = toNext.y;

					double crossProduct = x1 * y2 - x2 * y1;
					int multiplicator = 1;
					if (crossProduct < 0) {
						multiplicator = -1;
					}

					vector.performScale((multiplicator * i * scale) / vector.getDistance(new Point(0, 0)));
					point.translate((int) Math.round(vector.preciseX), (int) Math.round(vector.preciseY));
					current.addPoint(point);
				}
			}

			g.drawPolyline(current);
		}
	}

}
