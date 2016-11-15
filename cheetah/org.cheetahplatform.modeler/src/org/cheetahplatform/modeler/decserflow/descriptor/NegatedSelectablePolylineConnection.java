package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;

public class NegatedSelectablePolylineConnection extends SelectablePolylineConnection {

	public NegatedSelectablePolylineConnection(Edge edge) {
		super(edge);

		setAntialias(SWT.ON);
	}

	@Override
	protected void outlineShape(Graphics g) {
		super.outlineShape(g);
		g.setAntialias(SWT.ON);
		g.setBackgroundColor(getBackgroundColor());

		PointList points = getPoints();
		int size = points.size();
		Point first = points.getPoint(size / 2 - 1).getCopy();
		Point second = points.getPoint(size / 2).getCopy();
		Point vector = second.getCopy().translate(first.getCopy().negate());

		Point orthogonal = new Point(-vector.y, vector.x);
		double orthogonalLength = orthogonal.getDistance(new Point());
		double length = 15;
		orthogonal.scale(length / orthogonalLength);

		double basicFactor = 0.5;
		double spacing = 3;
		double vectorLength = vector.getDistance(new Point());
		double additionalSpacing = spacing / vectorLength;

		// first line
		double line1Spacing = basicFactor - additionalSpacing;
		Point line1Start = first.getCopy().translate(vector.getCopy().scale(line1Spacing));
		line1Start = line1Start.translate(orthogonal.getCopy().scale(0.5));
		Point line1End = line1Start.getCopy().translate(orthogonal.getCopy().negate());
		g.drawLine(line1Start.x, line1Start.y, line1End.x, line1End.y);

		// second line
		double line2Spacing = basicFactor + additionalSpacing;
		Point line2Start = first.getCopy().translate(vector.getCopy().scale(line2Spacing));
		line2Start = line2Start.translate(orthogonal.getCopy().scale(0.5));
		Point line2End = line2Start.getCopy().translate(orthogonal.getCopy().negate());
		g.drawLine(line2Start.x, line2Start.y, line2End.x, line2End.y);
	}
}
