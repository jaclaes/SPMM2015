/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import java.util.List;

import org.cheetahplatform.tdm.daily.editpart.PlanningAreaEditPart;
import org.eclipse.gef.EditPart;

public class PlanningArea extends AbstractPlanningArea {

	public PlanningArea(Day parent) {
		super(parent, PlanningAreaEditPart.WIDTH);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new PlanningAreaEditPart(this);
	}

	@Override
	public List<Activity> getActivities() {
		return getWorkspace().getActivityInstances((Day) parent);
	}

	@Override
	public List<? extends Object> getChildren() {
		return getActivities();
	}

}
