package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.ChainedResponseConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.SelectableTriplePolyLineConnection;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class ChainedResponseConstraintDescriptor extends AbstractConstraintDescriptor {

	public ChainedResponseConstraintDescriptor() {
		super("img/decserflow/response.png", "Chained Response", EditorRegistry.DECSERFLOW_CHAINED_RESPONSE, new ChainedResponseConstraint(
				DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectableTriplePolyLineConnection connection = new SelectableTriplePolyLineConnection((Edge) element, 8);

		connection.setSourceDecoration(new CircleDecoration());
		FilledArrowFigure arrow = new FilledArrowFigure();
		arrow.setScale(10, 6);
		connection.setTargetDecoration(arrow);

		return connection;
	}

}
