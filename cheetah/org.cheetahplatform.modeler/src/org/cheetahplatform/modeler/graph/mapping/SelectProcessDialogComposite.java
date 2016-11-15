package org.cheetahplatform.modeler.graph.mapping;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class SelectProcessDialogComposite extends Composite {
	private Table table;
	private CheckboxTableViewer viewer;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectProcessDialogComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);

		viewer = CheckboxTableViewer.newCheckList(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(250);
		tableColumn.setText("New Column");
	}

	public CheckboxTableViewer getViewer() {
		return viewer;
	}

}
