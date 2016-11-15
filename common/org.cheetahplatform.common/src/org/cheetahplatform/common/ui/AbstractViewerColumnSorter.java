package org.cheetahplatform.common.ui;

import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public abstract class AbstractViewerColumnSorter {

	protected class SortListener extends SelectionAdapter {
		private ViewerSorter sorter;

		public SortListener(ViewerSorter sorter) {
			this.sorter = sorter;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			viewer.setSorter(sorter);
		}
	}

	protected final StructuredViewer viewer;

	protected AbstractViewerColumnSorter(StructuredViewer viewer, List<ViewerSorter> sorters) {
		this.viewer = viewer;
		this.viewer.setSorter(sorters.get(0));
	}

	/**
	 * A column has been selected for sorting, mark it accordingly.
	 * 
	 * @param event
	 *            the selection event
	 */
	protected abstract void indicateSortedColumn(SelectionEvent event);
}
