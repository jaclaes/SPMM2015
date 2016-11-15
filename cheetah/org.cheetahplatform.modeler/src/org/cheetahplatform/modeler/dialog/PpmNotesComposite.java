package org.cheetahplatform.modeler.dialog;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class PpmNotesComposite extends Composite {

	private TreeViewer ppmNotesTreeViewer;

	public PpmNotesComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		ppmNotesTreeViewer = new TreeViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = ppmNotesTreeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(ppmNotesTreeViewer, SWT.NONE);
		TreeColumn trclmnComment = treeViewerColumn.getColumn();
		trclmnComment.setWidth(200);
		trclmnComment.setText("Comment");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(ppmNotesTreeViewer, SWT.NONE);
		TreeColumn trclmnStarttime = treeViewerColumn_1.getColumn();
		trclmnStarttime.setWidth(100);
		trclmnStarttime.setText("Starttime");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(ppmNotesTreeViewer, SWT.NONE);
		TreeColumn trclmnEndtime = treeViewerColumn_2.getColumn();
		trclmnEndtime.setWidth(100);
		trclmnEndtime.setText("Endtime");

		TreeColumn trclmnCategory = new TreeColumn(tree, SWT.NONE);
		trclmnCategory.setWidth(100);
		trclmnCategory.setText("Category");

		TreeColumn trclmnOriginator = new TreeColumn(tree, SWT.NONE);
		trclmnOriginator.setWidth(100);
		trclmnOriginator.setText("Originator");
	}

	protected TreeViewer getPpmNotesTreeViewer() {
		return ppmNotesTreeViewer;
	}
}
