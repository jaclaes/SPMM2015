package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class FeedbackNode extends Node {

	public FeedbackNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
	}

	public FeedbackNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

}
