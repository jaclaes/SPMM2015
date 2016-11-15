package org.cheetahplatform.common.logging;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class SelectPredefinedConnectionDialogComposite extends Composite {
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectPredefinedConnectionDialogComposite(Composite parent, int style) {
		super(parent, style);

		TableColumnLayout layout = new TableColumnLayout();
		setLayout(layout);
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		TableColumn column = new TableColumn(table, SWT.NONE);
		layout.setColumnData(column, new ColumnWeightData(100));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

}
