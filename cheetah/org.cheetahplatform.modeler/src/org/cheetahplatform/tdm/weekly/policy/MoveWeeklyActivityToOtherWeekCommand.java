/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.policy;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.tdm.modeler.test.model.MultiWeek;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class MoveWeeklyActivityToOtherWeekCommand extends Command {

	private final WeeklyActivity activity;
	private final MultiWeek targetModel;
	private final Point newLocation;
	private final DateTime newStart;

	public MoveWeeklyActivityToOtherWeekCommand(WeeklyActivity activity, MultiWeek targetModel, Point newLocation, DateTime newStart) {
		this.activity = activity;
		this.targetModel = targetModel;
		this.newLocation = newLocation;
		this.newStart = newStart;
	}

	@Override
	public void execute() {
		DateTime oldStart = activity.getStartTime().copy();

		activity.clearBounds();
		activity.setCustomLayout(true);
		activity.setLocation(targetModel, newLocation);
		activity.setStartTime(newStart);
		activity.adaptMilestone();

		activity.logMovedToOtherWeekIfNecessary(oldStart, newStart);
	}

}
