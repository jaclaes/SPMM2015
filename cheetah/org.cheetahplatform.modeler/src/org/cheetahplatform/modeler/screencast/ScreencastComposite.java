package org.cheetahplatform.modeler.screencast;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ScreencastComposite extends Composite {

	private Browser browser;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ScreencastComposite(Composite parent, int style) {
		super(parent, style);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		browser = new Browser(this, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Browser getBrowser() {
		return browser;
	}

}
