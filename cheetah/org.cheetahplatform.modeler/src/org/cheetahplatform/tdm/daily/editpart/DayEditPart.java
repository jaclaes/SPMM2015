/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.editpart;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.tdm.daily.model.Day;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;

public class DayEditPart extends GenericEditPart {

	public static final int WIDTH = DayTimeLineEditPart.WIDTH + PlanningAreaEditPart.WIDTH + ExecutionAssertionAreaEditPart.WIDTH
			+ TerminationAssertionAreaEditPart.WIDTH;

	public DayEditPart(Day tddDay) {
		super(tddDay);
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		figure.setLayoutManager(layout);
		return figure;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// no properties to react on
	}

	@Override
	protected void refreshVisuals() {
		GridData data = new GridData();
		data.widthHint = WIDTH;
		data.grabExcessVerticalSpace = true;
		data.heightHint = DayHeaderEditPart.HEIGHT + DayTimeLineEditPart.computeHeight();
		data.verticalAlignment = SWT.TOP;
		((WorkspaceEditPart) getParent()).getContent().setConstraint(figure, data);
	}

}
