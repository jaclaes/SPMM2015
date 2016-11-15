package org.cheetahplatform.modeler.experiment.editor.model;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

public class BPMNNode extends ModelingNode {

	private boolean isLayoutAvailable;
	
	public BPMNNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
		isLayoutAvailable = false;
	}
	
	public BPMNNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public boolean isLayoutAvailable() {
		return isLayoutAvailable;
	}

	public void setLayoutAvailable(boolean isLayoutAvailable) {
		this.isLayoutAvailable = isLayoutAvailable;
	}
}
