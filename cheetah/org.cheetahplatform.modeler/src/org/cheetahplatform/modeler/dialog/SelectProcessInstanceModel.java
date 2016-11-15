package org.cheetahplatform.modeler.dialog;

import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_PROCESS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabasePromReader;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;

public class SelectProcessInstanceModel {
	private abstract static class AbstractProcessDatabaseHandleSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (!(e1 instanceof ProcessInstanceDatabaseHandle)) {
				return 0;
			}
			return doCompare(viewer, e1, e2);
		}

		protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
	}

	private static class DateSorter extends AbstractProcessDatabaseHandleSorter {
		@Override
		public int doCompare(Viewer viewer, Object e1, Object e2) {
			ProcessInstanceDatabaseHandle handle1 = (ProcessInstanceDatabaseHandle) e1;
			ProcessInstanceDatabaseHandle handle2 = (ProcessInstanceDatabaseHandle) e2;

			return handle1.compareCreationDate(handle2);
		}
	}

	private static class HostSorter extends AbstractProcessDatabaseHandleSorter {
		@Override
		public int doCompare(Viewer viewer, Object e1, Object e2) {
			ProcessInstanceDatabaseHandle handle1 = (ProcessInstanceDatabaseHandle) e1;
			ProcessInstanceDatabaseHandle handle2 = (ProcessInstanceDatabaseHandle) e2;

			return handle1.compareHost(handle2);
		}
	}

	private static class IdSorter extends AbstractProcessDatabaseHandleSorter {
		@Override
		public int doCompare(Viewer viewer, Object e1, Object e2) {
			ProcessInstanceDatabaseHandle handle1 = (ProcessInstanceDatabaseHandle) e1;
			ProcessInstanceDatabaseHandle handle2 = (ProcessInstanceDatabaseHandle) e2;

			return Collator.getInstance().compare(handle1.getId(), handle2.getId());
		}
	}

	private class ProcessInstanceDatabaseHandleFilter extends ViewerFilter {

		String getProcessId(ProcessInstanceDatabaseHandle handle) {
			String id = handle.getAttribute(ATTRIBUTE_PROCESS);
			if (!id.isEmpty()) {
				return id;
			}

			return handle.getProcessId();
		}

		protected boolean matches(Date creationDate) {
			if (from == null || to == null) {
				return true;
			}
			if (creationDate == null) {
				return from == null && to == null;
			}

			TimeSlot slot = new TimeSlot(new DateTime(from, true), new DateTime(to, false));
			return slot.includes(new DateTime(creationDate, true));
		}

		protected boolean matches(String value, String filter) {
			if (filter == null) {
				return true;
			}
			if (value == null) {
				return filter.trim().length() == 0;
			}

			return value.contains(filter);
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return selectProcessInstanceDatabaseHandle(element);
		}

		private boolean selectProcessInstanceDatabaseHandle(Object element) {
			if (!(element instanceof ProcessInstanceDatabaseHandle)) {
				return true;
			}

			ProcessInstanceDatabaseHandle handle = (ProcessInstanceDatabaseHandle) element;
			if (!matches(handle.getId(), id)) {
				return false;
			}
			if (!matches(handle.getHost(), host)) {
				return false;
			}
			if (!matches(getProcessId(handle), process)) {
				return false;
			}
			if (!matches(handle.getTimeAsDate())) {
				return false;
			}

			return true;
		}
	}

	private static class ProcessSorter extends AbstractProcessDatabaseHandleSorter {
		@Override
		public int doCompare(Viewer viewer, Object e1, Object e2) {
			ProcessInstanceDatabaseHandle handle1 = (ProcessInstanceDatabaseHandle) e1;
			ProcessInstanceDatabaseHandle handle2 = (ProcessInstanceDatabaseHandle) e2;

			String process1 = handle1.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
			String process2 = handle2.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
			if (process1 == null) {
				return -1;
			}
			if (process2 == null) {
				return 1;
			}

			return Collator.getInstance().compare(process1, process2);
		}
	}

	private class SelectProcessInstanceContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			ProcessInstanceDatabaseHandle experimentalWorkflowHandle = (ProcessInstanceDatabaseHandle) parentElement;
			List<ExperimentalWorkflowElementDatabaseHandle> allHandles = loadChildren(experimentalWorkflowHandle);

			return allHandles.toArray();
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof ProcessInstanceDatabaseHandle;
		}
	}

	public static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private final IExtraColumnProvider extraColumnProvider;
	private String id;
	private String host;
	private String process;
	private Date from;
	private Date to;

	private List<ProcessInstanceDatabaseHandle> allHandles;
	private List<String> ignoredProcesses;
	private List<String> includedProceses;

	public SelectProcessInstanceModel(IExtraColumnProvider extraColumnProvider) {
		this.extraColumnProvider = extraColumnProvider;
		this.ignoredProcesses = new ArrayList<String>();
		this.includedProceses = new ArrayList<String>();

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		from = loadDate(CommonConstants.FILTER_PROCESS_INSTANCE_FROM);
		to = loadDate(CommonConstants.FILTER_PROCESS_INSTANCE_TO);
		id = store.getString(CommonConstants.FILTER_PROCESS_INSTANCE_ID);
		host = store.getString(CommonConstants.FILTER_PROCESS_INSTANCE_HOST);
		process = store.getString(CommonConstants.FILTER_PROCESS_INSTANCE_PROCESS);
	}

	public void addIgnoredProcess(String processId) {
		Assert.isTrue(includedProceses.isEmpty());
		ignoredProcesses.add(processId);
	}

	/**
	 * Add the given process to be included, no other processes with different ids are selected. Use either this method or
	 * {@link #addIgnoredProcess(String)}. By default all processes are selected.
	 * 
	 * @param process
	 *            the process
	 */
	public void addIncludedProcess(String process) {
		Assert.isTrue(ignoredProcesses.isEmpty());
		includedProceses.add(process);
	}

	private String assembleIgnoredProcesses() {
		StringBuilder query = new StringBuilder();
		List<String> toProcess = includedProceses;

		if (!ignoredProcesses.isEmpty()) {
			query.append(" not in");
			toProcess = ignoredProcesses;
		} else {
			query.append(" in");
		}

		query.append(" (");
		boolean first = true;

		for (String current : toProcess) {
			if (!first) {
				query.append(", ");
			}

			try {
				query.append(DatabasePromReader.getProcessDatabaseId(current, Activator.getDatabaseConnector().getDatabaseConnection()));
			} catch (SQLException e) {
				Activator.logError("Could not assemble a query.", e);
				if (!first) {
					query.replace(query.length() - 2, query.length(), "");
				}
			}

			first = false;
		}

		query.append(")");
		return query.toString();
	}

	public IContentProvider createContentProvider() {
		return new SelectProcessInstanceContentProvider();
	}

	/**
	 * @param filter
	 */
	public ViewerFilter[] createFilter() {
		return new ViewerFilter[] { new ProcessInstanceDatabaseHandleFilter() };
	}

	public IBaseLabelProvider createLabelProvider() {
		return new ProcessInstanceDatabaseHandleLabelProvider(extraColumnProvider);
	}

	public List<ViewerSorter> createSorters() {
		List<ViewerSorter> sorters = new ArrayList<ViewerSorter>();
		sorters.add(new DateSorter());
		sorters.add(new IdSorter());
		sorters.add(new HostSorter());
		sorters.add(new ProcessSorter());
		sorters.addAll(extraColumnProvider.createSorters());

		return sorters;
	}

	public void dispose() {

	}

	/**
	 * This methods converts the {@link ExperimentalWorkflowElementDatabaseHandle} into a {@link ProcessInstanceDatabaseHandle}. For this
	 * purpose the attribute {@link CommonConstants#ATTRIBUTE_PROCESS_INSTANCE} has to be defined!
	 * 
	 * @param handle
	 *            the {@link ExperimentalWorkflowElementDatabaseHandle}
	 * @return the corresponding {@link ProcessInstanceDatabaseHandle} or <code>null</code> if the {@link ProcessInstanceDatabaseHandle}
	 *         could not be loaded.
	 */
	public ProcessInstanceDatabaseHandle getDatabaseHandle(ExperimentalWorkflowElementDatabaseHandle handle) {
		try {
			PreparedStatement statement = Activator
					.getDatabaseConnector()
					.getDatabaseConnection()
					.prepareStatement(
							"select i.database_id, i.id, i.data, p.id from process_instance i, process p where i.process = p.database_id and i.id=?");
			statement.setString(1, handle.getAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE));
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				long databaseId = resultSet.getLong(1);
				String id = resultSet.getString(2);
				String attributes = resultSet.getString(3);
				String processId = resultSet.getString(4);
				statement.close();

				return new ProcessInstanceDatabaseHandle(databaseId, id, attributes, processId);
			}

			statement.close();
		} catch (SQLException e) {
			Activator.logError("Failed to extract process instance from experimental workflow element", e);
		}

		return null;
	}

	public Map<String, Integer> getExtraColumns() {
		return extraColumnProvider.getExtraColumns();
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the process
	 */
	public String getProcess() {
		return process;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * Load all handles from the database.
	 * 
	 * @return all handles
	 */
	public List<ProcessInstanceDatabaseHandle> loadAllProcessInstances() {
		if (allHandles != null) {
			return allHandles;
		}

		try {
			Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
			PreparedStatement statement = connection
					.prepareStatement("select i.database_id, i.id, i.data, p.id from process_instance i, process p where i.process = p.database_id and p.database_id"
							+ assembleIgnoredProcesses());
			ResultSet resultSet = statement.executeQuery();
			allHandles = new ArrayList<ProcessInstanceDatabaseHandle>();
			while (resultSet.next()) {
				long databaseId = resultSet.getLong(1);
				String id = resultSet.getString(2);
				String attributes = resultSet.getString(3);
				String processId = resultSet.getString(4);

				ProcessInstanceDatabaseHandle handle = new ProcessInstanceDatabaseHandle(databaseId, id, attributes, processId);
				allHandles.add(handle);
			}

			statement.close();
			return allHandles;
		} catch (SQLException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not load the process instances.", e);
			Activator.getDefault().getLog().log(status);
			return Collections.emptyList();
		}
	}

	public List<ExperimentalWorkflowElementDatabaseHandle> loadChildren(ProcessInstanceDatabaseHandle experimentalWorkflowHandle) {
		List<ExperimentalWorkflowElementDatabaseHandle> allHandles = new ArrayList<ExperimentalWorkflowElementDatabaseHandle>();

		try {
			Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
			PreparedStatement loadChildrenStatement = connection
					.prepareStatement("SELECT a.database_id, a.type, a.data, a.timestamp FROM audittrail_entry a where process_instance=?;");
			loadChildrenStatement.setLong(1, experimentalWorkflowHandle.getDatabaseId());
			ResultSet resultSet = loadChildrenStatement.executeQuery();

			while (resultSet.next()) {
				long databaseId = resultSet.getLong(1);
				String type = resultSet.getString(2);
				String attributes = resultSet.getString(3);
				long timestamp = resultSet.getLong(4);

				ExperimentalWorkflowElementDatabaseHandle handle = new ExperimentalWorkflowElementDatabaseHandle(databaseId,
						experimentalWorkflowHandle, timestamp, type, attributes);
				allHandles.add(handle);
			}

			resultSet.close();
			loadChildrenStatement.close();
		} catch (SQLException e) {
			Activator.logError("Failed to fetch children for experimental workflow: " + experimentalWorkflowHandle.getId(), e);
		}

		return allHandles;
	}

	private Date loadDate(String key) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String fromString = store.getString(key);

		if (fromString.length() != 0) {
			try {
				return FORMAT.parse(fromString);
			} catch (ParseException e) {
				// ignore
			}
		}

		return null;
	}

	/**
	 * Load the process instance describing the given graph.
	 * 
	 * @param handle
	 *            the handle
	 * @return the corresponding process instance
	 * @throws Exception
	 */
	public ProcessInstance loadProcessInstance(ProcessInstanceDatabaseHandle handle) throws Exception {
		return DatabasePromReader.readProcessInstance(handle.getDatabaseId(), Activator.getDatabaseConnector().getDatabaseConnection());
	}

	private void save(String key, Date date) {
		if (date == null) {
			save(key, (String) null);
		} else {
			save(key, FORMAT.format(date));
		}
	}

	private void save(String key, String value) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		if (value == null) {
			store.setToDefault(key);
		} else {
			store.putValue(key, value);
		}
	}

	public void saveFilters() {
		save(CommonConstants.FILTER_PROCESS_INSTANCE_FROM, from);
		save(CommonConstants.FILTER_PROCESS_INSTANCE_TO, to);
		save(CommonConstants.FILTER_PROCESS_INSTANCE_HOST, host);
		save(CommonConstants.FILTER_PROCESS_INSTANCE_ID, id);
		save(CommonConstants.FILTER_PROCESS_INSTANCE_PROCESS, process);
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param process
	 *            the process to set
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

}
