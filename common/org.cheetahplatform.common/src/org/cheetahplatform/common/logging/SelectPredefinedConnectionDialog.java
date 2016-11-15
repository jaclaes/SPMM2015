package org.cheetahplatform.common.logging;

import org.cheetahplatform.common.logging.db.ConnectionSetting;
import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectPredefinedConnectionDialog extends LocationPersistentTitleAreaDialog {

	private SelectPredefinedConnectionDialogComposite composite;
	private SelectPredefinedConnectionDialogModel model;
	private ConnectionSetting connection;

	public SelectPredefinedConnectionDialog(Shell parentShell) {
		super(parentShell);

		model = new SelectPredefinedConnectionDialogModel();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new SelectPredefinedConnectionDialogComposite(realParent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		setMessage("Please select one of the predefined connections below.");
		getShell().setText("Select Predefined Connection");
		setTitle("Select Predefined Connection");

		TableViewer viewer = composite.getTableViewer();
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(model.createLabelProvider());
		viewer.setInput(model.getInput());
		addValidationListener(viewer);

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});

		return realParent;
	}

	@Override
	protected String doValidate() {
		if (composite.getTableViewer().getSelection().isEmpty()) {
			return "Please select a connection.";
		}

		return null;
	}

	public ConnectionSetting getConnection() {
		return connection;
	}

	@Override
	protected void okPressed() {
		connection = (ConnectionSetting) ((IStructuredSelection) composite.getTableViewer().getSelection()).getFirstElement();

		super.okPressed();
	}
}
