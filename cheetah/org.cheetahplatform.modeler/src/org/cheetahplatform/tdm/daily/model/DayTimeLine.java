/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.tdm.daily.editpart.DayTimeLineEditPart;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.eclipse.gef.EditPart;

public class DayTimeLine extends GenericModel {
	private int startHour;

	protected DayTimeLine(Day day) {
		super(day);

		startHour = DayTimeLineFigure.START_HOUR;
	}

	
	@Override
	public EditPart createEditPart(EditPart context) {
		return new DayTimeLineEditPart(this);
	}

	/**
	 * Return the startHour.
	 * 
	 * @return the startHour
	 */
	public int getStartHour() {
		return startHour;
	}

}
