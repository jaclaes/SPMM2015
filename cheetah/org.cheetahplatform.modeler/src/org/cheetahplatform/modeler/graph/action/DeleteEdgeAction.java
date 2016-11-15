package org.cheetahplatform.modeler.graph.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class DeleteEdgeAction extends Action {

	private final EdgeEditPart edgeEditPart;

	public DeleteEdgeAction(EdgeEditPart edgeEditPart) {
		Assert.isNotNull(edgeEditPart);
		setText(Messages.DeleteEdgeAction_0);
		setToolTipText(Messages.DeleteEdgeAction_1);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/delete.gif")); //$NON-NLS-1$

		this.edgeEditPart = edgeEditPart;
	}

	@Override
	public void run() {
		Command command = edgeEditPart.getModel().getDescriptor().getCommand(edgeEditPart, new Request(RequestConstants.REQ_DELETE));
		edgeEditPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}

}