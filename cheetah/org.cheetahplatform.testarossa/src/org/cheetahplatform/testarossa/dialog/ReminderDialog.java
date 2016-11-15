package org.cheetahplatform.testarossa.dialog;

import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.view.ReminderView;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2009
 */
public class ReminderDialog extends TitleAreaDialog {
	public static final int CHANGE_REMINDER_STATE = 10000;

	private final IReminderInstance reminderInstance;

	public ReminderDialog(Shell parent, IReminderInstance reminderInstance) {
		super(parent);
		Assert.isNotNull(reminderInstance);
		this.reminderInstance = reminderInstance;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == CHANGE_REMINDER_STATE) {
			reminderInstance.setDismissed(!reminderInstance.isDismissed());
			IViewPart view = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ReminderView.id);
			if (view != null) {
				((ReminderView) view).refresh();
			}
			setReturnCode(CHANGE_REMINDER_STATE);
			close();
			return;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(reminderInstance.getReminder().getName());
		newShell.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "img/reminder.png"));
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (reminderInstance.isDismissed()) {
			createButton(parent, CHANGE_REMINDER_STATE, "Enable Reminder", false);
			createButton(parent, IDialogConstants.OK_ID, "Close", true);
		} else {
			createButton(parent, CHANGE_REMINDER_STATE, "Dismiss Reminder", true);
			createButton(parent, IDialogConstants.OK_ID, "Close", false);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Reminder");
		setMessage(reminderInstance.getReminder().getName());
		Composite container = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginTop = 10;
		layout.marginLeft = 10;
		layout.marginRight = 10;
		layout.marginBottom = 10;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Label text = new Label(composite, SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setText(reminderInstance.getReminder().getReminderText());
		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 300);
	}
}
