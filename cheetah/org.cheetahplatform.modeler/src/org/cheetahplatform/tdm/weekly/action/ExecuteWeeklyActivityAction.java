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

public class ExecuteWeeklyActivityAction extends Action {

	private final WeeklyActivity activity;

	public ExecuteWeeklyActivityAction(WeeklyActivity activity) {
		setToolTipText("Execute Activity");
		setText("Execute");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/launch-16x16.gif"));
		setDisabledImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/launch-16x16_disabled.gif"));

		this.activity = activity;
	}

	@Override
	public boolean isEnabled() {
		return activity.canBeLaunched();
	}

	@Override
	public void run() {
		activity.launch();
	}

}
