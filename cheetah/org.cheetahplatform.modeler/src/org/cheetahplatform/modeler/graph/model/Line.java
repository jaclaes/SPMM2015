package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.LineAttributes;

public class Line {
	private List<LineSegment> segments;
	private LineAttributes lineAttributes;
	private Color color;

	private String label;

	public Line() {
		segments = new ArrayList<LineSegment>();
	}

	public void addSegment(LineSegment segment) {
		segments.add(segment);
	}

	public Color getColor() {
		return color;
	}

	public String getLabel() {
		return label;
	}

	public LineAttributes getLineAttributes() {
		return lineAttributes;
	}

	public List<LineSegment> getSegments() {
		return Collections.unmodifiableList(segments);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLineAttributes(LineAttributes lineAttributes) {
		this.lineAttributes = lineAttributes;
	}
}
