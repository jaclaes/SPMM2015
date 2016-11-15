/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyHeaderEditPart;
import org.eclipse.gef.EditPart;

public class WeeklyHeader extends GenericModel {

	public WeeklyHeader(IGenericModel parent) {
		super(parent);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WeeklyHeaderEditPart(this);
	}

	public Date getStartDate() {
		return ((MultiWeek) getParent()).getSlot().getStart();
	}

}
