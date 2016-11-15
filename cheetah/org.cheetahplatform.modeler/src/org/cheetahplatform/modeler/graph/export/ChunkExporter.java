package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.dialog.ProcessOfProcessModelingIterationDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ChunkExporter extends AbstractExporter {
	private IChunkExtractor extractor;
	private boolean keepSettings = false;

	private File target;
	private double comprehensionPenalty;
	private double modelingPenalty;
	private double reconciliationPenalty;
	private boolean keepIterationSettings = false;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		ProcessInstance modelingInstance = handle.getInstance();

		// if (!handle.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS).equals("modeling_task1_bpmn_1.0")) {
		// return;
		// }

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				if (!keepSettings) {
					ModelingPhaseDiagramDialog dialog = new ModelingPhaseDiagramDialog(shell);
					if (dialog.open() != Window.OK) {
						return;
					}
					extractor = new ModelingPhaseChunkExtractor(dialog.getModelingPhaseDetectionStrategy(), dialog
							.getComprehensionThreshold(), dialog.getComprehensionAggregationThreshold());
					keepSettings = dialog.isKeepSettings();

				}
				if (!keepIterationSettings) {
					ProcessOfProcessModelingIterationDialog iterationDialog = new ProcessOfProcessModelingIterationDialog(shell);
					if (iterationDialog.open() != Window.OK) {
						keepSettings = false;
						return;
					}

					comprehensionPenalty = iterationDialog.getComprehensionPenalty();
					modelingPenalty = iterationDialog.getModelingPenalty();
					reconciliationPenalty = iterationDialog.getReconciliationPenalty();
					keepIterationSettings = iterationDialog.isKeepSettings();
				}
			}
		});

		List<Chunk> chunks = extractor.extractChunks(modelingInstance);
		ProcessOfProcessModelingIterationsExtractor iterationsExtractor = new ProcessOfProcessModelingIterationsExtractor();
		List<ProcessOfProcessModelingIteration> iterations = iterationsExtractor.extractIterations(chunks);
		writeChunkFile(modelingInstance, chunks, iterations);
	}

	private String formatDuration(long duration) {
		long minutes = duration / 1000 / 60;
		long seconds = duration / 1000 % 60;
		return String.valueOf(minutes + ":" + seconds);
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

	public void writeAttributes(ProcessInstance modelingInstance, BufferedWriter writer) throws IOException {
		writer.write("\n\n");
		writer.write("Process Instance Attributes;\n");
		List<Attribute> attributes = modelingInstance.getAttributes();
		for (Attribute attribute : attributes) {
			writer.write(attribute.getName() + ";" + attribute.getContent() + ";\n");
		}
	}

	@SuppressWarnings("unused")
	private void writeChunkFile(ProcessInstance modelingInstance, List<Chunk> chunks, List<ProcessOfProcessModelingIteration> iterations) {
		try {
			File file = new File(getPathToFile(target, modelingInstance, "csv"));
			if (file.exists()) {
				final String error = "Could not write file: " + file.getAbsolutePath();
				showErrorMessage(error);
				return;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Model id;" + modelingInstance.getId() + ";\n");

			writer.write("\n");

			List<String> additionalInformation = extractor.getAdditionalInformation();
			if (additionalInformation != null && !(additionalInformation.isEmpty())) {
				for (String info : additionalInformation) {
					writer.write(info);
					writer.write("\n");
				}
			}

			writer.write("chunkType;relative start;relative end;duration[mm:ss]\n");

			for (Chunk chunk : chunks) {
				Date startTime = chunk.getStartTime();
				String startTimeString = getTimeRelativeToStartTime(startTime, modelingInstance);
				Date endTime = chunk.getEndTime();
				String endTimeString = "";
				if (endTime != null) {
					endTimeString = getTimeRelativeToStartTime(endTime, modelingInstance);
				}
				StringBuilder builder = new StringBuilder();
				builder.append(chunk.getType());
				// builder.append(";");
				// builder.append(chunk.getSize());
				builder.append(";");
				builder.append(startTimeString);
				builder.append(";");
				builder.append(endTimeString);
				builder.append(";");
				builder.append(formatDuration(chunk.getDuration()));
				// builder.append(";");
				// builder.append(chunk.getFromStepIndex());
				// builder.append(";");
				// builder.append(chunk.getToStepIndex());
				builder.append(";\n");
				String string = builder.toString();
				writer.write(string);
			}
			writer.write("\n");

			// writeTotalTime(chunks, writer);
			// writePPMIterations(iterations, writer);
			// writeAttributes(modelingInstance, writer);

			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error writing csv file for modeling process " + modelingInstance.getId() + " to " + target.getAbsolutePath());
		}
	}

	public void writePPMIterations(List<ProcessOfProcessModelingIteration> iterations, BufferedWriter writer) throws IOException {
		writer.write("Iterations of the Process of Process Modeling;\n");
		writer.write("duration;numberOfAddedElements;numberOfDeletedElement;numberOfReconnectEdgeEvents;numberOfReconciliationEvents;penalty;phases;\n");

		double totalPenalty = 0;
		StringBuilder iterationsBuilder = new StringBuilder();
		for (ProcessOfProcessModelingIteration iteration : iterations) {
			iterationsBuilder.append(iteration.getDuration());
			iterationsBuilder.append(";");
			iterationsBuilder.append(iteration.numberOfAddedElements());
			iterationsBuilder.append(";");
			iterationsBuilder.append(iteration.numberOfRemovedElements());
			iterationsBuilder.append(";");
			iterationsBuilder.append(iteration.numberOfReconnectElements());
			iterationsBuilder.append(";");
			iterationsBuilder.append(iteration.numberOfReconciliationElements());
			iterationsBuilder.append(";");
			double penalty = iteration.calcualtePenalty(comprehensionPenalty, modelingPenalty, reconciliationPenalty);
			totalPenalty += penalty;
			iterationsBuilder.append(String.valueOf(penalty).replaceAll("\\.", ","));
			iterationsBuilder.append(";");
			iterationsBuilder.append(iteration.getDescription());
			iterationsBuilder.append(";\n");
		}

		writer.write(iterationsBuilder.toString());
		writer.write("\n");

		writer.write("Number of Iterations;" + iterations.size() + ";\n");
		writer.write("Total Penalty;" + String.valueOf(totalPenalty).replaceAll("\\.", ",") + ";\n");
	}

	public void writeTotalTime(List<Chunk> chunks, BufferedWriter writer) throws IOException {
		long totalComprehensionTime = 0;
		int comprehensionCounter = 0;
		long totalModelingTime = 0;
		int modelingCounter = 0;
		long totalReconciliationTime = 0;
		int reconciliationCounter = 0;
		for (Chunk chunk : chunks) {
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				totalComprehensionTime += chunk.getDuration();
				comprehensionCounter++;
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.MODELING)) {
				totalModelingTime += chunk.getDuration();
				modelingCounter++;
				continue;
			}
			if (chunk.getType().equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
				totalReconciliationTime += chunk.getDuration();
				reconciliationCounter++;
				continue;
			}
			throw new IllegalStateException("Illegal type of phase" + chunk.getType());
		}

		StringBuilder overviewBuilder = new StringBuilder();
		overviewBuilder.append(";Comprehension;Modeling;Reconciliation;\n");
		overviewBuilder.append("Total Duration;");
		overviewBuilder.append(totalComprehensionTime);
		overviewBuilder.append(";");
		overviewBuilder.append(totalModelingTime);
		overviewBuilder.append(";");
		overviewBuilder.append(totalReconciliationTime);
		overviewBuilder.append(";\n");
		overviewBuilder.append("# Phases;");
		overviewBuilder.append(comprehensionCounter);
		overviewBuilder.append(";");
		overviewBuilder.append(modelingCounter);
		overviewBuilder.append(";");
		overviewBuilder.append(reconciliationCounter);
		overviewBuilder.append(";\n");
		writer.write(overviewBuilder.toString());

		writer.write("\n");
	}
}
