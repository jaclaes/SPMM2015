package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.DeleteMultiActivityConstraintCommand;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;

public class DeleteMultiExclusionConstraintCommand extends DeleteMultiActivityConstraintCommand {

	private int minimum;

	public DeleteMultiExclusionConstraintCommand(Edge toDelete) {
		super(toDelete, (MultiActivityConstraintDescriptor) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_MULTI_EXCLUSIVE_CHOICE));

		MultiExclusiveChoiceAuxiliaryNode auxiliaryNode;
		if (toDelete.getSource().getDescriptor() instanceof AuxiliaryNodeDescriptor) {
			auxiliaryNode = (MultiExclusiveChoiceAuxiliaryNode) toDelete.getSource();
		} else {
			auxiliaryNode = (MultiExclusiveChoiceAuxiliaryNode) toDelete.getTarget();
		}

		minimum = auxiliaryNode.getMinimum();
	}

	@Override
	public void undo() {
		new CreateMultiExclusionConstraintCommand(toDelete.getGraph(), nodes, incomingEdges, outgoingEdges, auxiliaryNode, descriptor,
				minimum).execute();
	}
}
