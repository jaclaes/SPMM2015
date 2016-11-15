package at.component.util;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * This class can be used to resize the table columns of a table when the table is resized.<br>
 * If you want a column not to consume as little space as possible, set the data of the tableColumn to <code>TableColumnResizer.PACK</code>.<br>
 * All columns will take the same amount of space - except the last one: it fills the rest of the space.
 * 
 * @author Stefan Zugal (csae7511@uibk.ac.at)<br>
 * <br>
 *         Date: 10.05.2006
 */
public class TableColumnResizer extends ControlAdapter {
	/**
	 * Adds this tableResizer to given table
	 * 
	 * @param table
	 *            The table.
	 * @param widths
	 *            The widths in percent from 0 to 100.
	 * @param minWidths
	 *            The minimum widths of each column in pixels
	 * @return The created Resizer.
	 */
	public static TableColumnResizer addToTable(Table table, int[] widths, int[] minWidths) {
		return new TableColumnResizer(table, widths, minWidths);
	}

	protected Table table;

	protected int[] widths;

	protected int[] minWidths;

	protected TableColumnResizer(Table table, int[] widths, int[] minWidths) {
		this.table = table;
		this.widths = widths;
		this.minWidths = minWidths;

		table.addControlListener(this);
	}

	@Override
	public void controlResized(ControlEvent e) {
		TableColumn[] columns = table.getColumns();
		int totalWidth = table.getBounds().width;

		for (int i = 0; i < columns.length; i++) {
			float columnWidth = totalWidth * ((float) widths[i] / 100);
			if (i != columns.length - 1) {
				if (columnWidth < minWidths[i])
					columnWidth = minWidths[i];
				columns[i].setWidth((int) columnWidth);
			} else {
				if (columnWidth - 4 < minWidths[i])
					columnWidth = minWidths[i];
				columns[i].setWidth((int) columnWidth - 4);
			}
		}

	}

	/**
	 * Forces the resizer to resize to columns now.
	 */
	public void forceResizeColumns() {
		controlResized(null);
	}

	/**
	 * Removes the resizer from its table.
	 */
	public void removeFromTable() {
		table.removeControlListener(this);
	}

}