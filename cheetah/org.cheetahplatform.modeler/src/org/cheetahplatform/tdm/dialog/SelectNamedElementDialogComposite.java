package org.cheetahplatform.tdm.dialog;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class SelectNamedElementDialogComposite extends Composite {
	private Table table;
	private TableViewer activitiesViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectNamedElementDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		activitiesViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = activitiesViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(450);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the activitiesViewer
	 */
	public TableViewer getActivitiesViewer() {
		return activitiesViewer;
	}

}
