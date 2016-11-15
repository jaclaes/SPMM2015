package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.InitConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.figure.NamedSingleActivityConstraint;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;

public class InitConstraintDescriptor extends SingleActivityConstraintDescriptor {

	public InitConstraintDescriptor() {
		super("img/decserflow/init.png", "Init", EditorRegistry.DECSERFLOW_INIT, new InitConstraint(DUMMY_A));
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		return new NamedSingleActivityConstraint((Edge) element, "init");
	}

}
