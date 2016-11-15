package org.cheetahplatform.modeler.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.PageBook;

public class SelectProcessInstanceDialogComposite extends Composite {

	private PageBook pageBook;
	private Button manualSelectionButton;
	private Button idSelectionButton;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectProcessInstanceDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group.setLayout(new GridLayout(2, true));

		manualSelectionButton = new Button(group, SWT.RADIO);
		manualSelectionButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		manualSelectionButton.setText("Manual Selection");

		idSelectionButton = new Button(group, SWT.RADIO);
		idSelectionButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		idSelectionButton.setText("Select by Id");

		pageBook = new PageBook(this, SWT.NONE);
		pageBook.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	public Button getIdSelectionButton() {
		return idSelectionButton;
	}

	public Button getManualSelectionButton() {
		return manualSelectionButton;
	}

	public PageBook getPageBook() {
		return pageBook;
	}

}
