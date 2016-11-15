package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.ResponseConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class ResponseConstraintDescriptor extends AbstractConstraintDescriptor {

	public ResponseConstraintDescriptor() {
		super("img/decserflow/response.png", "Response", EditorRegistry.DECSERFLOW_RESPONSE, new ResponseConstraint(DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = (SelectablePolylineConnection) super.createFigure(element);

		edge.setSourceDecoration(new CircleDecoration());
		FilledArrowFigure arrow = new FilledArrowFigure();
		arrow.setScale(8, 5);
		edge.setTargetDecoration(arrow);

		return edge;
	}

}
