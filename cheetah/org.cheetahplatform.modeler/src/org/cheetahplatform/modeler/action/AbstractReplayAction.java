package org.cheetahplatform.modeler.action;

import org.cheetahplatform.common.PartListenerAdapter;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.dialog.ReplayView;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractReplayAction extends AbstractOpenProcessInstanceAction {

	final class EditorCloseListener extends PartListenerAdapter {
		@Override
		public void partClosed(IWorkbenchPart part) {
			if (!(part instanceof GraphEditor) && !(part instanceof ReplayView)) {
				return;
			}

			if (part instanceof GraphEditor) {
				GraphEditor editor = ((GraphEditor) part);
				if (!editor.getGraph().equals(getGraph())) {
					return;
				}
			}

			REPLAY_ACTIVE = false;
			IWorkbenchWindow window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
			if (window.getActivePage() != null) {
				window.getActivePage().removePartListener(this);
			}
			replayFinished();
		}
	}

	public static boolean REPLAY_ACTIVE;

	protected EditorCloseListener editorCloseListener;

	public AbstractReplayAction() {
		editorCloseListener = new EditorCloseListener();
	}

	public AbstractReplayAction(IExtraColumnProvider provider) {
		super(provider);
	}

	@Override
	protected void doRun(ProcessInstanceDatabaseHandle handle, ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle) {
		REPLAY_ACTIVE = true;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		window.getActivePage().addPartListener(editorCloseListener);
	}

	protected abstract Graph getGraph();

	/**
	 * Called after the replay is finshed. Subclasses might override.
	 */
	protected void replayFinished() {
		// nothing to do
	}

	@Override
	public void run() {
		if (REPLAY_ACTIVE) {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Replay active", "Replay is already running.");
			return;
		}

		super.run();
	}

}