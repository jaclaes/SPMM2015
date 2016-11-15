/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.dialog;

import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

public class CreateActivityDialog extends LocationPersistentTitleAreaDialog {

	private final DeclarativeProcessSchema schema;
	private CreateActivityDialogComposite composite;
	private DeclarativeActivity activity;

	public CreateActivityDialog(Shell parentShell, DeclarativeProcessSchema schema) {
		super(parentShell);
		this.schema = schema;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new CreateActivityDialogComposite(parent, SWT.NONE);
		setTitle("Create Activity");
		getShell().setText("Create Activity");
		setMessage("Enter the fields below to create a new activity.");

		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		String name = composite.getNameTextWidget().getText();
		if (name.trim().length() == 0) {
			return "Please enter a name";
		}
		for (INode node : schema.getNodes()) {
			if (node.getName().equals(name)) {
				return "\"" + name + "\" is already in use by another activity";
			}
		}

		DateTime time = composite.getExpectedTime();
		if (time.getMinutes() == 0 && time.getHours() == 0) {
			return "Please enter an expected time";
		}

		return null;
	}

	public DeclarativeActivity getActivity() {
		return activity;
	}

	private void initialize() {
		composite.getExpectedTime().setTime(0, 30, 0);

		addValidationListener(composite.getNameTextWidget());
		addValidationListener(composite.getExpectedTime());
	}

	@Override
	protected void okPressed() {
		int hours = composite.getExpectedTime().getHours();
		int minutes = composite.getExpectedTime().getMinutes();
		Duration expectedDuration = new Duration(hours, minutes);
		String name = composite.getNameTextWidget().getText().trim();

		activity = schema.createActivity(name);
		activity.setExpectedDuration(expectedDuration);

		super.okPressed();
	}

}