package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.RespondedExistenceConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class RespondedExistenceConstraintDescriptor extends AbstractConstraintDescriptor {

	public RespondedExistenceConstraintDescriptor() {
		super("img/decserflow/responded_existence.png", "Responded Existence", EditorRegistry.DECSERFLOW_RESPONDED_EXISTENCE,
				new RespondedExistenceConstraint(DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = (SelectablePolylineConnection) super.createFigure(element);

		edge.setSourceDecoration(new CircleDecoration(CircleDecoration.SOURCE));
		edge.setTargetDecoration(null);

		return edge;
	}
}
