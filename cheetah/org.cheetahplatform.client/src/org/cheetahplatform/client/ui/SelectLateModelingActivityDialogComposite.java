package org.cheetahplatform.client.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class SelectLateModelingActivityDialogComposite extends Composite {
	private Table table;
	private TableViewer activitiesTable;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectLateModelingActivityDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		activitiesTable = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = activitiesTable.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the activitiesTable
	 */
	public TableViewer getActivitiesTable() {
		return activitiesTable;
	}

}
