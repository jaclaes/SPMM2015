package org.cheetahplatform.modeler.decserflow.descriptor;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.decserflow.ShowConstraintDescriptionAction;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IMenuManager;

public abstract class AbstractConstraintDescriptor extends EdgeDescriptor {

	private final IDeclarativeConstraint constraint;

	protected static final DeclarativeActivity DUMMY_A = new DeclarativeActivity("A");
	protected static final DeclarativeActivity DUMMY_B = new DeclarativeActivity("B");
	protected static final DeclarativeActivity DUMMY_C = new DeclarativeActivity("C");
	protected static final DeclarativeActivity DUMMY_D = new DeclarativeActivity("D");

	protected AbstractConstraintDescriptor(String imagePath, String name, String id, IDeclarativeConstraint constraint) {
		super(imagePath, name, id);

		this.constraint = constraint;
	}

	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation) {
		menu.add(new ShowConstraintDescriptionAction((Edge) editPart.getModel()));
	}

	public IDeclarativeConstraint getConstraint() {
		return constraint;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

}
