package org.cheetahplatform.testarossa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.IDeferredObjectProvider;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import org.cheetahplatform.testarossa.dialog.ReminderAggregationDialog;
import org.cheetahplatform.testarossa.dialog.ReminderDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2009
 */
public class ReminderJob extends Job implements IDeferredObjectProvider<DeclarativeProcessInstance> {
	private static final int RESCHEDULE_TIME = 3000;
	private Set<IReminderInstance> shownReminders;

	public ReminderJob() {
		super("Checking All Reminders");

		shownReminders = new HashSet<IReminderInstance>();
	}

	private void displayReminder(final List<IReminderInstance> remindersToDisplay) {
		if (remindersToDisplay.isEmpty()) {
			return;
		}

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Shell shell = Display.getDefault().getActiveShell();
				TitleAreaDialog dialog = null;
				if (remindersToDisplay.size() > 1) {
					dialog = new ReminderAggregationDialog(shell, ReminderJob.this, remindersToDisplay);
				} else {
					dialog = new ReminderDialog(shell, remindersToDisplay.get(0));
				}
				dialog.open();
			}
		});
	}

	public DeclarativeProcessInstance get() {
		return TestaRossaModel.getInstance().getCurrentInstance();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		DeclarativeProcessInstance instance = TestaRossaModel.getInstance().getCurrentInstance();
		if (instance == null) {
			schedule(RESCHEDULE_TIME);
			return Status.OK_STATUS;
		}

		List<IReminderInstance> remindersToDisplay = new ArrayList<IReminderInstance>();
		List<IReminderInstance> activeReminders = instance.getActiveReminders();
		for (IReminderInstance reminderInstance : activeReminders) {
			if (!shownReminders.contains(reminderInstance)) {
				remindersToDisplay.add(reminderInstance);
				shownReminders.add(reminderInstance);
			}
		}

		displayReminder(remindersToDisplay);

		schedule(RESCHEDULE_TIME);
		return Status.OK_STATUS;
	}
}
