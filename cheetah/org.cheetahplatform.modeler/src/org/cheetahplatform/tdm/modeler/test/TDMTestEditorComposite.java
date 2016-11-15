package org.cheetahplatform.tdm.modeler.test;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class TDMTestEditorComposite extends Composite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TDMTestEditorComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
