package org.cheetahplatform.modeler.graph.policy;

import org.cheetahplatform.modeler.graph.command.DummyCommand;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class NodeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Connection createDummyConnection(Request req) {
		Edge edge = (Edge) ((CreateConnectionRequest) req).getNewObject();
		return (Connection) edge.getDescriptor().createFigure(edge);
	}

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Node source = (Node) request.getSourceEditPart().getModel();
		Node target = (Node) request.getTargetEditPart().getModel();
		Edge edge = (Edge) request.getNewObject();
		IEdgeDescriptor descriptor = edge.getDescriptor();

		if (source.getGraph().existsConnection(source, target, descriptor)) {
			return null; // avoid duplicates
		}
		if (source.equals(target)) {
			return null;
		}

		return descriptor.createCreateEdgeCommand(source.getGraph(), edge, source, target, null);
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		return new DummyCommand(); // allow all connections by default
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Edge edge = (Edge) request.getConnectionEditPart().getModel();
		Node source = (Node) request.getTarget().getModel();
		Node target = null;
		if (request.getConnectionEditPart().getTarget() != null) {
			target = (Node) request.getConnectionEditPart().getTarget().getModel();
		}

		return handleReconnectCommand(edge, source, target);
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Edge edge = (Edge) request.getConnectionEditPart().getModel();
		Node source = null;
		if (request.getConnectionEditPart().getSource() != null) {
			source = (Node) request.getConnectionEditPart().getSource().getModel();
		}
		Node target = (Node) request.getTarget().getModel();

		return handleReconnectCommand(edge, source, target);
	}

	private Command handleReconnectCommand(Edge edge, Node source, Node target) {
		if (source == null && target == null) {
			return null;
		}
		if (source != null && source.equals(target)) {
			return null;
		}

		return edge.getDescriptor().createReconnectEdgeCommand(edge, source, target);
	}

}
