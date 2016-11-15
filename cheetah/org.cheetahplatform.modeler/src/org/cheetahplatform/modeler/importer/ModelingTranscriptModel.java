package org.cheetahplatform.modeler.importer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.model.ModelingTranscript;

public class ModelingTranscriptModel {

	private List<ModelingTranscript> transcripts;
	private PreparedStatement updateStatement;

	public ModelingTranscriptModel(ProcessInstanceDatabaseHandle processInstanceDatabaseHandle) throws SQLException {
		initializeTranscripts(processInstanceDatabaseHandle);
		updateStatement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
				.prepareStatement("UPDATE transcript SET startTime=?, endTime=?, originator=?, text=? WHERE database_id=?");
	}

	public void changeOffset(int seconds) throws SQLException {
		for (ModelingTranscript transcript : transcripts) {
			transcript.changeTranscriptOffset(seconds);
			saveTranscript(transcript);
		}
	}

	public List<ModelingTranscript> getModelingTranscripts() {
		return transcripts;
	}

	private void initializeTranscripts(ProcessInstanceDatabaseHandle processInstance) throws SQLException {
		if (transcripts == null) {
			transcripts = new ArrayList<ModelingTranscript>();
			PreparedStatement statement = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection()
					.prepareStatement("select startTime, endTime, originator, text, database_id from transcript where process_instance=?");
			statement.setLong(1, processInstance.getDatabaseId());

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String startTime = resultSet.getString(1);
				String endTime = resultSet.getString(2);
				String originator = resultSet.getString(3);
				String text = resultSet.getString(4);
				long id = resultSet.getLong(5);
				ModelingTranscript transcript = new ModelingTranscript(new Date(Long.parseLong(startTime)), new Date(
						Long.parseLong(endTime)), originator, text, id);
				transcripts.add(transcript);
			}
			resultSet.close();

			sortTranscripts();
		}
	}

	public void saveTranscript(ModelingTranscript transcript) throws SQLException {
		updateStatement.setString(1, String.valueOf(transcript.getStartTime().getTime()));
		updateStatement.setString(2, String.valueOf(transcript.getEndTime().getTime()));
		updateStatement.setString(3, transcript.getOriginator());
		updateStatement.setString(4, transcript.getText());
		updateStatement.setLong(5, transcript.getDatabaseId());

		updateStatement.execute();
	}

	public void sortTranscripts() {
		Collections.sort(transcripts, new Comparator<ModelingTranscript>() {
			@Override
			public int compare(ModelingTranscript o1, ModelingTranscript o2) {
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
		});
	}
}
