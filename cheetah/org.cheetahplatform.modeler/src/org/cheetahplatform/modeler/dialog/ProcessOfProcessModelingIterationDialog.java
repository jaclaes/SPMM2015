package org.cheetahplatform.modeler.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProcessOfProcessModelingIterationDialog extends TitleAreaDialog {

	private final class ValidationListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent arg0) {
			valdate();
		}
	}

	private Button keepSettingsButton;
	private Text reconciliationText;
	private Text modelingText;
	private Text comprehensionText;
	private double comprehensionPenalty;
	private double modelingPenalty;
	private double reconciliationPenalty;
	private boolean keepSettings;

	public ProcessOfProcessModelingIterationDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Missing Phase Penalties");
		setMessage("Please enter the penalities for missing phases.");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, true));
		Label comprehensionPenaltyLabel = new Label(composite, SWT.NONE);
		comprehensionPenaltyLabel.setText("Comprehension Penalty");
		GridData labelGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		comprehensionPenaltyLabel.setLayoutData(labelGridData);

		comprehensionText = new Text(composite, SWT.BORDER);
		comprehensionText.setLayoutData(labelGridData);
		comprehensionText.setText("3.0");
		comprehensionText.addModifyListener(new ValidationListener());

		Label modelingLabel = new Label(composite, SWT.NONE);
		modelingLabel.setText("Modeling Penalty");
		modelingLabel.setLayoutData(labelGridData);

		modelingText = new Text(composite, SWT.BORDER);
		modelingText.setLayoutData(labelGridData);
		modelingText.setText("2.0");
		modelingText.addModifyListener(new ValidationListener());

		Label reconciliationLabel = new Label(composite, SWT.NONE);
		reconciliationLabel.setText("Reconciliation Penalty");
		reconciliationLabel.setLayoutData(labelGridData);

		reconciliationText = new Text(composite, SWT.BORDER);
		reconciliationText.setLayoutData(labelGridData);
		reconciliationText.setText("1.0");
		reconciliationText.addModifyListener(new ValidationListener());

		keepSettingsButton = new Button(composite, SWT.CHECK);
		keepSettingsButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		keepSettingsButton.setText("Keep setting for all models to be exported");
		keepSettingsButton.setSelection(true);

		return container;
	}

	public double getComprehensionPenalty() {
		return comprehensionPenalty;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 250);
	}

	public double getModelingPenalty() {
		return modelingPenalty;
	}

	public double getReconciliationPenalty() {
		return reconciliationPenalty;
	}

	public boolean isKeepSettings() {
		return keepSettings;
	}

	@Override
	protected void okPressed() {
		comprehensionPenalty = Double.parseDouble(comprehensionText.getText());
		modelingPenalty = Double.parseDouble(modelingText.getText());
		reconciliationPenalty = Double.parseDouble(reconciliationText.getText());
		keepSettings = keepSettingsButton.getSelection();

		super.okPressed();

	}

	protected void valdate() {
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		try {
			Double.parseDouble(comprehensionText.getText());
		} catch (NumberFormatException e) {
			setErrorMessage("Please enter a valid number for the comprehension penalty");
			return;
		}
		try {
			Double.parseDouble(modelingText.getText());
		} catch (NumberFormatException e) {
			setErrorMessage("Please enter a valid number for the modeling penalty");
			return;
		}
		try {
			Double.parseDouble(reconciliationText.getText());
		} catch (NumberFormatException e) {
			setErrorMessage("Please enter a valid number for the reconciliation penalty");
			return;
		}

		getButton(IDialogConstants.OK_ID).setEnabled(true);
		setErrorMessage(null);
	}
}
