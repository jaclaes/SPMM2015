package org.cheetahplatform.modeler.graph.export.declare;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class DeclareModel {
	private DeclareAssignment assignment;

	public DeclareModel() {
		this.assignment = new DeclareAssignment();
	}

	public void addActivity(Node node) {
		assignment.addActivity(node);
	}

	public void addConstraint(Edge edge) {
		assignment.addConstraint(edge);
	}

	public DeclareAssignment getAssignment() {
		return assignment;
	}
}
