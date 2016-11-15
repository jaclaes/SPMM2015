/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic.editpart;

import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IMenuManager;

public interface IGenericEditPart {

	/**
	 * Build the context menu for this edit part.
	 * 
	 * @param menu
	 *            the menu to be filled
	 * @param location
	 *            the location where the request came from
	 */
	void buildContextMenu(IMenuManager menu, Point location);

	/**
	 *Find the <b>parent</b> edit part of given type.
	 * 
	 * @param target
	 *            the type
	 * @return the (indirect) parent of given type, <code>null</code> if there is no such parent
	 */
	IGenericEditPart getEditPart(Class<? extends IGenericEditPart> target);

	/**
	 * Return the edit part's figure.
	 * 
	 * @return the figure
	 */
	IFigure getFigure();

	/**
	 * Return the edit part's model.
	 * 
	 * @return the model
	 */
	IGenericModel getModel();

}
