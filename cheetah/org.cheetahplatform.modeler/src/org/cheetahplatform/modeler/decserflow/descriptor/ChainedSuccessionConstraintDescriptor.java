package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.cheetahplatform.modeler.generic.figure.CircleDecoration.TARGET;

import org.cheetahplatform.core.declarative.constraint.ChainedSuccessionConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.FilledArrowCircleDecoration;
import org.cheetahplatform.modeler.decserflow.figure.SelectableTriplePolyLineConnection;
import org.cheetahplatform.modeler.generic.figure.CircleDecoration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class ChainedSuccessionConstraintDescriptor extends AbstractConstraintDescriptor {

	public ChainedSuccessionConstraintDescriptor() {
		super("img/decserflow/succession.png", "Chained Succession", EditorRegistry.DECSERFLOW_CHAINED_SUCCESSION,
				new ChainedSuccessionConstraint(DUMMY_A, DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectableTriplePolyLineConnection connection = new SelectableTriplePolyLineConnection((Edge) element, 15);

		connection.setSourceDecoration(new CircleDecoration());
		connection.setTargetDecoration(new FilledArrowCircleDecoration(TARGET));

		return connection;
	}

}
