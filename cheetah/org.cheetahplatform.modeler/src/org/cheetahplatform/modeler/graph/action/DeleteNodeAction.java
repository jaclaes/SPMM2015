package org.cheetahplatform.modeler.graph.action;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

public class DeleteNodeAction extends Action {
	private final NodeEditPart nodeEditPart;

	public DeleteNodeAction(NodeEditPart nodeEditPart) {
		this.nodeEditPart = nodeEditPart;
		setText(Messages.DeleteNodeAction_0);
		setToolTipText(Messages.DeleteNodeAction_1);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/delete.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		Command command = NodeDescriptor.getDeleteCommand(nodeEditPart);
		nodeEditPart.getViewer().getEditDomain().getCommandStack().execute(command);
	}
}
