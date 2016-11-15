package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.modeler.generic.figure.CircleDecoration.TARGET;

import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.FilledArrowCircleDecoration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class PrecedenceConstraintDescriptor extends AbstractConstraintDescriptor {

	public PrecedenceConstraintDescriptor() {
		super("img/decserflow/precedence.png", "Precedence", EditorRegistry.DECSERFLOW_PRECEDENCE, new PrecedenceConstraint(DUMMY_A,
				DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = (SelectablePolylineConnection) super.createFigure(element);
		edge.setTargetDecoration(new FilledArrowCircleDecoration(TARGET));

		return edge;
	}

}
