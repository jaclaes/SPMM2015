package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.common.ui.CustomCheckboxTreeViewer;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class SelectCommandsComposite extends Composite {

	private CheckboxTreeViewer commandsViewer;

	public SelectCommandsComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite_2 = new Composite(this, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TreeColumnLayout tcl_composite_2 = new TreeColumnLayout();
		composite_2.setLayout(tcl_composite_2);

		commandsViewer = new CustomCheckboxTreeViewer(composite_2, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Tree tree = commandsViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(commandsViewer, SWT.NONE);
		TreeColumn treeColumn = treeViewerColumn.getColumn();
		tcl_composite_2.setColumnData(treeColumn, new ColumnWeightData(20, true));
		treeColumn.setText("#");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(commandsViewer, SWT.NONE);
		TreeColumn trclmnNewColumn = treeViewerColumn_1.getColumn();
		tcl_composite_2.setColumnData(trclmnNewColumn, new ColumnWeightData(20, true));
		trclmnNewColumn.setText("Timestamp");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(commandsViewer, SWT.NONE);
		TreeColumn trclmnEvent = treeViewerColumn_2.getColumn();
		tcl_composite_2.setColumnData(trclmnEvent, new ColumnWeightData(60, true));
		trclmnEvent.setText("Event");
	}

	/**
	 * @return the commandsViewer
	 */
	public CheckboxTreeViewer getCommandsViewer() {
		return commandsViewer;
	}

}
