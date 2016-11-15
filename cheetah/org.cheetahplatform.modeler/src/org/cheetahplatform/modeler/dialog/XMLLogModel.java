package org.cheetahplatform.modeler.dialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class XMLLogModel {
	private class XMLLogContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		@SuppressWarnings("unchecked")
		@Override
		public Object[] getChildren(Object elements) {
			return ((List<XMLLog>) elements).toArray();
		}

		@Override
		public Object getParent(Object arg0) {
			return null;
		}

		@Override
		public boolean hasChildren(Object arg0) {
			return false;
		}

	}

	private class XMLLogLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int index) {
			if (index == 0) {
				return String.valueOf(((XMLLog) element).getId());
			} else if (index == 1) {
				return String.valueOf(((XMLLog) element).getTimestamp());
			}
			throw new IllegalArgumentException("Illegal column index.");
		}
	}

	private List<XMLLog> logs;

	public XMLLogModel() throws Exception {
		logs = new ArrayList<XMLLog>();

		Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select database_id, timestamp from xml_log");
		while (resultSet.next()) {
			int id = resultSet.getInt(1);
			Timestamp timestamp = resultSet.getTimestamp(2);
			XMLLog xmlLog = new XMLLog(id, timestamp);
			logs.add(xmlLog);
		}

		statement.close();
	}

	public Object[] getCheckedElements() {
		List<XMLLog> checked = new ArrayList<XMLLog>();
		for (XMLLog xmlLog : logs) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(xmlLog.getTimestamp());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			if (year == 2011 && month == Calendar.OCTOBER && day == 21) {
				checked.add(xmlLog);
			}
		}

		return checked.toArray();
	}

	public IContentProvider getContentProvider() {
		return new XMLLogContentProvider();
	}

	public IBaseLabelProvider getLabelProvider() {
		return new XMLLogLabelProvider();
	}

	public List<XMLLog> getLogs() {
		return logs;
	}
}
