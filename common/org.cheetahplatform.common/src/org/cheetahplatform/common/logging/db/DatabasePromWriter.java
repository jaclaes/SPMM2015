package org.cheetahplatform.common.logging.db;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DatabasePromWriter implements IPromWriter {
	public static final String DATABASE_TABLE_PROCESS = "process";
	public static final String DATABASE_TABLE_PROCESS_INSTANCE = "process_instance";
	public static final String DATABASE_TABLE_AUDITTRAIL_ENTRY = "audittrail_entry";
	public static final String DATABASE_COLUMN_DATA = "data";
	public static final String DATABASE_COLUMN_ID = "id";
	public static final String DATABASE_COLUMN_DATABASE_ID = "database_id";
	public static final String DATABASE_COLUMN_WORKFLOW_ELEMENT = "workflow_element";
	public static final String DATABASE_COLUMN_TYPE = "type";
	public static final String DATABASE_COLUMN_PROCESS = "process";
	public static final String DATABASE_COLUMN_PROCESS_INSTANCE = "process_instance";

	private long processInstanceId;
	private PreparedStatement insertEntryStatement;

	public DatabasePromWriter() {
		this(0);
	}

	/**
	 * Instantiate a new writer with given id, allows to continue an existing log.
	 * 
	 * @param processInstanceId
	 *            the database id of the process instance to be continued
	 */
	public DatabasePromWriter(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Override
	public IStatus append(AuditTrailEntry entry) {
		Assert.isTrue(processInstanceId != 0);

		prepareInsertEntryStatement();
		try {
			if (insertEntryStatement == null || insertEntryStatement.getConnection() == null
					|| insertEntryStatement.getConnection().isClosed()) {
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not establish the database connection.");
			}
		} catch (SQLException e1) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not establish the database connection.");
		}

		try {
			insertEntryStatement.setLong(1, processInstanceId);
			insertEntryStatement.setLong(2, entry.getTimestamp().getTime());
			insertEntryStatement.setString(3, entry.getEventType());
			insertEntryStatement.setString(4, entry.getWorkflowModelElement());
			if (entry.getOriginator() == null) {
				insertEntryStatement.setNull(5, Types.VARCHAR);
			} else {
				insertEntryStatement.setString(5, entry.getOriginator());
			}
			insertEntryStatement.setString(6, DatabaseUtil.toDatabaseRepresentation(entry.getAttributes()));

			insertEntryStatement.execute();
		} catch (SQLException e) {
			Activator.logError("Could not log a log entry.", e);
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not log a log entry.", e);
			return status;
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus append(Process process, ProcessInstance instance) {
		prepareInsertEntryStatement();

		try {
			if (insertEntryStatement == null || insertEntryStatement.getConnection().isClosed()) {
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not establish the database connection.");
			}
		} catch (SQLException e1) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not establish the database connection.");
		}

		try {
			Long processId = DatabasePromReader.getProcessDatabaseId(process, Activator.getDatabaseConnector().getDatabaseConnection());
			if (processId == null) {
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "No process with ID: " + process.getId());
			}

			String data = DatabaseUtil.toDatabaseRepresentation(instance.getAttributes());
			String insertStatement = "insert into process_instance (process, " + DATABASE_COLUMN_ID + ", " + DATABASE_COLUMN_DATA
					+ ") values (?, ?, ?)";
			PreparedStatement statement = insertEntryStatement.getConnection().prepareStatement(insertStatement, RETURN_GENERATED_KEYS);
			statement.setLong(1, processId);
			statement.setString(2, instance.getId());
			statement.setString(3, data);
			statement.execute();

			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			processInstanceId = keys.getLong(1);
			statement.close();
		} catch (SQLException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Couldn't write first log entry.", e);
		}

		return Status.OK_STATUS;
	}

	@Override
	public void close() {
		try {
			if (insertEntryStatement != null)
				insertEntryStatement.close();
		} catch (SQLException e) {
			Activator.logError("Could not close a statement.", e);
		}
	}

	@Override
	public String getProcessInstanceId() {
		return String.valueOf(processInstanceId);
	}

	private void prepareInsertEntryStatement() {
		try {
			if (insertEntryStatement == null || insertEntryStatement.getConnection() == null
					|| insertEntryStatement.getConnection().isClosed()) {
				Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
				connection.setAutoCommit(true);

				String statement = "insert into audittrail_entry (process_instance, timestamp, type, workflow_element, originator, "
						+ DATABASE_COLUMN_DATA + ") values (?, ?, ?, ?, ?, ?)";
				insertEntryStatement = connection.prepareStatement(statement);
			}
		} catch (SQLException e) {
			Activator.logError("Could not establish the database connection.", e);
		}
	}
}
