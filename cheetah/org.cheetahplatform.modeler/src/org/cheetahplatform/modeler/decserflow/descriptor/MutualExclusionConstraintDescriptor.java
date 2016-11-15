package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.MutualExclusionConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class MutualExclusionConstraintDescriptor extends AbstractConstraintDescriptor {

	public MutualExclusionConstraintDescriptor() {
		super("img/decserflow/mutual_exclusion.png", "Not Co-Existence", EditorRegistry.DECSERFLOW_MUTUAL_EXCLUSION,
				new MutualExclusionConstraint(DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = new NegatedSelectablePolylineConnection((Edge) element);
		edge.setSourceDecoration(new CircleDecoration(CircleDecoration.SOURCE));
		edge.setTargetDecoration(new CircleDecoration(CircleDecoration.TARGET));

		return edge;
	}
}
