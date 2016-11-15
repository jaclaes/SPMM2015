package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.dialog.DemoReplayDialog;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.GraphReplayAdvisor;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.dialog.ReplayView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class DemoReplayAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.action.DemoReplayAction";

	public DemoReplayAction() {
		setId(ID);
		setText("Replay Process of Process Modeling");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/16-arrow-next.png"));
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		DemoReplayDialog dialog = new DemoReplayDialog(shell);
		if (dialog.open() != Window.OK) {
			return;
		}

		ProcessInstanceDatabaseHandle processInstance = dialog.getProcessInstance();
		AbstractReplayAction.REPLAY_ACTIVE = true;

		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ReplayView.ID);

			GraphReplayAdvisor advisor = new GraphReplayAdvisor();
			GraphCommandStack stack = advisor.initialize(processInstance);
			new ReplayModel(stack, processInstance, stack.getGraph());
			// TODO yay, we should implement the remaining part...
		} catch (PartInitException e) {
			Activator.logError("Could not replay the model", e);
		}

		AbstractReplayAction.REPLAY_ACTIVE = false;
	}
}
