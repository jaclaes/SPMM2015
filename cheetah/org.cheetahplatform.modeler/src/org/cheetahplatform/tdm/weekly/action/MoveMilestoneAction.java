/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.action;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.modeler.Activator;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyMilestone;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public abstract class MoveMilestoneAction extends Action {
	protected final WeeklyMilestone milestone;

	protected MoveMilestoneAction(WeeklyMilestone milestone, String id, String image, String text) {
		this.milestone = milestone;

		setId(id);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), image));
		setText(text);
	}

	/**
	 * If the milestone lies in the past the user is asked whether he still wants to move it.
	 * 
	 * @return <code>true</code> if the milestone can be moved, <code>false</code> otherwise
	 */
	protected boolean checkTime() {
		DateTime currentTime = DateTimeProvider.getDateTimeSource().getCurrentTime(true);
		DateTime start = milestone.getStartTime().copy();
		start.plus(new Duration(7, 0, 0));
		boolean isInPast = currentTime.compareTo(start) > 0;

		if (isInPast) {
			return MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Milestone in Past",
					"The milestone is in the past. Move it anyway?");
		}

		return true;
	}

	protected abstract void doRun();

	@Override
	public final void run() {
		if (!checkTime()) {
			return;
		}

		doRun();
	}
}
