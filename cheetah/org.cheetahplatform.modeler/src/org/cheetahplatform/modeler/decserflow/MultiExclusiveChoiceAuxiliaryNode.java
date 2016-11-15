package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class MultiExclusiveChoiceAuxiliaryNode extends Node {
	private int minimum;

	public MultiExclusiveChoiceAuxiliaryNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
	}

	public MultiExclusiveChoiceAuxiliaryNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
		firePropertyChanged(CreateMultiExclusiveChoiceAuxiliaryNodeCommand.MINIMUM);
	}

}
