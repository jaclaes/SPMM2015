/**
 * 
 */
package org.cheetahplatform.testarossa.composite;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.07.2009
 */
public class ReminderComposite extends Composite {

	private TableViewer reminderViewer;

	public ReminderComposite(Composite parent, int style) {
		super(parent, style);
		TableColumnLayout layout = new TableColumnLayout();
		setLayout(layout);
		reminderViewer = new TableViewer(this, SWT.FULL_SELECTION);
		Table table = reminderViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TableColumn nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setText("Name");
		TableColumn descriptionColumn = new TableColumn(table, SWT.NONE);
		descriptionColumn.setText("Role");
		TableColumn timeColumn = new TableColumn(table, SWT.NONE);
		timeColumn.setText("Time");

		layout.setColumnData(nameColumn, new ColumnWeightData(40));
		layout.setColumnData(descriptionColumn, new ColumnWeightData(20));
		layout.setColumnData(timeColumn, new ColumnWeightData(20));
	}

	/**
	 * Returns the reminderViewer.
	 * 
	 * @return the reminderViewer
	 */
	public TableViewer getReminderViewer() {
		return reminderViewer;
	}
}
