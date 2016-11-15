package org.cheetahplatform.modeler.decserflow.descriptor;

import java.util.Arrays;

import org.cheetahplatform.core.declarative.constraint.MultiActivityConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiPrecedenceConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.FilledArrowCircleDecoration;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;

public class MultiPrecedenceConstraintDescriptor extends MultiActivityConstraintDescriptor {

	public MultiPrecedenceConstraintDescriptor() {
		super("img/decserflow/precedence.png", "Multi Precedence", EditorRegistry.DECSERFLOW_MULTI_PRECENDENCE, 2, NO_MAXIMUM, 1, 1,
				"img/decserflow/multi_precedence.png", new MultiPrecedenceConstraint(Arrays.asList(new DeclarativeActivity[] { DUMMY_A,
						DUMMY_B }), DUMMY_C));
	}

	@Override
	public MultiActivityConstraint createConstraint() {
		return new MultiPrecedenceConstraint();
	}

	@Override
	protected SelectablePolylineConnection createOutgoingConnection(Edge edge) {
		SelectablePolylineConnection connection = super.createOutgoingConnection(edge);
		connection.setTargetDecoration(new FilledArrowCircleDecoration(CircleDecoration.TARGET));

		return connection;
	}

}
