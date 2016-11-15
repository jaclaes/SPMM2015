/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.policy;

import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class MoveWeeklyActivityCommand extends Command {
	private final WeeklyActivity activity;
	private final Point moveDelta;
	private final MultiWeek week;
	private final int weekOffset;

	public MoveWeeklyActivityCommand(WeeklyActivity activity, MultiWeek week, Point moveDelta, int weekOffset) {
		this.activity = activity;
		this.week = week;
		this.moveDelta = moveDelta;
		this.weekOffset = weekOffset;
	}

	@Override
	public void execute() {
		activity.setCustomLayout(true);
		activity.move(week, moveDelta, weekOffset);
		activity.getWorkspace().getWeekly().adaptMilestone(activity);
	}
}
