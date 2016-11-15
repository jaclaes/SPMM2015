package org.cheetahplatform.modeler.graph.dialog;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class PpmIterationsComposite extends Composite {

	private TreeViewer phaseTreeViewer;

	public PpmIterationsComposite(Composite parent, int style) {
		super(parent, style);
		TreeColumnLayout layout = new TreeColumnLayout();
		setLayout(layout);

		phaseTreeViewer = new TreeViewer(this, SWT.BORDER);
		Tree tree = phaseTreeViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(phaseTreeViewer, SWT.NONE);
		TreeColumn trclmnPhase = treeViewerColumn.getColumn();
		trclmnPhase.setResizable(false);
		trclmnPhase.setText("Phase");
		layout.setColumnData(trclmnPhase, new ColumnWeightData(100));
	}

	/**
	 * @return the phaseTreeViewer
	 */
	public TreeViewer getPhaseTreeViewer() {
		return phaseTreeViewer;
	}

}
