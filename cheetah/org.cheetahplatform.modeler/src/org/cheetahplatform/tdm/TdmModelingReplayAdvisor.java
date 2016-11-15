package org.cheetahplatform.tdm;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.IReplayAdvisor;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerView;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class TdmModelingReplayAdvisor implements IReplayAdvisor {

	private IPerspectiveDescriptor previousPerspective;
	private IPromLogger logger;

	public TdmModelingReplayAdvisor() {
		this(new PromLogger());
	}

	public TdmModelingReplayAdvisor(IPromLogger logger) {
		this.logger = logger;
	}

	@Override
	public void dispose() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(previousPerspective);
		activePage.closeAllEditors(false);

		TDMModelingActivity.cleanUpTDM();
	}

	@Override
	public GraphCommandStack initialize(ProcessInstance instance) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		previousPerspective = activePage.getPerspective();

		long tdmProcessId = instance.getLongAttribute(TDMConstants.ATTRIBUTE_TDM_PROCESS_ID);
		TDMProcess process = TDMModelingActivity.getProcess(tdmProcessId, logger);
		String processId = instance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		Process experimentalProcess = ProcessRepository.getProcess(processId);
		logger.append(experimentalProcess, new ProcessInstance());
		TDMCommand.setProcess(process);
		TDMCommand.setLogger(logger);
		TDMModelingActivity.addTestEditorListener(logger);
		TDMDeclarativeModelerView modelerView = (TDMDeclarativeModelerView) activePage.findView(TDMDeclarativeModelerView.ID);

		return (GraphCommandStack) modelerView.getViewer().getViewer().getEditDomain().getCommandStack();
	}

	@Override
	public GraphCommandStack initialize(ProcessInstanceDatabaseHandle handle) {
		ProcessInstance instance = handle.getInstance();
		return initialize(instance);
	}
}
