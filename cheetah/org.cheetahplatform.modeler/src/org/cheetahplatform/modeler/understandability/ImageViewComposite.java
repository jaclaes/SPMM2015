package org.cheetahplatform.modeler.understandability;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

public class ImageViewComposite extends Composite {

	private Label imageLabel;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ImageViewComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		setBackground(SWTResourceManager.getColor(255, 255, 255));

		imageLabel = new Label(this, SWT.NONE);
		imageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the imageLabel
	 */
	public Label getImageLabel() {
		return imageLabel;
	}

}
