package org.cheetahplatform.modeler.experiment.editor.model;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

public class DecSerFlowNode extends ModelingNode {

	private Set<String> constraints;
	
	public DecSerFlowNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
		constraints = new HashSet<String>();
	}
	
	public DecSerFlowNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public Set<String> getConstraints() {
		return constraints;
	}

	public void setConstraints(Set<String> constraints) {
		this.constraints = constraints;
	}
}
