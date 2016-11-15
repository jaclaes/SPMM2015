package org.cheetahplatform.modeler.decserflow.figure;

import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.graph.model.Edge;

public class SelectionConstraintFigure extends AbstractSingleActivityConstraint {

	public SelectionConstraintFigure(Edge edge) {
		super(edge);
	}

	@Override
	protected String getText() {
		SelectionConstraintEdge constraint = (SelectionConstraintEdge) edge;
		int min = constraint.getMinimum();
		int max = constraint.getMaximum();

		String text = min + " ... " + max;
		if (min == max) {
			text = String.valueOf(min);
		} else if (!constraint.hasMinimum() && !constraint.hasMaximum()) {
			text = "* ... *";
		} else if (!constraint.hasMaximum()) {
			text = min + " ... *";
		} else if (!constraint.hasMinimum()) {
			text = "0 ... " + max;
		}

		return text;
	}

}
