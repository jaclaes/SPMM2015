package org.cheetahplatform.modeler.graph.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.csv.CsvWriter;
import org.cheetahplatform.common.logging.csv.IProcessInformation;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class ChunkOverviewCsvExporter extends AbstractExporter {
	private List<IProcessInformation> information;
	private ModelingPhaseChunkExtractor chunkExtractor;
	private File target;
	private boolean keepSettings;

	public ChunkOverviewCsvExporter() {
		information = new ArrayList<IProcessInformation>();
		keepSettings = false;
	}

	@Override
	protected void doExportAuditTrailEntry(AuditTrailEntry entry) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				if (!keepSettings) {
					ModelingPhaseDiagramDialog dialog = new ModelingPhaseDiagramDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getShell());
					if (dialog.open() != Window.OK) {
						return;
					}
					chunkExtractor = new ModelingPhaseChunkExtractor(dialog.getModelingPhaseDetectionStrategy(), dialog
							.getComprehensionThreshold(), dialog.getComprehensionAggregationThreshold());
					keepSettings = dialog.isKeepSettings();
				}

			}
		});

	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		if (chunkExtractor == null) {
			return;
		}

		String id = handle.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS);

		if (!id.equals("modeling_task1_bpmn_1.0")) {
			return;
		}

		ChunkOverviewProcessInformation information2 = new ChunkOverviewProcessInformation(handle.getInstance(), chunkExtractor);
		information.add(information2);
	}

	@Override
	public void exportFinished() {
		final CsvWriter csvWriter = new CsvWriter(information);

		try {
			csvWriter.write(target, null, true);
		} catch (IOException e) {
			Activator.logError("Error when exporting to CSV.", e);
		}

		super.exportFinished();
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}
}
