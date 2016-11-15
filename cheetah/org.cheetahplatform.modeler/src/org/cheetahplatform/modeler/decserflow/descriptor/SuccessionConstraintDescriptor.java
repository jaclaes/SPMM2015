package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.modeler.generic.figure.CircleDecoration.TARGET;

import org.cheetahplatform.core.declarative.constraint.SuccessionConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.FilledArrowCircleDecoration;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class SuccessionConstraintDescriptor extends AbstractConstraintDescriptor {

	public SuccessionConstraintDescriptor() {
		super("img/decserflow/succession.png", "Succession", EditorRegistry.DECSERFLOW_SUCCESSION, new SuccessionConstraint(DUMMY_A,
				DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = (SelectablePolylineConnection) super.createFigure(element);

		edge.setSourceDecoration(new CircleDecoration());
		edge.setTargetDecoration(new FilledArrowCircleDecoration(TARGET));

		return edge;
	}

}
