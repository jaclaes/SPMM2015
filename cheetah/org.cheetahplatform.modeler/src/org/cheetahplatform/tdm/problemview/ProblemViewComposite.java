package org.cheetahplatform.tdm.problemview;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ProblemViewComposite extends Composite {
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProblemViewComposite(Composite parent, int style) {
		super(parent, style);

		tableViewer = new TableViewer(this, SWT.NONE | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText("Problem");
		//
		// TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		// tableColumn_1.setText("Test");

		TableColumnLayout layout = new TableColumnLayout();
		setLayout(layout);
		layout.setColumnData(tableColumn, new ColumnWeightData(80));
		// layout.setColumnData(tableColumn_1, new ColumnWeightData(20));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}
}
