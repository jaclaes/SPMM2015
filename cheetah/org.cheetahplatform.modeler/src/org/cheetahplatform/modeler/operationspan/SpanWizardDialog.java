package org.cheetahplatform.modeler.operationspan;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class SpanWizardDialog extends WizardDialog {

	public SpanWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);

	}

	@Override
	protected int getShellStyle() {
		return ~SWT.CLOSE & SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.TOOL;
	}

	@Override
	protected void handleShellCloseEvent() {
		// prevent closing of dialog by pressing esc.
	}

	@Override
	public void updateButtons() {
		super.updateButtons();
		getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
		getButton(IDialogConstants.BACK_ID).setEnabled(false);

		// prevents users from accidantely switching to the next page by clicking enter
		getShell().setDefaultButton(getButton(IDialogConstants.CANCEL_ID));
	}
}
