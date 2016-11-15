/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic.model;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPart;

/**
 * Interface for GEF models.
 * 
 * @author Stefan Zugal
 */
public interface IGenericModel {

	/**
	 * Add a property change listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Create the edit part for this model.
	 * 
	 * @param context
	 *            the current context
	 * @return the corresponding edit part for this model
	 */
	EditPart createEditPart(EditPart context);

	/**
	 * The given property has changed - fire the corresponding event.
	 * 
	 * @param property
	 *            the property
	 */
	void firePropertyChanged(String property);

	/**
	 * Return the model's children.
	 * 
	 * @return the children
	 */
	List<? extends Object> getChildren();

	/**
	 * Returns the model's parent object.
	 * 
	 * @return the parent
	 */
	IGenericModel getParent();

	/**
	 * Return the model's source connections.
	 * 
	 * @return the source connections
	 */
	List<? extends Object> getSourceConnections();

	/**
	 * Return the model's target connections.
	 * 
	 * @return the target connections
	 */
	List<? extends Object> getTargetConnections();

	/**
	 * Remove the given listener.
	 * @param listener the listener to be removed
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);
}
