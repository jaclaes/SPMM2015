package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public class SingleNodeEdge extends Edge {
	public SingleNodeEdge(IGenericModel parent, IGraphElementDescriptor descriptor) {
		super(parent, descriptor);
	}

	public SingleNodeEdge(IGenericModel parent, IGraphElementDescriptor descriptor, long id) {
		super(parent, descriptor, id);
	}

	@Override
	public Node getTarget() {
		return super.getSource();
	}

	@Override
	public void setTarget(Node target) {
		super.setSource(target);
	}
}
