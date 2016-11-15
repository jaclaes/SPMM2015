package org.cheetahplatform.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2009
 */
public class ActivityExecutionComposite extends Composite {
	private Label dilbertLabel;
	private Button completeButton;
	private Button cancelButton;

	public ActivityExecutionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dilbertLabel = new Label(this, SWT.NONE);
		GridData layoutData = new GridData(SWT.CENTER, SWT.FILL, true, true);
		layoutData.horizontalSpan = 2;
		dilbertLabel.setLayoutData(layoutData);

		cancelButton = new Button(this, SWT.NONE);
		cancelButton.setText("Cancel Activity");
		GridData cancelButtonLayoutData = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false);
		cancelButtonLayoutData.widthHint = 100;
		cancelButton.setLayoutData(cancelButtonLayoutData);
		completeButton = new Button(this, SWT.NONE);
		completeButton.setText("Complete Activity");
		GridData completeButtonLayoutData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
		completeButtonLayoutData.widthHint = 100;
		completeButton.setLayoutData(completeButtonLayoutData);
	}

	/**
	 * Returns the cancelButton.
	 * 
	 * @return the cancelButton
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * Returns the completeButton.
	 * 
	 * @return the completeButton
	 */
	public Button getCompleteButton() {
		return completeButton;
	}

	/**
	 * Returns the dilbertLabel.
	 * 
	 * @return the dilbertLabel
	 */
	public Label getDilbertLabel() {
		return dilbertLabel;
	}

}
