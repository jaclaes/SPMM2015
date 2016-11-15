/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.editpart;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.cheetahplatform.tdm.daily.model.DayTimeLine;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;

public class DayTimeLineEditPart extends GenericEditPart {

	public static final int WIDTH = 40;

	public static int computeHeight() {
		int gridLayoutMargin = 1;
		int height = DayTimeLineFigure.HOUR_HEIGHT * (DayTimeLineFigure.END_HOUR - DayTimeLineFigure.START_HOUR) + gridLayoutMargin;
		return height;
	}

	public DayTimeLineEditPart(DayTimeLine tddTimeLine) {
		super(tddTimeLine);
	}

	@Override
	protected IFigure createFigure() {
		return new DayTimeLineFigure((DayTimeLine) getModel());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// no properties to react on
	}

	@Override
	protected void refreshVisuals() {
		GridData constraint = new GridData();
		constraint.widthHint = WIDTH;
		constraint.grabExcessVerticalSpace = true;
		constraint.verticalAlignment = SWT.FILL;
		int height = computeHeight();
		constraint.heightHint = height;
		((AbstractGraphicalEditPart) getParent()).getFigure().setConstraint(figure, constraint);
	}

}
