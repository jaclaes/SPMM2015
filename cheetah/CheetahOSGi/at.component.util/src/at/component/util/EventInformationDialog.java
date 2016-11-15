package at.component.util;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import at.component.event.IEventInformation;

public class EventInformationDialog extends TitleAreaDialog {

	private final IEventInformation eventInformation;
	private final String dialogTitle;

	public EventInformationDialog(Shell parentShell, String dialogTitle, IEventInformation eventInformation) {
		super(parentShell);
		this.dialogTitle = dialogTitle;
		this.eventInformation = eventInformation;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Event Information");
		setTitle(dialogTitle);
		setMessage("This Dialog provides information about all events that are sent by this component.");

		Composite realParent = (Composite) super.createDialogArea(parent);

		new EventInformationComposite(realParent, SWT.NONE, eventInformation);

		return realParent;
	}

}
