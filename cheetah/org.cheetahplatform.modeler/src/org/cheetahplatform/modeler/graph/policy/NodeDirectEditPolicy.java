package org.cheetahplatform.modeler.graph.policy;

import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

public class NodeDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		return new RenameCommand((Node) getHost().getModel(), (String) request.getCellEditor().getValue());
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		// ignore
	}

}
