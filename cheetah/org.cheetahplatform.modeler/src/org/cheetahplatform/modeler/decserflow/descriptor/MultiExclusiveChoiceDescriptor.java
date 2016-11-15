package org.cheetahplatform.modeler.decserflow.descriptor;

import java.util.Arrays;

import org.cheetahplatform.core.declarative.constraint.MultiActivityConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiExclusiveChoiceConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.ShowConstraintDescriptionAction;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IMenuManager;

public class MultiExclusiveChoiceDescriptor extends MultiActivityConstraintDescriptor {

	public MultiExclusiveChoiceDescriptor() {
		super("img/decserflow/multi_exclusive_choice.png", "Exclusive Choice", EditorRegistry.DECSERFLOW_MULTI_EXCLUSIVE_CHOICE, 2,
				NO_MAXIMUM, 0, 0, "img/decserflow/multi_exclusive_choice_large.png", new MultiExclusiveChoiceConstraint(
						Arrays.asList(new DeclarativeActivity[] { DUMMY_A, DUMMY_B, DUMMY_C }), 1));
	}

	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation) {
		menu.add(new ShowConstraintDescriptionAction((Edge) editPart.getModel()));
	}

	@Override
	public MultiActivityConstraint createConstraint() {
		return new MultiExclusiveChoiceConstraint();
	}

	@Override
	public INodeDescriptor getAuxiliaryNodeDescriptor() {
		return (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_AUXILIARY_NODE_FOR_MULTI_EXCLUSIVE_CHOICE);
	}

}
