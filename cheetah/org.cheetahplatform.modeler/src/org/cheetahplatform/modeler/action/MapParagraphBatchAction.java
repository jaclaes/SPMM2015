package org.cheetahplatform.modeler.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.DataContainer;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.SelectProcessDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class MapParagraphBatchAction extends Action {
	private static class LoadInstancesRunnable implements IRunnableWithProgress {

		private ArrayList<ProcessInstanceInformation> instances;

		public ArrayList<ProcessInstanceInformation> getInstances() {
			return instances;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Loading instances... (please stand by, this may take some time)", IProgressMonitor.UNKNOWN);

			try {
				Statement statement = Activator.getDatabaseConnector().getDatabaseConnection().createStatement();
				ResultSet resultSet = statement
						.executeQuery("select top_instance.id, top_instance.database_id, top_instance.data from process_instance top_instance where top_instance.data like '%4,4,typeBPMN%' and not exists (select * from audittrail_entry entry, paragraph_mapping mapping where entry.process_instance = top_instance.database_id and mapping.audittrail_entry = entry.database_id);");
				// ResultSet resultSet = statement
				// .executeQuery("select top_instance.id, top_instance.database_id, top_instance.data from process_instance top_instance where top_instance.data like '%4,4,typeBPMN%' and top_instance.id>375 and not exists (select * from audittrail_entry entry, paragraph_mapping mapping where entry.process_instance = top_instance.database_id and mapping.audittrail_entry = entry.database_id);");
				// // ResultSet resultSet = statement
				// .executeQuery("select top_instance.id, top_instance.database_id, top_instance.data from process_instance top_instance where top_instance.data like '%4,4,typeBPMN%' and top_instance.id>3000;");
				// ResultSet resultSet = statement
				// .executeQuery("select top_instance.id, top_instance.database_id, top_instance.data from process_instance top_instance where top_instance.data like '%4,4,typeBPMN%';");

				instances = new ArrayList<ProcessInstanceInformation>();
				while (resultSet.next()) {
					String id = resultSet.getString(1);
					long databaseId = resultSet.getLong(2);
					String data = resultSet.getString(3);

					if (!validateId(id)) {
						continue;
					}

					instances.add(new ProcessInstanceInformation(databaseId, id, data));
					if (monitor.isCanceled()) {
						throw new InterruptedException();
					}
				}
				statement.close();
			} catch (SQLException e) {
				throw new InvocationTargetException(e);
			}
		}

		private boolean validateId(String id) throws SQLException {
			Statement statement = Activator.getDatabaseConnector().getDatabaseConnection().createStatement();
			String sql = "SELECT count(*) FROM audittrail_entry entry where entry.data like ''%16,{0},PROCESS_INSTANCE{1}%'';";
			sql = MessageFormat.format(sql, id.length(), id);
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			int count = resultSet.getInt(1);
			statement.close();

			return count == 1;
		}

	}

	private static class ProcessInstanceInformation {
		private long databaseId;
		private String id;
		private DataContainer data;

		public ProcessInstanceInformation(long databaseId, String id, String data) {
			this.databaseId = databaseId;
			this.id = id;
			this.data = new DataContainer();
			List<Attribute> converted = DatabaseUtil.fromDataBaseRepresentation(data);
			this.data.addAttributes(converted);
		}

		public long getDatabaseId() {
			return databaseId;
		}

		public String getId() {
			return id;
		}

		public String getProcess() {
			return data.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		}

		public boolean isProcessDefined() {
			return data.isAttributeDefined(CommonConstants.ATTRIBUTE_PROCESS);
		}

	}

	public static final String ID = "org.cheetahplatform.modeler.action.MapParagraphBatchAction";

	public MapParagraphBatchAction() {
		setId(ID);
		setText("Map Model Elements to Paragraphs (Batch Version)");
	}

	protected List<ProcessInstanceInformation> loadInstances() {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		LoadInstancesRunnable runnable = new LoadInstancesRunnable();
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			Activator.logError("Could not load the instances", e);
		} catch (InterruptedException e) {
			return null;
		}

		return runnable.getInstances();
	}

	@Override
	public void run() {
		try {
			List<ProcessInstanceInformation> instances = loadInstances();
			if (instances == null) {
				return;
			}

			Set<String> processes = new HashSet<String>();
			for (ProcessInstanceInformation current : instances) {
				if (current.isProcessDefined()) {
					processes.add(current.getProcess());
				}
			}

			SelectProcessDialog dialog = new SelectProcessDialog(Display.getDefault().getActiveShell(), new ArrayList<String>(processes));
			if (dialog.open() != Window.OK) {
				return;
			}

			List<String> toFilter = dialog.getSelected();
			MapParagraphAction action = new MapParagraphAction();
			for (ProcessInstanceInformation current : instances) {
				if (!current.isProcessDefined() || !toFilter.contains(current.getProcess())) {
					continue;
				}

				ProcessInstance instance = DatabasePromReader.readProcessInstance(current.getDatabaseId());
				ProcessInstanceDatabaseHandle handle = new ProcessInstanceDatabaseHandle(current.getDatabaseId(), current.getId(),
						instance.getAttributes(), "");
				handle.setInstance(instance);
				if (!MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Ready to Map?", "Now mapping: " + instance.getId()
						+ "\n\nContinue?")) {
					break;
				}

				action.doRun(handle, null);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
			}
		} catch (SQLException e) {
			Activator.logError("Could not execute a query.", e);
		}

	}

}
