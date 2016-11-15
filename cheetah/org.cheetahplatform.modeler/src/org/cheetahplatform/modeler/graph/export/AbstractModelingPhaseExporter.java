package org.cheetahplatform.modeler.graph.export;

import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractModelingPhaseExporter extends AbstractExporter {
	class InitializationRunnable implements Runnable {
		boolean initialized;

		protected boolean isInitialized() {
			return initialized;
		}

		@Override
		public void run() {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			if (!keepSettings) {
				ModelingPhaseDiagramDialog dialog = new ModelingPhaseDiagramDialog(shell);
				if (dialog.open() != Window.OK) {
					initialized = false;
					return;
				}
				extractor = new ModelingPhaseChunkExtractor(dialog.getModelingPhaseDetectionStrategy(), dialog.getComprehensionThreshold(),
						dialog.getComprehensionAggregationThreshold());
				keepSettings = dialog.isKeepSettings();

			}
			initialized = true;
		}
	}

	private boolean keepSettings = false;
	private ModelingPhaseChunkExtractor extractor;
	private String processId;
	private List<Chunk> chunks;
	private List<ProcessOfProcessModelingIteration> iterations;

	public AbstractModelingPhaseExporter() {
		super();
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		ProcessInstance instance = modelingInstance.getInstance();
		processId = instance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		if (!initalizeExtractor()) {
			return;
		}

		chunks = extractor.extractChunks(instance);
		ProcessOfProcessModelingIterationsExtractor iterationsExtractor = new ProcessOfProcessModelingIterationsExtractor();
		iterations = iterationsExtractor.extractIterations(chunks);
	}

	protected List<Chunk> getChunks() {
		return chunks;
	}

	protected List<ProcessOfProcessModelingIteration> getIterations() {
		return iterations;
	}

	protected String getProcessId() {
		return processId;
	}

	protected boolean initalizeExtractor() {
		InitializationRunnable runnable = new InitializationRunnable();
		Display.getDefault().syncExec(runnable);
		return runnable.isInitialized();
	}
}