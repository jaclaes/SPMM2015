package org.cheetahplatform.modeler.graph.export.declare;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class DeclareGraphical {
	private List<DeclareCell> activities;

	private List<DeclareCell> constraints;

	public DeclareGraphical() {
		this.activities = new ArrayList<DeclareCell>();
		this.constraints = new ArrayList<DeclareCell>();
	}

	public void addActivity(Node node) {
		activities.add(new DeclareCell(node.getId(), node.getBounds()));
	}

	public void addConstraint(Edge edge) {
		// compute an arbitrary bend-point, otherwise the declare editor introduces arbitrary one
		Point source = edge.getSource().getBounds().getCenter();
		Point target = edge.getTarget().getBounds().getCenter();

		Point vector = target.getCopy().translate(source.getCopy().negate());
		source.translate(vector.scale(0.5));

		Rectangle edgeBounds = new Rectangle();
		edgeBounds.setSize(1, 1);
		edgeBounds.setLocation(source);

		constraints.add(new DeclareCell(edge.getId(), edgeBounds));
	}

	public List<DeclareCell> getActivities() {
		return activities;
	}

	public List<DeclareCell> getConstraints() {
		return constraints;
	}

}
