package org.cheetahplatform.modeler.decserflow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class EditSelectionConstraintDialogComposite extends Composite {
	private Text minimumText;
	private Text maximumText;
	private Button minimumButton;
	private Button maximumButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EditSelectionConstraintDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		minimumButton = new Button(this, SWT.CHECK);
		minimumButton.setText("Restrict Minimum Occurrence to:");

		minimumText = new Text(this, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 50;
		minimumText.setLayoutData(gd_text);

		maximumButton = new Button(this, SWT.CHECK);
		maximumButton.setText("Restrict Maximum Occurrence to:");

		maximumText = new Text(this, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_1.widthHint = 50;
		maximumText.setLayoutData(gd_text_1);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the maximumButton
	 */
	public Button getMaximumButton() {
		return maximumButton;
	}

	/**
	 * @return the maximumText
	 */
	public Text getMaximumText() {
		return maximumText;
	}

	/**
	 * @return the minimumButton
	 */
	public Button getMinimumButton() {
		return minimumButton;
	}

	/**
	 * @return the minimumText
	 */
	public Text getMinimumText() {
		return minimumText;
	}
}
