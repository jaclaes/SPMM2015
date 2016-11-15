package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.dialog.ProcessOfProcessModelingIterationDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class IterationExporter extends AbstractExporter {

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
		writeChunkFile(modelingInstance, iterations);
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

	private void writeChunkFile(ProcessInstance modelingInstance, List<ProcessOfProcessModelingIteration> iterations) {
		try {
			File file = new File(target.getAbsolutePath() + "/" + modelingInstance.getId() + ".csv");
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
			writer.write("\n");

			writer.write("Phases;Duration;# Added Elements;# Deleted Elements;# Reconnect Elements;# Reconciliation Elements;penalty;\n");

			StringBuilder builder = new StringBuilder();
			for (ProcessOfProcessModelingIteration iteration : iterations) {
				builder.append(iteration.getDescription());
				builder.append(";");
				builder.append(iteration.getDuration());
				builder.append(";");
				builder.append(iteration.numberOfAddedElements());
				builder.append(";");
				builder.append(iteration.numberOfRemovedElements());
				builder.append(";");
				builder.append(iteration.numberOfReconnectElements());
				builder.append(";");
				builder.append(iteration.numberOfReconciliationElements());
				builder.append(";");
				builder.append(iteration.calcualtePenalty(comprehensionPenalty, modelingPenalty, reconciliationPenalty));
				builder.append(";\n");
			}

			writer.write(builder.toString());
			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error writing csv file for modeling process " + modelingInstance.getId() + " to " + target.getAbsolutePath());
		}
	}
}
