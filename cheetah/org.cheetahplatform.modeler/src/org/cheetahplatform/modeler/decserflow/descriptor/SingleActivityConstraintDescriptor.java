package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.modeler.decserflow.ReconnectSingleActivityConstraintEdgeCommand;
import org.cheetahplatform.modeler.decserflow.SingleNodeEdge;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.graph.policy.EdgeEditPolicy;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

public abstract class SingleActivityConstraintDescriptor extends AbstractConstraintDescriptor {
	protected SingleActivityConstraintDescriptor(String imagePath, String name, String id, IDeclarativeConstraint constraint) {
		super(imagePath, name, id, constraint);
	}

	@Override
	public void createEditPolicies(EditPart editPart) {
		editPart.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		editPart.installEditPolicy(EditPolicy.CONNECTION_ROLE, new EdgeEditPolicy());
	}

	@Override
	public Edge createModel(Graph graph) {
		return new SingleNodeEdge(graph, this);
	}

	@Override
	public Edge createModel(Graph graph, long id, AuditTrailEntry entry) {
		return new SingleNodeEdge(graph, this, id);
	}

	@Override
	public AbstractGraphCommand createReconnectEdgeCommand(Edge edge, Node source, Node target) {
		return new ReconnectSingleActivityConstraintEdgeCommand(edge, source, target);
	}
}
