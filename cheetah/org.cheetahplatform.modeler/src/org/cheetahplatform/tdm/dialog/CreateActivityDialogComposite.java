package org.cheetahplatform.tdm.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateActivityDialogComposite extends Composite {

	private Text nameText;
	private DateTime expectedTime;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public CreateActivityDialogComposite(Composite parent, int style) {
		super(parent, style);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);

		final Label nameLabel = new Label(this, SWT.NONE);
		nameLabel.setText("Name:");

		nameText = new Text(this, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label expectedDurationLabel = new Label(this, SWT.NONE);
		expectedDurationLabel.setText("Expected Duration:");

		expectedTime = new DateTime(this, SWT.TIME | SWT.SHORT | SWT.BORDER);
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public DateTime getExpectedTime() {
		return expectedTime;
	}

	public Text getNameTextWidget() {
		return nameText;
	}

}
