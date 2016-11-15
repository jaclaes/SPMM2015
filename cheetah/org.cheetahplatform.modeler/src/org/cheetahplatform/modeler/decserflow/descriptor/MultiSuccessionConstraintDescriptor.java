package org.cheetahplatform.modeler.decserflow.descriptor;

import java.util.Arrays;

import org.cheetahplatform.core.declarative.constraint.MultiActivityConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiSuccessionConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.FilledArrowCircleDecoration;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;

public class MultiSuccessionConstraintDescriptor extends MultiActivityConstraintDescriptor {

	public MultiSuccessionConstraintDescriptor() {
		super("img/decserflow/succession.png", "Multi Succession", EditorRegistry.DECSERFLOW_MULTI_SUCCESSION, 1, NO_MAXIMUM, 1,
				NO_MAXIMUM, "img/decserflow/multi_succession.png", new MultiSuccessionConstraint(Arrays.asList(new DeclarativeActivity[] {
						DUMMY_A, DUMMY_B }), Arrays.asList(new DeclarativeActivity[] { DUMMY_C, DUMMY_D })));
	}

	@Override
	public MultiActivityConstraint createConstraint() {
		return new MultiSuccessionConstraint();
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
		connection.setTargetDecoration(new FilledArrowCircleDecoration(CircleDecoration.TARGET));

		return connection;
	}

}
