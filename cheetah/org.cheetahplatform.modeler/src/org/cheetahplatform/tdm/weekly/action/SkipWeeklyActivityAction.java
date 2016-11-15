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

public class SkipWeeklyActivityAction extends Action {
	public static final String ID = "org.cheetahplatform.tdd.actions.SkipWeeklyActivityAction";
	private final WeeklyActivity activity;

	public SkipWeeklyActivityAction(WeeklyActivity activity) {
		this.activity = activity;

		setId(ID);
		setText("Skip");
		setToolTipText("Skip Activity");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/skipped.gif"));
	}

	@Override
	public boolean isEnabled() {
		return activity.canBeSkipped();
	}

	@Override
	public void run() {
		activity.skip();
	}
}
