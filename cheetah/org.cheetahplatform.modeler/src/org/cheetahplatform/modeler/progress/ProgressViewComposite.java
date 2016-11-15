package org.cheetahplatform.modeler.progress;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class ProgressViewComposite extends Composite {

	private ProgressBar progressBar;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProgressViewComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());

		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

}
