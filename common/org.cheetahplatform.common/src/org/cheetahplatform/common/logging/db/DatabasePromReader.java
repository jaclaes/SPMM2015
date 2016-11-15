package org.cheetahplatform.common.logging.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.DatabaseAuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;

public class DatabasePromReader {
	private static void appendAuditTrailEntries(long databaseId, ProcessInstance instance, Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("select timestamp, type, workflow_element, originator, data, database_id from audittrail_entry where process_instance = "
						+ databaseId + " order by database_id");

		while (resultSet.next()) {
			long timestamp = resultSet.getLong(1);
			String type = resultSet.getString(2);
			String workflowElement = resultSet.getString(3);
			String originator = resultSet.getString(4);
			String rawData = resultSet.getString(5);
			List<Attribute> data = DatabaseUtil.fromDataBaseRepresentation(rawData);

			long id = resultSet.getLong(6);

			AuditTrailEntry entry = new DatabaseAuditTrailEntry(new Date(timestamp), type, workflowElement, originator, data, id);
			instance.addEntry(entry);
		}

		statement.close();
	}

	public static long getProcessDatabaseId(Process process, Connection connection) throws SQLException {
		return getProcessDatabaseId(process.getId(), connection);
	}

	public static long getProcessDatabaseId(String processId, Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("select database_id from process where id = ?");
		statement.setString(1, processId);
		ResultSet result = statement.executeQuery();

		if (!result.next()) {
			PreparedStatement insertProcessStatement = connection.prepareStatement("insert into process (id) values (?);");
			insertProcessStatement.setString(1, processId);
			insertProcessStatement.execute();
			insertProcessStatement.close();
			statement.close();

			return getProcessDatabaseId(processId, connection);
		}

		long id = result.getLong(1);
		statement.close();

		return id;
	}

	public static long getProcessInstanceDatabaseId(ProcessInstance instance, Connection connection) throws SQLException {
		return getProcessInstanceDatabaseId(instance.getId(), connection);
	}

	public static long getProcessInstanceDatabaseId(String processInstanceId, Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("select " + DatabasePromWriter.DATABASE_COLUMN_DATABASE_ID + " from "
				+ DatabasePromWriter.DATABASE_TABLE_PROCESS_INSTANCE + " where " + DatabasePromWriter.DATABASE_COLUMN_ID + " = ?");
		statement.setString(1, processInstanceId);
		ResultSet result = statement.executeQuery();
		result.next();
		long id = result.getLong(1);
		statement.close();

		return id;
	}

	public static ProcessInstance readProcessInstance(long databaseId) throws SQLException {
		return readProcessInstance(databaseId, Activator.getDatabaseConnector().getDatabaseConnection());
	}

	public static ProcessInstance readProcessInstance(long databaseId, Connection connection) throws SQLException {
		ProcessInstance instance = new ProcessInstance();
		readProcessInstanceData(databaseId, instance, connection);
		appendAuditTrailEntries(databaseId, instance, connection);

		return instance;
	}

	private static void readProcessInstanceData(long databaseId, ProcessInstance instance, Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select data, id from process_instance where database_id=" + databaseId);

		resultSet.next();
		String data = resultSet.getString(1);
		List<Attribute> attributes = DatabaseUtil.fromDataBaseRepresentation(data);
		instance.addAttributes(attributes);
		instance.setId(resultSet.getString(2));

		statement.close();
	}

}
