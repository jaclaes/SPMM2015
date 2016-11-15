package org.cheetahplatform.tdm.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

public class DetermineConsistencyDialogComposite extends Composite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DetermineConsistencyDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(this, SWT.WRAP);
		GridData gd_label = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gd_label.verticalIndent = 10;
		gd_label.horizontalIndent = 10;
		label.setLayoutData(gd_label);
		label.setText("Please check whether the changes your performed are consistent with invariants a) to h).");
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));

		setBackground(SWTResourceManager.getColor(255, 255, 255));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
