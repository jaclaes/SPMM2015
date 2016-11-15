/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.action;

import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;

public class MoveMilestoneToNextWeekAction extends MoveMilestoneAction {
	public static final String ID = "org.cheetahplatform.tdd.actions.MoveMilestoneAction";

	public MoveMilestoneToNextWeekAction(WeeklyMilestone milestone) {
		super(milestone, ID, "img/move_milestone.gif", "Move Milestone to Next Week");
	}

	@Override
	protected void doRun() {
		milestone.moveToNextWeek();
	}

}
