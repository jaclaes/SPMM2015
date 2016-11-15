package org.cheetahplatform.modeler.graph.export;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.mapping.ParagraphProvider;

public class ParagraphMappingExporter extends AbstractMXMLExporter {

	private List<String> emptyProcessInstances;

	public ParagraphMappingExporter() {
		emptyProcessInstances = new ArrayList<String>();
	}

	@Override
	protected void exportToMXML(IPromWriter writer, ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		writer.append(ExperimentalWorkflowEngine.PARAGRAPH_MAPPING_PROCESS, handle.getInstance());

		try {
			String query = "select database_id from audittrail_entry where process_instance = ? order by database_id asc";
			PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement(query);
			statement.setLong(1, handle.getDatabaseId());
			ResultSet resultSet = statement.executeQuery();

			String mappingQuery = "select paragraph from paragraph_mapping where audittrail_entry = ?";
			PreparedStatement mappingStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement(mappingQuery);

			int mappedParagraphs = 0;
			while (resultSet.next()) {
				long databaseId = resultSet.getLong(1);
				mappingStatement.setLong(1, databaseId);
				ResultSet mappedParagraph = mappingStatement.executeQuery();

				if (mappedParagraph.next()) {
					long paragraphId = mappedParagraph.getLong(1);
					Paragraph paragraph = ParagraphProvider.getParagraph(paragraphId);
					String description = paragraph.getDescription();
					writer.append(new AuditTrailEntry(description, description));
					mappedParagraphs++;
				}
			}

			if (mappedParagraphs == 0) {
				emptyProcessInstances.add(String.valueOf(handle.getDatabaseId()));
			}

			mappingStatement.close();
			statement.close();
		} catch (SQLException e) {
			Activator.logError("Could not load the audit trail entries.", e);
		}
	}

	public List<String> getEmptyProcessInstances() {
		return emptyProcessInstances;
	}

}
