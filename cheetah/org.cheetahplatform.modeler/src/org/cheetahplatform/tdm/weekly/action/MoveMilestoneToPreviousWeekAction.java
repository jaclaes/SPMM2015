/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.action;

import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;

public class MoveMilestoneToPreviousWeekAction extends MoveMilestoneAction {
	public static final String ID = "org.cheetahplatform.tdd.actions.MoveMilestoneToPreviousWeekAction";

	public MoveMilestoneToPreviousWeekAction(WeeklyMilestone milestone) {
		super(milestone, ID, "img/move_milestone_previous.gif", "Move Milestone to Previous Week");
	}

	@Override
	protected void doRun() {
		milestone.moveToPreviousWeek();
	}
}
