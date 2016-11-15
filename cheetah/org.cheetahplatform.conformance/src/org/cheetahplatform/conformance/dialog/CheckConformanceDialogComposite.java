package org.cheetahplatform.conformance.dialog;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class CheckConformanceDialogComposite extends Composite {

	private FileFieldEditor firstFileEditor;
	private FileFieldEditor secondFileEditor;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CheckConformanceDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		firstFileEditor = new FileFieldEditor("first_input", "First Process Model", true, composite);
		firstFileEditor.getTextControl(composite).setEditable(false);
		GridData data = new GridData();
		data.widthHint = 150;
		firstFileEditor.getLabelControl(composite).setLayoutData(data);

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(1, false));
		secondFileEditor = new FileFieldEditor("first_input", "Second Process Model", true, composite_1);
		secondFileEditor.getTextControl(composite_1).setEditable(false);
		GridData data_1 = new GridData();
		data_1.widthHint = 150;
		secondFileEditor.getLabelControl(composite_1).setLayoutData(data_1);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the firstFileEditor
	 */
	public FileFieldEditor getFirstFileEditor() {
		return firstFileEditor;
	}

	/**
	 * @return the secondFileEditor
	 */
	public FileFieldEditor getSecondFileEditor() {
		return secondFileEditor;
	}

}
