package org.cheetahplatform.modeler.decserflow.figure;

import org.cheetahplatform.modeler.graph.model.Edge;

public class NamedSingleActivityConstraint extends AbstractSingleActivityConstraint {

	private final String name;

	public NamedSingleActivityConstraint(Edge edge, String name) {
		super(edge);

		this.name = name;
	}

	@Override
	protected String getText() {
		return name;
	}

}
