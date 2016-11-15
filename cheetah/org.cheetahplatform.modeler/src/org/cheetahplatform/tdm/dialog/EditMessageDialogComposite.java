/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.dialog;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class EditMessageDialogComposite extends Composite {

	private TextViewer textViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EditMessageDialogComposite(Composite parent, int style) {
		super(parent, style);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setLayout(new GridLayout(1, false));
		{
			textViewer = new TextViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP);
			textViewer.getTextWidget().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Return the textViewer.
	 * 
	 * @return the textViewer
	 */
	public TextViewer getTextViewer() {
		return textViewer;
	}
}
