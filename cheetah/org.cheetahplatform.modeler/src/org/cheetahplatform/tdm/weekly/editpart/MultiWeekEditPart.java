/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.weekly.figure.MultiWeekFigure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;

public class MultiWeekEditPart extends GenericEditPart {

	public MultiWeekEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new MultiWeekFigure();
		GridLayout manager = new GridLayout();
		manager.verticalSpacing = 0;
		manager.marginWidth = 0;
		figure.setLayoutManager(manager);
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		GridData constraint = new GridData();
		constraint.grabExcessHorizontalSpace = true;
		constraint.grabExcessVerticalSpace = true;
		constraint.verticalAlignment = SWT.FILL;
		constraint.horizontalAlignment = SWT.FILL;

		figure.getParent().setConstraint(figure, constraint);
	}

}
