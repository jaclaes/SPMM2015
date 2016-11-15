package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.common.ui.dialog.LocationPersistentTitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EditSelectionConstraintDialog extends LocationPersistentTitleAreaDialog {

	private final SelectionConstraintEdge constraint;
	private EditSelectionConstraintDialogComposite composite;

	public EditSelectionConstraintDialog(Shell parentShell, SelectionConstraintEdge constraint) {
		super(parentShell);

		this.constraint = constraint;
	}

	private void addListener() {
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				minimumOrMaximumButtonSelected();
			}
		};

		composite.getMinimumButton().addSelectionListener(listener);
		composite.getMaximumButton().addSelectionListener(listener);

		addValidationListener(composite.getMinimumButton());
		addValidationListener(composite.getMinimumText());
		addValidationListener(composite.getMaximumButton());
		addValidationListener(composite.getMaximumText());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new EditSelectionConstraintDialogComposite(realParent, SWT.NONE);

		setTitle("Selection Constraint");
		setMessage("The selection constraint's minimum and maximum values can be adjusted below.");
		getShell().setText("Selection Constraint");

		initialize();

		return realParent;
	}

	@Override
	protected String doValidate() {
		boolean isMinimumSelected = composite.getMinimumButton().getSelection();
		boolean isMaximumSelected = composite.getMaximumButton().getSelection();

		if (isMinimumSelected) {
			String message = validate(composite.getMinimumText(), 0, Integer.MAX_VALUE);
			if (message != null) {
				return message;
			}
		}

		if (isMaximumSelected) {
			String message = validate(composite.getMaximumText(), 0, Integer.MAX_VALUE);
			if (message != null) {
				return message;
			}
		}

		if (!isMinimumSelected && !isMaximumSelected) {
			return "Please choose at least a lower or upper bound.";
		}

		if (isMinimumSelected && isMaximumSelected) {
			int minimum = Integer.parseInt(composite.getMinimumText().getText());
			int maximum = Integer.parseInt(composite.getMaximumText().getText());

			if (maximum < minimum) {
				return "The minimum amount must be smaller equal to the maximum.";
			}
		}

		return null;
	}

	/**
	 * @return the constraint
	 */
	public SelectionConstraintEdge getConstraint() {
		return constraint;
	}

	private void initialize() {
		if (!constraint.hasMinimum()) {
			composite.getMinimumText().setEnabled(false);
		} else {
			String minimum = String.valueOf(constraint.getMinimum());
			composite.getMinimumText().setText(minimum);
			composite.getMinimumButton().setSelection(true);
		}

		if (!constraint.hasMaximum()) {
			composite.getMaximumText().setEnabled(false);
		} else {
			String maximum = String.valueOf(constraint.getMaximum());
			composite.getMaximumText().setText(maximum);
			composite.getMaximumButton().setSelection(true);
		}

		addListener();
	}

	protected void minimumOrMaximumButtonSelected() {
		composite.getMinimumText().setEnabled(composite.getMinimumButton().getSelection());
		composite.getMaximumText().setEnabled(composite.getMaximumButton().getSelection());
	}

	@Override
	protected void okPressed() {
		if (!composite.getMinimumButton().getSelection()) {
			constraint.unsetMinimum();
		} else {
			int minimum = Integer.parseInt(composite.getMinimumText().getText());
			constraint.setMinimum(minimum);
		}

		if (!composite.getMaximumButton().getSelection()) {
			constraint.unsetMaximum();
		} else {
			int maximum = Integer.parseInt(composite.getMaximumText().getText());
			constraint.setMaximum(maximum);
		}

		super.okPressed();
	}

	private String validate(Text input, int lowerBound, int upperBound) {
		String text = input.getText();
		try {
			int value = Integer.parseInt(text);
			if (value < lowerBound || upperBound < value) {
				return "Please enter a value between " + lowerBound + " and " + upperBound + ".";
			}
		} catch (NumberFormatException e) {
			return "Please enter an integer.";
		}

		return null;
	}

}
