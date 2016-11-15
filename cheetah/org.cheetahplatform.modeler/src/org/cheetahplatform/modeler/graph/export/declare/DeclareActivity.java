package org.cheetahplatform.modeler.graph.export.declare;

import org.cheetahplatform.modeler.graph.model.Node;

public class DeclareActivity {

	private final Node node;

	public DeclareActivity(Node node) {
		this.node = node;
	}

	public long getId() {
		return node.getId();
	}

	public String getName() {
		return node.getNameNullSafe();
	}

}
