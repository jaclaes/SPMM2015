package org.cheetahplatform.modeler.decserflow;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SelectActivityComposite extends Composite {

	private ComboViewer activitySelectionViewer;
	private Label activityNameLabel;
	private Button clearButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	/**
	 * @param parent
	 * @param style
	 */
	public SelectActivityComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		setLayout(gridLayout);

		activityNameLabel = new Label(this, SWT.NONE);
		activityNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		activityNameLabel.setText("Activity X");

		activitySelectionViewer = new ComboViewer(this, SWT.READ_ONLY);
		Combo combo = activitySelectionViewer.getCombo();
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_combo.horizontalIndent = 20;
		combo.setLayoutData(gd_combo);

		clearButton = new Button(this, SWT.NONE);
		clearButton.setText("Clear");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Label getActivityNameLabel() {
		return activityNameLabel;
	}

	public ComboViewer getActivitySelectionViewer() {
		return activitySelectionViewer;
	}

	public Button getClearButton() {
		return clearButton;
	}

}
