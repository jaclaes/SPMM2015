package org.cheetahplatform.common.ui;

import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewerColumnSorter extends AbstractViewerColumnSorter {

	public static void addColumnSorter(TableViewer viewer, List<ViewerSorter> sorters) {
		new TableViewerColumnSorter(viewer, sorters);
	}

	private TableViewerColumnSorter(TableViewer viewer, List<ViewerSorter> sorters) {
		super(viewer, sorters);

		Assert.isTrue(viewer.getTable().getColumnCount() == sorters.size());
		Iterator<ViewerSorter> iterator = sorters.iterator();
		for (TableColumn column : viewer.getTable().getColumns()) {
			SortListener listener = new SortListener(iterator.next());
			column.addSelectionListener(listener);
		}

		viewer.getTable().setSortDirection(SWT.UP);
	}

	@Override
	protected void indicateSortedColumn(SelectionEvent event) {
		TableColumn column = (TableColumn) event.widget;
		Table table = column.getParent();
		table.setSortColumn(column);
	}

}
