package org.cheetahplatform.tdm.daily.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.daily.figure.DayHeaderFigure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;

public class DayHeaderEditPart extends GenericEditPart {

	public static final int HEIGHT = 20;

	public DayHeaderEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		return new DayHeaderFigure();
	}

	@Override
	protected void refreshVisuals() {
		GridData constraint = new GridData(SWT.FILL, SWT.TOP, true, false);
		constraint.heightHint = HEIGHT;
		constraint.horizontalSpan = 4;
		((AbstractGraphicalEditPart) getParent()).getFigure().setConstraint(figure, constraint);
	}

}
