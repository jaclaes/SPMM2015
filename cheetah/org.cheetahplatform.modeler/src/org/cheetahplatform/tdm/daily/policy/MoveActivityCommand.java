/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.policy;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.eclipse.gef.commands.Command;

public class MoveActivityCommand extends Command {

	private final Activity activity;
	private final DateTime newStartTime;

	public MoveActivityCommand(Activity activity, DateTime newStartTime) {
		this.activity = activity;
		this.newStartTime = newStartTime;
	}

	@Override
	public void execute() {
		activity.setStartTime(newStartTime);
		activity.select();
	}

}
