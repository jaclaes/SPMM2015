/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.GenericTDMModel;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyMilestoneEditPart;
import org.eclipse.gef.EditPart;

public class WeeklyMilestone extends GenericTDMModel {

	private final MilestoneInstance milestone;

	public WeeklyMilestone(IGenericModel parent, MilestoneInstance milestone) {
		super(parent);

		Assert.isNotNull(parent);
		this.milestone = milestone;
	}

	public void addActivity(WeeklyActivity activity) {
		milestone.addActivity(activity.getActivity());
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WeeklyMilestoneEditPart(this);
	}

	public String getLabel() {
		return milestone.getNode().getName();
	}

	/**
	 * Return the milestone.
	 * 
	 * @return the milestone
	 */
	public MilestoneInstance getMilestone() {
		return milestone;
	}

	public DateTime getStartTime() {
		return milestone.getStartTime();
	}

	public int getWeekOffset() {
		MultiWeek week = (MultiWeek) getParentType(MultiWeek.class);
		Duration difference = Date.weekEnd(week.getStartTime()).getDifference(getStartTime());
		return difference.getWeek();
	}

	public void moveToNextWeek() {
		getWorkspace().getWeekly().moveToNextWeek(this);
	}

	public void moveToPreviousWeek() {
		getWorkspace().getWeekly().moveToPreviousWeek(this);
	}

	public void removeActivity(WeeklyActivity activity) {
		milestone.removeActivity(activity.getActivity());
	}

	@Override
	public String toString() {
		return milestone.getNode().getName() + ", " + milestone.getStartTime();
	}

}
