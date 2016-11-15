/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;
import org.cheetahplatform.tdm.weekly.action.MoveMilestoneToNextWeekAction;
import org.cheetahplatform.tdm.weekly.action.MoveMilestoneToPreviousWeekAction;
import org.cheetahplatform.tdm.weekly.figure.WeeklyMilestoneFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IMenuManager;

public class WeeklyMilestoneEditPart extends GenericEditPart {

	public static final int MILESTONE_WIDTH = 20;

	public WeeklyMilestoneEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		menu.add(new MoveMilestoneToPreviousWeekAction((WeeklyMilestone) getModel()));
		menu.add(new MoveMilestoneToNextWeekAction((WeeklyMilestone) getModel()));
	}

	@Override
	protected IFigure createFigure() {
		String label = ((WeeklyMilestone) getModel()).getLabel();
		return new WeeklyMilestoneFigure(label);
	}

}
