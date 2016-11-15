package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;

/**
 * An action for filtering all dismissed {@link IReminderInstance}s in a {@link TableViewer}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2009
 */
public class FilterDismissedRemindersAction extends Action {
	private static class DismissedRemindersFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return !((IReminderInstance) element).isDismissed();
		}
	}

	private boolean showDismissed;
	private final TableViewer reminderViewer;
	private DismissedRemindersFilter filter;

	public FilterDismissedRemindersAction(TableViewer reminderViewer) {
		super("", SWT.TOGGLE);
		this.reminderViewer = reminderViewer;
		showDismissed = true;
		setChecked(showDismissed);
		filter = new DismissedRemindersFilter();
		setToolTipText("Show Dismissed Reminders");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/reminder_disabled.png"));
	}

	@Override
	public void run() {
		showDismissed = !showDismissed;

		if (!showDismissed)
			reminderViewer.addFilter(filter);
		else
			reminderViewer.removeFilter(filter);

		setChecked(showDismissed);
	}
}
