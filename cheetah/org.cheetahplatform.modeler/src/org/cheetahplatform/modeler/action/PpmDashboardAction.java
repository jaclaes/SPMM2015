package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.PpmDashboardPerspective;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.dialog.GraphReplayAdvisor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class PpmDashboardAction extends AbstractReplayAction {

	public static final String ID = "org.cheetahplatform.modeler.action.ReplayGraphAction";
	private Graph graph;
	private IWorkbenchWindow workbenchWindow;

	public PpmDashboardAction() {
		setId(ID);
		setText("PPM Dashboard");
	}

	@Override
	protected void doRun(ProcessInstanceDatabaseHandle handle, ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle) {
		super.doRun(handle, experimentalWorkflowElementHandle);

		GraphCommandStack stack = new GraphReplayAdvisor().initialize(handle);
		ReplayModelProvider.getInstance().initializeModel(stack, handle);
		this.graph = stack.getGraph();

		try {
			workbenchWindow = PlatformUI.getWorkbench().openWorkbenchWindow(PpmDashboardPerspective.ID, null);
		} catch (WorkbenchException e) {
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
		if (workbenchWindow.getShell() != null && !workbenchWindow.getShell().isDisposed()) {
			workbenchWindow.close();
		}
		super.replayFinished();
	}
}
