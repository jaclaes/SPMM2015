/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.modeler.generic;

import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * {@link EditPartFactory} which handles {@link IGenericModel} objects only.
 * 
 * @author Stefan Zugal
 */
public class GenericEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		return ((IGenericModel) model).createEditPart(context);
	}

}
