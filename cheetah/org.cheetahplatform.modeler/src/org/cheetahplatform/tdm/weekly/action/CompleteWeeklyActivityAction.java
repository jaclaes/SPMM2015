/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.action;

import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.eclipse.jface.action.Action;

public class CompleteWeeklyActivityAction extends Action {
	private final WeeklyActivity activity;

	public CompleteWeeklyActivityAction(WeeklyActivity activity) {
		this.activity = activity;

		setText("Complete");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/completed.png"));
		setDisabledImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/completed_disabled.png"));
	}

	@Override
	public boolean isEnabled() {
		return activity.canBeCompleted();
	}

	@Override
	public void run() {
		activity.complete();
	}
}
