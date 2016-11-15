package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public abstract class ModelingNode extends Node {

	private Graph initialGraph;

	public ModelingNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
	}

	public ModelingNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public Graph getInitialGraph() {
		return initialGraph;
	}

	public void setInitialGraph(Graph initialGraph) {
		this.initialGraph = initialGraph;
	}
}
