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
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.action.IMenuManager;

public abstract class GenericEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, IGenericEditPart {

	protected GenericEditPart(IGenericModel model) {
		setModel(model);
		model.addPropertyChangeListener(this);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		// no menu by default
	}

	@Override
	protected void createEditPolicies() {
		// no edit policies
	}

	@Override
	public void deactivate() {
		super.deactivate();

		(getModel()).removePropertyChangeListener(this);
	}

	@Override
	public Object getAdapter(Class key) {
		// override, as the platform may not be running (causing
		// IllegalStateException)
		if (!Platform.isRunning()) {
			return null;
		}

		return super.getAdapter(key);
	}

	@Override
	public GenericEditPart getEditPart(Class<? extends IGenericEditPart> target) {
		EditPart current = this;

		while (current != null && !current.getClass().equals(target)) {
			current = current.getParent();
		}

		return (GenericEditPart) current;
	}

	@Override
	public IGenericModel getModel() {
		return (IGenericModel) super.getModel();
	}

	@Override
	public List getModelChildren() {
		return getModel().getChildren();
	}

	@Override
	protected List getModelSourceConnections() {
		return getModel().getSourceConnections();
	}

	@Override
	protected List getModelTargetConnections() {
		return getModel().getTargetConnections();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// do not react to changes by default
	}

	public void refresh(boolean recursive) {
		refresh();

		if (recursive) {
			for (Object child : getChildren()) {
				((GenericEditPart) child).refresh(recursive);
			}

			for (Object connection : getSourceConnections()) {
				((GenericConnectionEditPart) connection).refresh(recursive);
			}

			for (Object connection : getTargetConnections()) {
				((GenericConnectionEditPart) connection).refresh(recursive);
			}
		}
	}

}
