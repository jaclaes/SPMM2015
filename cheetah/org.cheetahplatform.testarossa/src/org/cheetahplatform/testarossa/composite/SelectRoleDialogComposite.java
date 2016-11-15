package org.cheetahplatform.testarossa.composite;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class SelectRoleDialogComposite extends Composite {
	private Table table;
	private TableViewer rolesTable;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public SelectRoleDialogComposite(Composite parent, int style) {
		super(parent, style);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout = new GridLayout(1, false);

		setLayout(gridLayout);
		{
			Composite composite = new Composite(this, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			GridLayout gridLayout_1 = new GridLayout(1, false);
			gridLayout_1.marginWidth = 0;
			gridLayout_1.marginHeight = 0;

			TableColumnLayout columnLayout = new TableColumnLayout();
			composite.setLayout(columnLayout);

			{
				rolesTable = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				table = rolesTable.getTable();
				table.setHeaderVisible(false);
				table.setLinesVisible(true);
				GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
				layoutData.minimumHeight = 100;
				table.setLayoutData(layoutData);

				TableColumn column = new TableColumn(table, SWT.NONE);
				columnLayout.setColumnData(column, new ColumnWeightData(100));
			}
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the rolesTable
	 */
	public TableViewer getRolesTable() {
		return rolesTable;
	}

}
