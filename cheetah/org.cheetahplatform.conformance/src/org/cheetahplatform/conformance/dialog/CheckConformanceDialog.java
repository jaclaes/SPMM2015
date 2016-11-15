package org.cheetahplatform.conformance.dialog;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.conformance.Activator;
import org.cheetahplatform.conformance.core.CheckConformanceRunnable;
import org.cheetahplatform.conformance.model.CheckConformanceDialogModel;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class CheckConformanceDialog extends LocationPersistentTitleAreaDialog {

	private CheckConformanceDialogComposite composite;
	private CheckConformanceDialogModel model;

	public CheckConformanceDialog(Shell parentShell) {
		super(parentShell);

		model = new CheckConformanceDialogModel();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new CheckConformanceDialogComposite(parent, SWT.NONE);

		getShell().setText("Check Trace Equivalence");
		setTitle("Check Trace Equivalence");
		setMessage("Please select the process models to be checked for trace equivalency below.");
		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		String firstFile = composite.getFirstFileEditor().getStringValue();
		if (firstFile.trim().isEmpty()) {
			return "Please select the first process model.";
		}

		String secondFile = composite.getSecondFileEditor().getStringValue();
		if (secondFile.trim().isEmpty()) {
			return "Please select the second process model.";
		}

		return null;
	}

	public CheckConformanceRunnable getCheckConformanceRunnable() {
		return model.createCheckConformanceRunnable();
	}

	private void initialize() {
		addValidationListener(composite.getFirstFileEditor());
		addValidationListener(composite.getSecondFileEditor());
	}

	@Override
	protected void okPressed() {
		String firstFile = composite.getFirstFileEditor().getStringValue();
		String secondFile = composite.getSecondFileEditor().getStringValue();
		try {
			model.setFirstFile(firstFile);
			model.setSecondFile(secondFile);
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An error occurred.", e);
			Activator.getDefault().getLog().log(status);
			MessageDialog.openError(getShell(), "Error", "An error occurred.");

			setReturnCode(CANCEL);
		}

		super.okPressed();
	}

}
