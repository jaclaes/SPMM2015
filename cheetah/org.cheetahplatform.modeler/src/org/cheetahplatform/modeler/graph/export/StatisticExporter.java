package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.dialog.StatisticSelectionDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class StatisticExporter extends AbstractModelingPhaseExporter {

	private BufferedWriter writer;
	private List<IPpmStatistic> statistics;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		super.doExportModelingProcessInstance(modelingInstance, entry);
		try {
			writer.write(String.valueOf(modelingInstance.getId()) + ";" + getProcessId() + ";");
			for (IPpmStatistic statistic : statistics) {
				writer.write(String.valueOf(statistic.getValue(modelingInstance.getInstance(), getChunks(), getIterations())));
				writer.write(";");
			}
			writer.write(";\n");
		} catch (IOException e) {
			showErrorMessage("Error writing csv file for modeling process " + modelingInstance.getId());
		}
	}

	@Override
	public void exportFinished() {
		try {
			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error closing csv writer");
		}
		super.exportFinished();
	}

	@Override
	public void initializeExport(File target) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				StatisticSelectionDialog dialog = new StatisticSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell());
				dialog.open();
				statistics = dialog.getSelectedStatistics();
			}
		});

		try {
			File file = new File(target.getAbsolutePath());
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("Model id;Process;");
			for (IPpmStatistic statistic : statistics) {
				writer.write(statistic.getHeader());
				writer.write(";");
			}
			writer.write(";\n");
		} catch (IOException e) {
			showErrorMessage("Error writing csv file");
		}
	}
}
