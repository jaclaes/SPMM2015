package org.cheetahplatform.modeler.graph;

import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.requests.CreationFactory;

public class GraphElementCreationFactory implements CreationFactory {

	private final IGraphElementDescriptor descriptor;
	private final Graph graph;

	public GraphElementCreationFactory(IGraphElementDescriptor descriptor, Graph graph) {
		this.descriptor = descriptor;
		this.graph = graph;
	}

	@Override
	public Object getNewObject() {
		return descriptor.createModel(graph);
	}

	@Override
	public Object getObjectType() {
		return descriptor;
	}

}