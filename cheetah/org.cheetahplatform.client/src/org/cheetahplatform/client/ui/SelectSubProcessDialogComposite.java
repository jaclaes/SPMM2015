package org.cheetahplatform.client.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * @author zugi
 * 
 */
public class SelectSubProcessDialogComposite extends Composite {
	private Table table;
	private TableViewer subProcessesTable;
	private Composite subProcessComposite;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectSubProcessDialogComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		subProcessesTable = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = subProcessesTable.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		subProcessComposite = new Composite(this, SWT.BORDER);
		subProcessComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		subProcessComposite.setLayout(new GridLayout(1, false));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the subProcessComposite
	 */
	public Composite getSubProcessComposite() {
		return subProcessComposite;
	}

	/**
	 * @return the subProcessesTable
	 */
	public TableViewer getSubProcessesTable() {
		return subProcessesTable;
	}

}
