package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.literatemodeling.LiterateModelingActivity;
import org.cheetahplatform.literatemodeling.LiterateReplayAdvisor;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.dialog.GraphReplayAdvisor;
import org.cheetahplatform.modeler.graph.dialog.IReplayAdvisor;
import org.cheetahplatform.modeler.graph.dialog.ReplayView;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.tdm.TdmModelingReplayAdvisor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;

public class ReplayAction extends AbstractReplayAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ReplayGraphAction";

	private Graph graph;

	private ReplayView replayView;

	private IReplayAdvisor advisor;

	public ReplayAction() {
		super();

		setId(ID);
		setText("Replay");
	}

	@Override
	protected void doRun(ProcessInstanceDatabaseHandle handle, ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle) {
		super.doRun(handle, experimentalWorkflowElementHandle);
		advisor = null;
		if (experimentalWorkflowElementHandle.getType().equals(TDMModelingActivity.TDM_MODELING)) {
			advisor = new TdmModelingReplayAdvisor();
		} else if (experimentalWorkflowElementHandle.getType().equals(LiterateModelingActivity.ID)) {
			advisor = new LiterateReplayAdvisor();
		} else {
			advisor = new GraphReplayAdvisor();
		}
		GraphCommandStack stack = advisor.initialize(handle);
		ReplayModelProvider.getInstance().initializeModel(stack, handle);
		this.graph = stack.getGraph();

		try {
			replayView = (ReplayView) PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().showView(ReplayView.ID);
			WorkbenchPage workbenchPage = (WorkbenchPage) replayView.getViewSite().getWorkbenchWindow().getActivePage();

			// workbenchPage.getActivePerspective().getPresentation()
			// .detachPart(replayView.getSite().getPage().findViewReference(ReplayView.ID));

			replayView.getViewSite().getShell().setSize(722, 200);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected Graph getGraph() {
		return graph;
	}

	@Override
	protected void replayFinished() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];

		// workbench may be closing already
		if (window != null && window.getActivePage() != null) {
			window.getActivePage().hideView(replayView);
		}

		if (advisor != null) {
			advisor.dispose();
		}

		super.replayFinished();
	}
}
