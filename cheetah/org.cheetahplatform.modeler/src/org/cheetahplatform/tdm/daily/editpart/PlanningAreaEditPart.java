/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.editpart;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.daily.action.AddActivityInstanceAction;
import org.cheetahplatform.tdm.daily.action.CreateActivityInstanceAction;
import org.cheetahplatform.tdm.daily.figure.PlanningAreaFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IMenuManager;

public class PlanningAreaEditPart extends AbstractPlanningAreaEditPart {

	public static final int WIDTH = AbstractPlanningAreaEditPart.WIDTH;

	public PlanningAreaEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			menu.add(new AddActivityInstanceAction(this, dropLocation));
			menu.add(new CreateActivityInstanceAction(this, dropLocation));
		}
	}

	@Override
	protected IFigure createFigure() {
		PlanningAreaFigure figure = (PlanningAreaFigure) super.createFigure();
		figure.setBackgroundText("Right-click to add activity");
		return figure;
	}

	@Override
	public TimeSlot getSelection() {
		return getModel().getWorkspace().getPlanningAreaSelection();
	}

	@Override
	public void setSelection(TimeSlot slot) {
		getModel().getWorkspace().setPlanningAreaSelection(slot);
	}

}
