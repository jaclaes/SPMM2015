package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

public class ModelingPauseExporter extends AbstractExporter {
	private class ModelingPause {
		private Date startTime;
		private Date endTime;
		private int nextStepIndex;

		public ModelingPause(Date startTime, Date endTime, int nextStepIndex) {
			super();
			this.startTime = startTime;
			this.endTime = endTime;
			this.nextStepIndex = nextStepIndex;
		}

		public long getDuration() {
			return endTime.getTime() - startTime.getTime();
		}

		public Date getEndTime() {
			return endTime;
		}

		public int getNextStepIndex() {
			return nextStepIndex;
		}

		public Date getStartTime() {
			return startTime;
		}
	}

	private static final long THRESH_HOLD = 20000;

	private File target;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		List<ModelingPause> pauses = new ArrayList<ModelingPause>();
		ProcessInstance modelingInstance = handle.getInstance();
		List<AuditTrailEntry> entries = modelingInstance.getEntries();
		Date begin = new Date(modelingInstance.getLongAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP));

		for (AuditTrailEntry currentEntry : entries) {
			Date time = currentEntry.getTimestamp();
			if (time.getTime() - begin.getTime() > THRESH_HOLD) {
				ModelingPause modelingPause = new ModelingPause(begin, time, entries.indexOf(currentEntry));
				pauses.add(modelingPause);
			}
			begin = time;
		}

		writeChunkFile(modelingInstance, pauses);
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

	private void writeChunkFile(ProcessInstance modelingInstance, List<ModelingPause> pauses) {
		try {
			File file = new File(target.getAbsolutePath() + "/" + modelingInstance.getId() + "_ModelingPauses.csv");
			if (file.exists()) {
				final String error = "Could not write file: " + file.getAbsolutePath();
				showErrorMessage(error);
				return;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Model id;" + modelingInstance.getId() + ";\n");

			writer.write("\n");

			writer.write("start;end;duration[ms];beforeStep\n");

			SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");

			for (ModelingPause pause : pauses) {
				Date startTime = pause.getStartTime();
				String startTimeString = formatter.format(startTime);
				Date endTime = pause.getEndTime();
				String endTimeString = "";
				if (endTime != null) {
					endTimeString = formatter.format(endTime);
				}
				StringBuilder builder = new StringBuilder();
				builder.append(startTimeString);
				builder.append(";");
				builder.append(endTimeString);
				builder.append(";");
				builder.append(pause.getDuration());
				builder.append(";");
				builder.append(pause.getNextStepIndex());
				builder.append(";\n");
				String string = builder.toString();
				writer.write(string);
			}
			writer.write("\n");

			writer.write("Process Instance Attributes;\n");
			List<Attribute> attributes = modelingInstance.getAttributes();
			for (Attribute attribute : attributes) {
				writer.write(attribute.getName() + ";" + attribute.getContent() + ";\n");
			}

			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error writing csv file for modeling process " + modelingInstance.getId() + " to " + target.getAbsolutePath());
		}
	}
}
