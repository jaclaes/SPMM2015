package org.cheetahplatform.modeler.decserflow.descriptor;

import java.util.Arrays;

import org.cheetahplatform.core.declarative.constraint.MultiActivityConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiResponseConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;

public class MultiResponseConstraintDescriptor extends MultiActivityConstraintDescriptor {

	public MultiResponseConstraintDescriptor() {
		super("img/decserflow/response.png", "Multi Response", EditorRegistry.DECSERFLOW_MULTI_RESPONSE, 1, 1, 2, NO_MAXIMUM,
				"img/decserflow/multi_response.png", new MultiResponseConstraint(DUMMY_C, Arrays.asList(new DeclarativeActivity[] {
						DUMMY_A, DUMMY_B })));
	}

	@Override
	public MultiActivityConstraint createConstraint() {
		return new MultiResponseConstraint();
	}

	@Override
	protected SelectablePolylineConnection createIncomingConnection(Edge edge) {
		SelectablePolylineConnection connection = super.createIncomingConnection(edge);
		connection.setSourceDecoration(new CircleDecoration(CircleDecoration.SOURCE));

		return connection;
	}

	@Override
	protected SelectablePolylineConnection createOutgoingConnection(Edge edge) {
		SelectablePolylineConnection connection = super.createOutgoingConnection(edge);

		FilledArrowFigure arrow = new FilledArrowFigure();
		arrow.setScale(10, 6);
		connection.setTargetDecoration(arrow);

		return connection;
	}
}
