package org.cheetahplatform.modeler.graph.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cheetahplatform.common.Activator;

public class ExperimentalWorkflowIdentifier {
	private PreparedStatement statement;

	public String getWorkflowId(String id) {
		try {
			if (statement == null) {
				initializeStatement();
			}
			statement.setString(1, "%PROCESS_INSTANCE" + id);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			String workflowId = resultSet.getString(1);
			resultSet.close();
			return workflowId;
		} catch (SQLException e) {
			Activator.logError(e.getMessage(), e);
		}

		return null;
	}

	private void initializeStatement() throws SQLException {
		statement = Activator
				.getDatabaseConnector()
				.getDatabaseConnection()
				.prepareStatement(
						"SELECT xp.id FROM process_instance xp, audittrail_entry a where a.process_instance=xp.database_id and a.data like ?;");
	}
}
