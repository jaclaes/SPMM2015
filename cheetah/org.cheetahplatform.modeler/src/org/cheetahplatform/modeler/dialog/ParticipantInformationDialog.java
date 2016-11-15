package org.cheetahplatform.modeler.dialog;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class ParticipantInformationDialog extends TitleAreaDialog {

	private static final int AGREE_ID = 20000;
	private final int durationInMinutes;

	public ParticipantInformationDialog(Shell parentShell, int durationInMinutes) {
		super(parentShell);
		Assert.isLegal(durationInMinutes > 0);
		this.durationInMinutes = durationInMinutes;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (AGREE_ID == buttonId) {
			okPressed();
			return;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, AGREE_ID, "I Agree", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		Composite container = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		setTitle("Participant Information");
		setMessage("Introduction and General Data");

		Label textLabel = new Label(composite, SWT.WRAP);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = 600;

		textLabel.setLayoutData(gridData);
		String message = "The objective of this research study is to examine idea generation in business process innovation. This study is being conducted at the Institute for Information Systems & New Media at the Vienna University of Economics and Business and the Department of Computer Science at the University of Innsbruck.\n\nYour participation in this project is voluntary and all comments and responses are anonymous and will be treated confidentially. Completion of the experiment will take approximately "
				+ durationInMinutes
				+ " minutes.\n\nBy clicking “I agree”, you are indicating that you:\n  - have read and understood the information about this project\n  - understand that you are free to withdraw at any time prior to submission of the completed questionnaire";
		textLabel.setText(message);

		Label thanksLabel = new Label(composite, SWT.NONE);
		thanksLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		thanksLabel.setFont(SWTResourceManager.getBoldFont(thanksLabel.getFont()));
		thanksLabel.setText("\nThank you for helping with this research project.");

		return container;
	}
}
