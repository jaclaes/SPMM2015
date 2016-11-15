package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.NegationResponseConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class NegationResponseConstraintDescriptor extends AbstractConstraintDescriptor {
	public NegationResponseConstraintDescriptor() {
		super("img/decserflow/negation_response.png", "Negation Response", EditorRegistry.DECSERFLOW_NEGATION_RESPONSE,
				new NegationResponseConstraint(DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection edge = new NegatedSelectablePolylineConnection((Edge) element);

		edge.setSourceDecoration(new CircleDecoration());
		FilledArrowFigure arrow = new FilledArrowFigure();
		arrow.setScale(8, 5);
		edge.setTargetDecoration(arrow);

		return edge;
	}

}
