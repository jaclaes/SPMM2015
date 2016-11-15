package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

import com.ibm.icu.text.SimpleDateFormat;

public class PhaseSimilarityExporter extends AbstractModelingPhaseExporter {

	private File target;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		super.doExportModelingProcessInstance(modelingInstance, entry);

		// PhaseSimilarityCalculator calculator = new PhaseSimilarityCalculator(new AverageIterationDurationSimilarityCalculationStrategy(
		// getIterations(), 2000));
		PhaseSimilarityCalculator calculator = new PhaseSimilarityCalculator(new FixedDurationsPhaseSimilarityCalculationStrategy(10000,
				10000));
		List<SlidingWindow> slidingWindows = calculator.calculate(modelingInstance.getInstance(), getChunks(), getIterations());

		writeFile(modelingInstance.getInstance(), slidingWindows);
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

	private void writeFile(ProcessInstance modelingInstance, List<SlidingWindow> slidingWindows) {
		try {
			File file = new File(getPathToFile(target, modelingInstance, "csv"));
			if (file.exists()) {
				final String error = "Could not write file: " + file.getAbsolutePath();
				showErrorMessage(error);
				return;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Model id;" + modelingInstance.getId() + ";\n");
			if (!slidingWindows.isEmpty()) {
				SlidingWindow slidingWindow = slidingWindows.get(0);
				long duration = slidingWindow.getDuration() / 1000;
				writer.write("Window Duration;" + (duration / 60) + ":" + (duration % 60) + ";\n");
			}

			writer.write("\n");
			writer.write("Starttime;Endtime;Start #Elements;End #Elements;Element Difference;Chunks;# iterations;#Added;#Removed;#Layout;ComprehensionTime;audittrailentries\n");

			StringBuilder builder = new StringBuilder();
			for (SlidingWindow slidingWindow : slidingWindows) {
				builder.append(dateFormat.format(new Date(slidingWindow.getStartTimestamp())));
				builder.append(";");
				builder.append(dateFormat.format(new Date(slidingWindow.getEndTimestamp())));
				builder.append(";");
				builder.append(slidingWindow.getNumberOfStartElements());
				builder.append(";");
				builder.append(slidingWindow.getNumberOfEndElements());
				builder.append(";");
				builder.append(slidingWindow.getNumberOfEndElements() - slidingWindow.getNumberOfStartElements());
				builder.append(";");
				List<Chunk> chunks = slidingWindow.getChunks();
				for (Chunk chunk : chunks) {
					builder.append(chunk.getType() + " ");
				}

				builder.append(";");
				builder.append(slidingWindow.getIterations().size());
				builder.append(";");
				builder.append(slidingWindow.getNumberOfAddEvents());
				builder.append(";");
				builder.append(slidingWindow.getNumberOfDeleteEvents());
				builder.append(";");
				builder.append(slidingWindow.getNumberOfLayoutEvents());
				builder.append(";");
				builder.append(slidingWindow.getTimeOfComprehension());
				builder.append(";");
				List<AuditTrailEntry> entries = slidingWindow.getEntries();
				for (AuditTrailEntry auditTrailEntry : entries) {
					long startTimestamp = slidingWindow.getStartTimestamp();
					long time = auditTrailEntry.getTimestamp().getTime();
					builder.append("[");
					builder.append(time - startTimestamp);
					builder.append(",");
					builder.append(auditTrailEntry.getEventType());
					builder.append("]");
					builder.append(" ");
				}

				builder.append(";\n");
			}

			writer.write(builder.toString());
			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error writing csv file for modeling process " + modelingInstance.getId() + " to " + target.getAbsolutePath());
		}
	}
}
