/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.jface.action.IMenuManager;

public abstract class GenericConnectionEditPart extends AbstractConnectionEditPart implements IGenericEditPart, PropertyChangeListener {

	protected GenericConnectionEditPart(IGenericModel model) {
		setModel(model);
		model.addPropertyChangeListener(this);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		// no menu by default
	}

	@Override
	protected void createEditPolicies() {
		// no edit policies by default
	}

	@Override
	public IGenericEditPart getEditPart(Class<? extends IGenericEditPart> target) {
		EditPart current = getSource();
		if (current == null) {
			current = getTarget();
		}

		while (current != null && !current.getClass().equals(target)) {
			current = current.getParent();
		}

		return (IGenericEditPart) current;
	}

	@Override
	public IGenericModel getModel() {
		return (IGenericModel) super.getModel();
	}

	@Override
	protected List getModelChildren() {
		return getModel().getChildren();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// ignore
	}

	public void refresh(boolean recursive) {
		refresh();

		if (recursive) {
			for (Object child : getChildren()) {
				((GenericEditPart) child).refresh(recursive);
			}
		}
	}

}
