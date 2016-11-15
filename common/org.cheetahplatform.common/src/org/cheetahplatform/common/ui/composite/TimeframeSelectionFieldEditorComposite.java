package org.cheetahplatform.common.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TimeframeSelectionFieldEditorComposite extends Composite {
	private Text text;
	private Button selectButton;
	private Button clearButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TimeframeSelectionFieldEditorComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);

		Label lblFromTo = new Label(this, SWT.NONE);
		GridData gd_lblFromTo = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblFromTo.widthHint = 100;
		lblFromTo.setLayoutData(gd_lblFromTo);
		lblFromTo.setText("From - To");

		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setEditable(false);

		selectButton = new Button(this, SWT.NONE);
		selectButton.setText("...");

		clearButton = new Button(this, SWT.NONE);
		clearButton.setText("clear");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the clearButton
	 */
	public Button getClearButton() {
		return clearButton;
	}

	/**
	 * @return the button
	 */
	public Button getSelectButton() {
		return selectButton;
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

}
