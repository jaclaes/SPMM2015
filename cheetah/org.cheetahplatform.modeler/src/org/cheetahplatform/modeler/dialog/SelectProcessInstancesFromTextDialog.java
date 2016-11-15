package org.cheetahplatform.modeler.dialog;

import java.util.List;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectProcessInstancesFromTextDialog extends LocationPersistentTitleAreaDialog {

	private SelectProcessInstancesFromTextDialogComposite composite;
	private List<ProcessInstanceDatabaseHandle> selection;

	public SelectProcessInstancesFromTextDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectProcessInstancesFromTextDialogComposite(realParent, SWT.NONE);

		getShell().setText("Select Process Instances By Id");
		setTitle("Select Process Instances By Id");
		setMessage("Paste the instance ids to be selected in the text field below. You may use semicolons, spaces or newlines for separation.");
		initialize();

		return realParent;
	}

	public List<ProcessInstanceDatabaseHandle> getSelection() {
		return selection;
	}

	private void initialize() {
		composite.getSelectProcessInstancesButton().setEnabled(true);
	}

	@Override
	protected void okPressed() {
		String toParse = composite.getText().getText();
		SelectProcessInstancesFromTextDialogModel model = new SelectProcessInstancesFromTextDialogModel();

		if (composite.getSelectProcessInstancesButton().getSelection()) {
			selection = model.loadProcessInstancesById(toParse);
		} else {
			selection = model.loadExperimentalProcessInstancesByChildIds(toParse);
		}

		if (selection.isEmpty()) {
			MessageDialog.openWarning(getShell(), "Empty Selection", "Please select at least one process instance.");
			return;
		}

		super.okPressed();
	}

}
