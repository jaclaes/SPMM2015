/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic;

import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.IMenuManager;

public class GenericMenuManager extends ContextMenuProvider {

	public GenericMenuManager(EditPartViewer viewer) {
		super(viewer);
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		EditPart target = getViewer().findObjectAt(GEFUtils.getDropLocation(getViewer()));

		if (!(target instanceof IGenericEditPart)) {
			return;
		}

		org.eclipse.draw2d.geometry.Point dropLocation = GEFUtils.getDropLocation(getViewer());
		((IGenericEditPart) target).buildContextMenu(menu, dropLocation);
	}

}
