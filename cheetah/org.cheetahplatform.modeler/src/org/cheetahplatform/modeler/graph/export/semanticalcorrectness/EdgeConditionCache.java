package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.EdgeConditionProvider;
import org.cheetahplatform.modeler.graph.model.Edge;

public class EdgeConditionCache {
	private Map<Long, EdgeCondition> mapping;

	public EdgeConditionCache(ProcessInstanceDatabaseHandle handle) {
		this.mapping = new HashMap<Long, EdgeCondition>();

		try {
			PreparedStatement statement = Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("select edge, edge_condition from edge_condition_mapping where process_instance = ?;");
			statement.setLong(1, handle.getDatabaseId());
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				long edgeId = resultSet.getLong("edge");
				long edgeConditionId = resultSet.getLong("edge_condition");

				EdgeCondition edgeCondition = EdgeConditionProvider.getEdgeCondition(edgeConditionId);
				mapping.put(edgeId, edgeCondition);
			}

			statement.close();
		} catch (SQLException e) {
			Activator.logError("Could not load the mappings.", e);
		}
	}

	public EdgeCondition getCondition(Edge edge) {
		return mapping.get(edge.getId());
	}
}
