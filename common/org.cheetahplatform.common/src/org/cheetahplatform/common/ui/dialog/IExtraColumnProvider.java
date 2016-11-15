package org.cheetahplatform.common.ui.dialog;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ViewerSorter;

public interface IExtraColumnProvider {

	/**
	 * Create additional sorters for the provided columns.
	 * 
	 * @return the sorters
	 */
	List<ViewerSorter> createSorters();

	/**
	 * Determine the column text for given model and index.
	 * 
	 * @param handle
	 *            the model
	 * @param columnIndex
	 *            the column index
	 * @return the text
	 */
	String getColumnText(ProcessInstanceDatabaseHandle handle, int columnIndex);

	/**
	 * Return the extra columns provided.
	 * 
	 * @return the extra columns, a mapping of name to weight of column width
	 */
	Map<String, Integer> getExtraColumns();

}
