package org.cheetahplatform.common.ui.dialog;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ViewerSorter;

public class DefaultExtraColumnProvider implements IExtraColumnProvider {

	@Override
	public List<ViewerSorter> createSorters() {
		return Collections.emptyList();
	}

	@Override
	public String getColumnText(ProcessInstanceDatabaseHandle handle, int columnIndex) {
		return "";
	}

	@Override
	public Map<String, Integer> getExtraColumns() {
		return Collections.emptyMap();
	}

}
