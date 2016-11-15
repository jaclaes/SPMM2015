package org.cheetahplatform.modeler.graph.dialog;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class StepsViewComposite extends Composite {
	private Tree table;
	private TreeViewer commandsViewer;
	private TreeColumn tableViewerColumn;
	private Composite composite_1;
	private TreeColumn tableViewerColumn2;
	private TreeColumn tableViewerColumn3;
	private TreeColumn trclmnTimestamp;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public StepsViewComposite(Composite parent, int style) {
		super(parent, style);

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		{
			composite_1 = new Composite(this, SWT.NONE);
			composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			TreeColumnLayout layout = new TreeColumnLayout();
			composite_1.setLayout(layout);
			{
				commandsViewer = new TreeViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
				table = commandsViewer.getTree();
				table.setLinesVisible(true);
				table.setHeaderVisible(true);
				{
					tableViewerColumn = new TreeColumn(table, SWT.NONE);
					tableViewerColumn.setText("#");
					layout.setColumnData(tableViewerColumn, new ColumnWeightData(10));
				}
				{
					trclmnTimestamp = new TreeColumn(table, SWT.NONE);
					layout.setColumnData(trclmnTimestamp, new ColumnPixelData(150, true, true));
					trclmnTimestamp.setText("Timestamp");
					layout.setColumnData(trclmnTimestamp, new ColumnWeightData(20));
				}
				{
					tableViewerColumn2 = new TreeColumn(table, SWT.NONE);
					tableViewerColumn2.setText("Event");
					layout.setColumnData(tableViewerColumn2, new ColumnWeightData(45));
				}
				{
					tableViewerColumn3 = new TreeColumn(table, SWT.NONE);
					tableViewerColumn3.setText("Executed");
					layout.setColumnData(tableViewerColumn3, new ColumnWeightData(26));
				}
			}
		}
		new Label(this, SWT.NONE);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * @return the commandsViewer
	 */
	public TreeViewer getCommandsViewer() {
		return commandsViewer;
	}
}
