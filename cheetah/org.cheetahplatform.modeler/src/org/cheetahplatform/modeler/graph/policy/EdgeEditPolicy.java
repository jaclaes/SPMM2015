package org.cheetahplatform.modeler.graph.policy;

import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class EdgeEditPolicy extends ConnectionEditPolicy {
	@Override
	public Command getCommand(Request request) {
		return ((Edge) getHost().getModel()).getDescriptor().getCommand(getHost(), request);
	}

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		return null;
	}

}
