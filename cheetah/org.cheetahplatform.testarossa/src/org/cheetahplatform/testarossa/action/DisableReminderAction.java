package org.cheetahplatform.testarossa.action;

import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2009
 */
public class DisableReminderAction extends Action implements ISelectionChangedListener {
	private final StructuredViewer viewer;

	public DisableReminderAction(StructuredViewer viewer) {
		super("Dismiss Reminder");
		setToolTipText("Dismisses the reminder.");
		this.viewer = viewer;
		viewer.addSelectionChangedListener(this);
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if (selection.isEmpty())
			return;

		IReminderInstance reminder = (IReminderInstance) selection.getFirstElement();
		reminder.setDismissed(!reminder.isDismissed());
		viewer.refresh();
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if (selection.isEmpty()) {
			setEnabled(false);
			return;
		}

		setEnabled(true);
		IReminderInstance reminder = (IReminderInstance) selection.getFirstElement();
		if (reminder.isDismissed()) {
			setText("Enable Reminder");
			setToolTipText("Enables the Reminder");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/reminder.png"));
		} else {
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/reminder_disabled.png"));
			setText("Dismiss Reminder");
			setToolTipText("Dismisses the reminder.");
		}
	}
}
