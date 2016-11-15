package org.cheetahplatform.modeler.graph.command;

import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;

public abstract class AbstractReconnectEdgeCommand extends EdgeCommand {
	protected final Node source;
	protected final Node target;
	protected Node oldSource;
	protected Node oldTarget;

	public AbstractReconnectEdgeCommand(Edge edge, Node source, Node target) {
		super(edge.getGraph(), edge);

		this.source = source;
		this.target = target;
	}

	@Override
	public void undo() {
		IEdgeDescriptor descriptor = (IEdgeDescriptor) element.getDescriptor();
		AbstractGraphCommand command = descriptor.createReconnectEdgeCommand(getEdge(), oldSource, oldTarget);
		command.execute();
	}
}
