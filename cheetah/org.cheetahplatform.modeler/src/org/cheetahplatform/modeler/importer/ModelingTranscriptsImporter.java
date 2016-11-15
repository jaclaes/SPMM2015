package org.cheetahplatform.modeler.importer;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.model.ModelingTranscript;

import com.ibm.icu.util.Calendar;

public class ModelingTranscriptsImporter {

	private Date getAbsoluteTime(ProcessInstanceDatabaseHandle instance, String timestamp) {
		Date processStartTime = instance.getTimeAsDate();
		String[] split = timestamp.split(":");
		int hours = Integer.parseInt(split[0]);
		int minutes = Integer.parseInt(split[1]);
		int seconds = Integer.parseInt(split[2]);

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(processStartTime);
		calendar.add(Calendar.HOUR, hours);
		calendar.add(Calendar.MINUTE, minutes);
		calendar.add(Calendar.SECOND, seconds);

		Date transcriptDate = calendar.getTime();
		return transcriptDate;
	}

	public void importTranscripts(ProcessInstanceDatabaseHandle processInstance, File transcriptsFile) {
		List<ModelingTranscript> transcripts;
		try {
			transcripts = readModelingTranscripts(processInstance, transcriptsFile);
			for (ModelingTranscript modelingTranscript : transcripts) {
				storeTranscript(processInstance, modelingTranscript);
			}
		} catch (Exception e) {
			Activator.logError("Unable to store transcript.", e);
		}
	}

	protected List<ModelingTranscript> readModelingTranscripts(ProcessInstanceDatabaseHandle processInstance, File transcriptsFile)
			throws Exception {
		List<ModelingTranscript> transcripts = new ArrayList<ModelingTranscript>();
		CSVReader csvReader = new CSVReader(new FileInputStream(transcriptsFile));
		List<CSVLine> lines = csvReader.read();
		for (CSVLine line : lines) {
			if (line.size() < 4) {
				continue;
			}

			String startTime = line.get(0).trim();
			String endTime = line.get(1).trim();
			String originator = line.get(2).trim();
			String text = line.get(3).trim();
			if (startTime.isEmpty()) {
				continue;
			}

			if (endTime.isEmpty()) {
				int index = lines.indexOf(line);
				if (index >= lines.size() - 1) {
					continue;
				}

				CSVLine nextLine = lines.get(index + 1);
				endTime = nextLine.get(0).trim();
			}

			if (originator.isEmpty()) {
				continue;
			}
			if (text.isEmpty()) {
				continue;
			}

			Date startDate;
			Date endDate;
			try {
				startDate = getAbsoluteTime(processInstance, startTime);
				endDate = getAbsoluteTime(processInstance, endTime);
			} catch (Exception e) {
				// unable to parse - skip line
				continue;
			}
			ModelingTranscript transcript = new ModelingTranscript(startDate, endDate, originator, text);
			transcripts.add(transcript);
		}

		return transcripts;
	}

	private void storeTranscript(ProcessInstanceDatabaseHandle processInstance, ModelingTranscript modelingTranscript) throws SQLException {
		Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
		String sql = "insert into transcript (process_instance, startTime, endTime, originator, text) values (?, ?, ?, ?, ?);";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, processInstance.getDatabaseId());
		statement.setString(2, String.valueOf(modelingTranscript.getStartTime().getTime()));
		statement.setString(3, String.valueOf(modelingTranscript.getEndTime().getTime()));
		statement.setString(4, String.valueOf(modelingTranscript.getOriginator()));
		statement.setString(5, String.valueOf(modelingTranscript.getText()));
		statement.execute();
	}
}
