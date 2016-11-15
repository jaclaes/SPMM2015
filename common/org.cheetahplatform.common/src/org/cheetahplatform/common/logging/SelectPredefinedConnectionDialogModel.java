package org.cheetahplatform.common.logging;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.db.ConnectionSetting;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SelectPredefinedConnectionDialogModel {
	private static class ConnectionLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return ((ConnectionSetting) element).getDatabaseURL();
			}

			return "";
		}

	}

	public IBaseLabelProvider createLabelProvider() {
		return new ConnectionLabelProvider();
	}

	public Object getInput() {
		IDatabaseConnector connector = Activator.getDatabaseConnector();
		return connector.getPreconfiguredSettings();
	}

}
