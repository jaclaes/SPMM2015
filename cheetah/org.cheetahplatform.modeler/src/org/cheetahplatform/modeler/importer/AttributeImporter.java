package org.cheetahplatform.modeler.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.db.DatabaseUtil;

public class AttributeImporter {
	private Map<String, String> modelToAttribute;

	public void importAttributes(File attributeFile, String attributeId) {
		try {
			modelToAttribute = new HashMap<String, String>();
			readCsvFile(attributeFile);
			Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
			PreparedStatement queryDataStatement = connection.prepareStatement("select data from process_instance where id=?;");
			PreparedStatement updateDataStatement = connection.prepareStatement("UPDATE process_instance SET data=? WHERE id=?;");

			Set<Entry<String, String>> entrySet = modelToAttribute.entrySet();
			for (Entry<String, String> entry : entrySet) {
				queryDataStatement.setString(1, entry.getKey());
				ResultSet result = queryDataStatement.executeQuery();
				result.next();
				String oldData = result.getString(1);
				result.close();

				Attribute attribute = new Attribute(attributeId, entry.getValue());
				String toAppend = DatabaseUtil.toDatabaseRepresentation(attribute);

				updateDataStatement.setString(1, oldData + toAppend);
				updateDataStatement.setString(2, entry.getKey());
				updateDataStatement.execute();
			}

		} catch (Exception e) {
			Activator.logError("Unable to store transcript.", e);
		}
	}

	private void readCsvFile(File attributeFile) throws FileNotFoundException, IOException {
		CSVReader csvReader = new CSVReader(new FileInputStream(attributeFile));
		List<CSVLine> lines = csvReader.read();
		for (CSVLine line : lines) {
			String modelId = line.get(0).trim();
			String attributeValue = line.get(1).trim();
			if (modelId.isEmpty()) {
				continue;
			}
			if (attributeValue.isEmpty()) {
				continue;
			}

			if (modelToAttribute.containsKey(modelId)) {
				throw new IllegalArgumentException("There are multiple entries for id: " + modelId);
			}

			modelToAttribute.put(modelId, attributeValue);
		}
	}
}
