package org.cheetahplatform.modeler.graph.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.csv.AbstractProcessInformation;
import org.cheetahplatform.common.logging.csv.CsvWriter;
import org.cheetahplatform.common.logging.csv.IProcessInformation;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.11.2009
 */
public class CsvExporter extends AbstractExporter {
	private static class ExportInformation {
		private IProcessInformation information;
		private ExportType type;

		public ExportInformation(IProcessInformation information, ExportType type) {
			this.information = information;
			this.type = type;
		}

		public IProcessInformation getInformation() {
			return information;
		}

		public ExportType getType() {
			return type;
		}

	}

	private static enum ExportType {
		EXPERIMENTAL_PROCESS, MODELING_PROCESS;
	}

	private final class InitializeCsvWriterRunnable implements Runnable {
		private List<IProcessInformation> unwrapped;
		private CsvWriter csvWriter;

		public InitializeCsvWriterRunnable(List<IProcessInformation> unwrapped) {
			Assert.isNotNull(unwrapped);
			this.unwrapped = unwrapped;
		}

		protected CsvWriter getCsvWriter() {
			return csvWriter;
		}

		@Override
		public void run() {
			csvWriter = new CsvWriter(unwrapped);

			AttributeSelectionDialog attributeSelectionDialog = new AttributeSelectionDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), csvWriter.getAttributeNames());

			if (attributeSelectionDialog.open() == Window.OK) {
				boolean exportExperimentalProcesses = attributeSelectionDialog.isExportExperimentalProcesses();
				boolean exportModelingProcesses = attributeSelectionDialog.isExportModelingProcesses();

				unwrapped = new ArrayList<IProcessInformation>();
				for (ExportInformation wrapped : informations) {
					if (wrapped.getType().equals(ExportType.EXPERIMENTAL_PROCESS) && exportExperimentalProcesses) {
						unwrapped.add(wrapped.getInformation());
					}
					if (wrapped.getType().equals(ExportType.MODELING_PROCESS) && exportModelingProcesses) {
						unwrapped.add(wrapped.getInformation());
					}
				}
			}

			csvWriter = new CsvWriter(unwrapped);
			csvWriter.setAttributeNames(attributeSelectionDialog.getSelectedAttributes());
		}
	}

	private List<ExportInformation> informations;
	private File target;

	public CsvExporter() {
		informations = new ArrayList<ExportInformation>();
	}

	@Override
	protected void doExportAuditTrailEntry(AuditTrailEntry entry) {
		IProcessInformation information = informations.get(informations.size() - 1).getInformation();
		information.addAttributes(entry.getAttributes());

		List<IExportComputation> computations = getComputations();
		for (IExportComputation computation : computations) {
			information.addAttributes(computation.computeForAuditTrailEntry(entry));
		}
	}

	@Override
	protected void doExportExperimentalProcessInstance(ProcessInstanceDatabaseHandle instance) {
		boolean aggregateChoiceValues = CheetahPlatformConfigurator.getBoolean(IConfiguration.AGGREGATE_CHOICE_VALUES);

		ModelerProcessInformation information = new ModelerProcessInformation(instance.getInstance(), aggregateChoiceValues);
		informations.add(new ExportInformation(information, ExportType.EXPERIMENTAL_PROCESS));

		List<IExportComputation> computations = getComputations();
		for (IExportComputation computation : computations) {
			information.addAttributes(computation.computeForExperimentalProcessInstance(instance.getInstance()));
		}
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		ProcessInstance modelingInstance = handle.getInstance();
		boolean aggregateChoiceValues = CheetahPlatformConfigurator.getBoolean(IConfiguration.AGGREGATE_CHOICE_VALUES);
		AbstractProcessInformation information = new ModelerProcessInformation(modelingInstance, aggregateChoiceValues);
		informations.add(new ExportInformation(information, ExportType.MODELING_PROCESS));

		List<IExportComputation> computations = getComputations();
		for (IExportComputation computation : computations) {
			information.addAttributes(computation.computeForModelingProcessInstance(handle));
		}
	}

	@Override
	public void exportFinished() {
		List<IProcessInformation> unwrapped = new ArrayList<IProcessInformation>();
		for (ExportInformation wrapped : informations) {
			unwrapped.add(wrapped.getInformation());
		}

		InitializeCsvWriterRunnable runnable = new InitializeCsvWriterRunnable(unwrapped);
		Display.getDefault().syncExec(runnable);

		try {
			runnable.getCsvWriter().write(target, null, true);
		} catch (IOException e) {
			Activator.logError("Error when exporting to CSV.", e);
		}

		super.exportFinished();
	}

	@SuppressWarnings("unchecked")
	public List<IExportComputation> getComputations() {
		List<IExportComputation> computations = (List<IExportComputation>) CheetahPlatformConfigurator
				.getObject(IConfiguration.EXPORT_COMPUTATIONS);
		if (computations == null) {
			return Collections.emptyList();
		}
		return computations;
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}
}
