package org.cheetahplatform.modeler.graph.export.declare;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class DeclareAssignment {
	private List<DeclareActivity> activityDefinitions;
	private List<DeclareConstraint> constraintDefinitions;
	private DeclareGraphical graphical;

	public DeclareAssignment() {
		this.activityDefinitions = new ArrayList<DeclareActivity>();
		this.constraintDefinitions = new ArrayList<DeclareConstraint>();
		this.graphical = new DeclareGraphical();
	}

	public void addActivity(Node node) {
		DeclareActivity activity = new DeclareActivity(node);
		activityDefinitions.add(activity);
		graphical.addActivity(node);
	}

	public void addConstraint(Edge edge) {
		constraintDefinitions.add(DeclareConstraint.createConstraint(edge));
		String id = edge.getDescriptor().getId();
		if (id.equals(EditorRegistry.DECSERFLOW_SELECTION) || id.equals(EditorRegistry.DECSERFLOW_INIT)) {
			return;// no graphical information required
		}

		graphical.addConstraint(edge);
	}

	public List<DeclareActivity> getActivityDefinitions() {
		return activityDefinitions;
	}

	public List<DeclareConstraint> getConstraintDefinitions() {
		return constraintDefinitions;
	}

	public DeclareGraphical getGraphical() {
		return graphical;
	}
}
