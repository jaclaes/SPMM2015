package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.CoexistenceConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class CoexistenceConstraintDescriptor extends AbstractConstraintDescriptor {

	public CoexistenceConstraintDescriptor() {
		super("img/decserflow/coexistence.png", "Coexistence", EditorRegistry.DECSERFLOW_COEXISTENCE, new CoexistenceConstraint(DUMMY_A,
				DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = (SelectablePolylineConnection) super.createFigure(element);

		edge.setSourceDecoration(new CircleDecoration(CircleDecoration.SOURCE));
		edge.setTargetDecoration(new CircleDecoration(CircleDecoration.TARGET));

		return edge;
	}

}
