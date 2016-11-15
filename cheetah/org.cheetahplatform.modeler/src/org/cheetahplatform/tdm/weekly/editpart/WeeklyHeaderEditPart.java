/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyHeader;
import org.cheetahplatform.tdm.weekly.figure.WeeklyHeaderFigure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;

public class WeeklyHeaderEditPart extends GenericEditPart {

	public WeeklyHeaderEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		WeeklyHeader header = (WeeklyHeader) getModel();
		int count = WeeklyEditPart.getWeeksPerRow();
		return new WeeklyHeaderFigure(header.getStartDate(), count);
	}

	@Override
	protected void refreshVisuals() {
		GridData constraint = new GridData();
		constraint.grabExcessHorizontalSpace = true;
		constraint.horizontalAlignment = SWT.FILL;
		constraint.grabExcessVerticalSpace = false;
		constraint.heightHint = 25;
		figure.getParent().getLayoutManager().setConstraint(figure, constraint);
	}

}
