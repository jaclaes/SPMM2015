/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.testarossa.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DateSelectionComposite extends Composite {

	private Text dateText;

	private Button selectDateButton;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public DateSelectionComposite(Composite parent, int style) {
		super(parent, style);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		dateText = new Text(this, SWT.BORDER);
		dateText.setEditable(false);
		dateText.setEnabled(false);
		dateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		selectDateButton = new Button(this, SWT.NONE);
		selectDateButton.setText("Select Date ...");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Return the dateText.
	 * 
	 * @return the dateText
	 */
	public Text getDateText() {
		return dateText;
	}

	/**
	 * Return the selectDateButton.
	 * 
	 * @return the selectDateButton
	 */
	public Button getSelectDateButton() {
		return selectDateButton;
	}

}
