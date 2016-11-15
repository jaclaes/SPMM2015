package org.cheetahplatform.tdm.modeler.declarative;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class TDMDeclarativeModelerViewComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TDMDeclarativeModelerViewComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
