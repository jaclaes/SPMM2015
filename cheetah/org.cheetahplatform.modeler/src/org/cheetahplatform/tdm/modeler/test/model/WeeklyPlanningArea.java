/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.GenericTDMModel;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyPlanningAreaEditPart;
import org.eclipse.gef.EditPart;

public class WeeklyPlanningArea extends GenericTDMModel {

	public WeeklyPlanningArea(IGenericModel parent) {
		super(parent);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WeeklyPlanningAreaEditPart(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Object> getChildren() {
		List instances = getWorkspace().getWeekly().getActivityInstances((MultiWeek) parent);
		List<? extends Object> milestones = getWorkspace().getWeekly().getMilestones((MultiWeek) getParent());
		instances.addAll(0, milestones);

		return instances;
	}

	public DateTime getStartTime() {
		return ((MultiWeek) getParent()).getStartTime();
	}
}
