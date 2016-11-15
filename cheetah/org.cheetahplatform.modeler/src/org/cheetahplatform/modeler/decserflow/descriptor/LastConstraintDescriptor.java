package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.LastConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.NamedSingleActivityConstraint;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class LastConstraintDescriptor extends SingleActivityConstraintDescriptor {

	public LastConstraintDescriptor() {
		super("img/decserflow/last.png", "Last", EditorRegistry.DECSERFLOW_LAST, new LastConstraint(DUMMY_B));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		return new NamedSingleActivityConstraint((Edge) element, "last");
	}

}
