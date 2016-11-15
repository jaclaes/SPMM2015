package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.modeler.generic.figure.CircleDecoration.TARGET;

import org.cheetahplatform.core.declarative.constraint.ChainedPrecedenceConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.FilledArrowCircleDecoration;
import org.cheetahplatform.modeler.decserflow.figure.SelectableTriplePolyLineConnection;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class ChainedPrecedenceConstraintDescriptor extends AbstractConstraintDescriptor {

	public ChainedPrecedenceConstraintDescriptor() {
		super("img/decserflow/precedence.png", "Chained Prececdence", EditorRegistry.DECSERFLOW_CHAINED_PRECEDENCE,
				new ChainedPrecedenceConstraint(DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectableTriplePolyLineConnection connection = new SelectableTriplePolyLineConnection((Edge) element, 15);
		connection.setTargetDecoration(new FilledArrowCircleDecoration(TARGET));

		return connection;
	}

}
