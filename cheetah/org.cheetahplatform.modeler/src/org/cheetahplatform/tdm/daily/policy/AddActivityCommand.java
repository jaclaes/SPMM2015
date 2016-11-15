/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.policy;

import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.gef.commands.Command;

public class AddActivityCommand extends Command {
	private final List<DeclarativeActivity> activities;
	private final Workspace workspace;
	private final DateTime startTime;

	public AddActivityCommand(Workspace workspace, List<DeclarativeActivity> activities, DateTime startTime) {
		this.workspace = workspace;
		this.activities = activities;
		this.startTime = startTime;
	}

	@Override
	public void execute() {
		for (DeclarativeActivity activity : activities) {
			Activity newActivity = workspace.addActivityInstance(activity, startTime);
			Services.getModificationService().addListener(new ActivityChangeListener(newActivity), activity);
		}
	}
}
