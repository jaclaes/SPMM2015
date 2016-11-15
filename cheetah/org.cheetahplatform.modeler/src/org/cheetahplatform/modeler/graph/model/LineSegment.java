package org.cheetahplatform.modeler.graph.model;

import org.eclipse.draw2d.geometry.Point;

public class LineSegment {
	private Point start;
	private Point end;

	public LineSegment(Point start, Point end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Point getEnd() {
		return end;
	}

	public Point getStart() {
		return start;
	}
}
