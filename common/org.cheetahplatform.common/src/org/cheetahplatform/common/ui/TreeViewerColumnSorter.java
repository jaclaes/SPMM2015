package org.cheetahplatform.common.ui;

import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class TreeViewerColumnSorter extends AbstractViewerColumnSorter {

	public static void addColumnSorter(TreeViewer viewer, List<ViewerSorter> sorters) {
		new TreeViewerColumnSorter(viewer, sorters);
	}

	private TreeViewerColumnSorter(TreeViewer viewer, List<ViewerSorter> sorters) {
		super(viewer, sorters);

		Assert.isTrue(viewer.getTree().getColumnCount() == sorters.size());
		Iterator<ViewerSorter> iterator = sorters.iterator();
		for (TreeColumn column : viewer.getTree().getColumns()) {
			SortListener listener = new SortListener(iterator.next());
			column.addSelectionListener(listener);
		}

		viewer.getTree().setSortDirection(SWT.UP);
	}

	@Override
	protected void indicateSortedColumn(SelectionEvent event) {
		TreeColumn column = (TreeColumn) event.widget;
		Tree tree = column.getParent();
		tree.setSortColumn(column);
	}

}
