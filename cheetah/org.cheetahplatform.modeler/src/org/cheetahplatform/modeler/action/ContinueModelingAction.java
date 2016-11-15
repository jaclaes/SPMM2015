package org.cheetahplatform.modeler.action;

import static org.cheetahplatform.modeler.ModelerConstants.ATTRIBUTE_TYPE;

import java.util.List;
import java.util.concurrent.Semaphore;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.DelegatingPromLogger;
import org.cheetahplatform.common.logging.DevNullPromLogger;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.DatabasePromWriter;
import org.cheetahplatform.common.logging.db.StaticPromWriteProvider;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.literatemodeling.LiterateModelingActivity;
import org.cheetahplatform.literatemodeling.LiterateReplayAdvisor;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.ApplicationActionBarAdvisor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.dialog.IReplayAdvisor;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.tdm.TdmModelingReplayAdvisor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public class ContinueModelingAction extends AbstractOpenProcessInstanceAction {

	private static class ContinueLiProMoRunnable implements Runnable {
		private Semaphore semaphore;
		private ProcessInstanceDatabaseHandle handle;
		private final IReplayAdvisor advisor;
		private final GraphCommandStack stack;

		public ContinueLiProMoRunnable(Semaphore semaphore, ProcessInstanceDatabaseHandle handle, IReplayAdvisor advisor,
				GraphCommandStack stack) {
			this.semaphore = semaphore;
			this.handle = handle;
			this.advisor = advisor;
			this.stack = stack;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				Activator.logError("An error occurred while restoring a TDM model.", e);
			}
			AbstractReplayAction.REPLAY_ACTIVE = false;

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					advisor.dispose();
					stack.getGraph().addLogListener(new ContinueModelingListener(handle));
				}
			});
		}

	}

	private static class ContinueModelingListener implements ILogListener {

		private DatabasePromWriter writer;

		public ContinueModelingListener(ProcessInstanceDatabaseHandle handle) {
			writer = new DatabasePromWriter(handle.getDatabaseId());
		}

		@Override
		public void log(AuditTrailEntry entry) {
			writer.append(entry);
		}

	}

	private static class ContinueTDMRunnable implements Runnable {
		private Semaphore semaphore;
		private DelegatingPromLogger logger;
		private ProcessInstanceDatabaseHandle handle;

		public ContinueTDMRunnable(Semaphore semaphore, DelegatingPromLogger logger, ProcessInstanceDatabaseHandle handle) {
			this.semaphore = semaphore;
			this.logger = logger;
			this.handle = handle;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				Activator.logError("An error occurred while restoring a TDM model.", e);
			}
			AbstractReplayAction.REPLAY_ACTIVE = false;

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					PromLogger delegate = new PromLogger(new StaticPromWriteProvider(new DatabasePromWriter(handle.getDatabaseId())));
					logger.setDelegate(delegate);
					MenuManager menuManager = (MenuManager) ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow())
							.getMenuBarManager();
					menuManager.add(new FinishAction(logger));
					menuManager.update(true);
				}
			});
		}
	}

	private static class FinishAction extends Action {
		public static final String ID = "org.cheetahplatform.modeler.action.FinishAction";
		private IContributionItem processMenu;
		private DelegatingPromLogger logger;

		public FinishAction(DelegatingPromLogger logger) {
			setId(ID);
			setText("Finish Modeling");

			this.logger = logger;
			MenuManager menuManager = (MenuManager) ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow())
					.getMenuBarManager();
			processMenu = menuManager.remove(ApplicationActionBarAdvisor.PROCESS_MENU);
		}

		@Override
		public void run() {
			MenuManager menuManager = (MenuManager) ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow())
					.getMenuBarManager();
			menuManager.remove(ID);
			menuManager.update(true);
			logger.setDelegate(new DevNullPromLogger());

			TDMModelingActivity.cleanUpTDM();
			if (processMenu != null) {
				menuManager.add(processMenu);
			}
		}
	}

	private final class ReplayCompletedCallback implements ICommandReplayerCallback {
		private final Semaphore semaphore;
		private final CommandDelegate lastCommand;
		private ProgressMonitorDialog dialog;

		private ReplayCompletedCallback(Semaphore semaphore, CommandDelegate lastCommand) {
			this.semaphore = semaphore;
			this.lastCommand = lastCommand;
		}

		public ReplayCompletedCallback(Semaphore semaphore, CommandDelegate lastCommand, ProgressMonitorDialog dialog) {
			this(semaphore, lastCommand);
			this.dialog = dialog;
		}

		@Override
		public void processed(CommandDelegate command, boolean last) {
			if (command.equals(lastCommand)) {
				semaphore.release();
				model.removeCallbackListener(this);
				if (dialog != null) {
					dialog.getProgressMonitor().done();
					dialog.close();
				}
			}

			if (dialog != null) {
				dialog.getProgressMonitor().worked(1);
			}
		}
	}

	public static final String ID = "org.cheetahplatform.modeler.action.ContinueModelingAction";
	private ReplayModel model;

	public ContinueModelingAction() {
		setId(ID);
		setText("Continue Process Model");
	}

	@Override
	protected void doRun(ProcessInstanceDatabaseHandle handle, ExperimentalWorkflowElementDatabaseHandle experimentalWorkflowElementHandle) {
		AbstractReplayAction.REPLAY_ACTIVE = true;

		ProcessInstance instance = handle.getInstance();
		String editorType = instance.getAttribute(ATTRIBUTE_TYPE);

		String type = experimentalWorkflowElementHandle.getType();
		if (type.equals(TDMModelingActivity.TDM_MODELING)) {
			handleTDMContinuation(handle);
		} else if (type.equals(LiterateModelingActivity.ID)) {
			handleLiterateModelingContinuation(handle);
		} else {
			Graph graph = new Graph(EditorRegistry.getDescriptors(editorType));
			AbstractModelingActivity.restoreGraph(graph, instance);

			Process process = ProcessRepository.getProcess(handle.getProcessId());
			EditorRegistry.openEditor(editorType, graph, instance.getAttributes(), process);

			graph.addLogListener(new ContinueModelingListener(handle));
		}
	}

	private void handleLiterateModelingContinuation(ProcessInstanceDatabaseHandle handle) {
		IReplayAdvisor advisor = new LiterateReplayAdvisor();
		GraphCommandStack stack = advisor.initialize(handle);
		model = new ReplayModel(stack, handle, stack.getGraph());
		final Semaphore semaphore = new Semaphore(0);

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
		IProgressMonitor progressMonitor = dialog.getProgressMonitor();
		List<CommandDelegate> commands = model.getCommands();
		progressMonitor.beginTask("Restoring LiProMo Model...", commands.size());

		if (!commands.isEmpty()) {
			final CommandDelegate lastCommand = commands.get(commands.size() - 1);
			try {
				model.addCallbackListener(new ReplayCompletedCallback(semaphore, lastCommand, dialog));
				model.gotoLastStep();
			} catch (Exception e) {
				Activator.logError("Could not restore the model.", e);
				return;
			}
		} else {
			progressMonitor.done();
			dialog.close();
			semaphore.release();
		}

		new Thread(new ContinueLiProMoRunnable(semaphore, handle, advisor, stack)).start();

	}

	private void handleTDMContinuation(ProcessInstanceDatabaseHandle handle) {
		DelegatingPromLogger logger = new DelegatingPromLogger(new DevNullPromLogger());
		IReplayAdvisor advisor = new TdmModelingReplayAdvisor(logger);
		GraphCommandStack stack = advisor.initialize(handle);
		model = new ReplayModel(stack, handle, stack.getGraph());
		final Semaphore semaphore = new Semaphore(0);

		List<CommandDelegate> commands = model.getCommands();
		if (!commands.isEmpty()) {
			final CommandDelegate lastCommand = commands.get(commands.size() - 1);
			try {
				model.addCallbackListener(new ReplayCompletedCallback(semaphore, lastCommand));
				model.gotoLastStep();
			} catch (Exception e) {
				Activator.logError("Could not restore the model.", e);
				return;
			}
		} else {
			semaphore.release();
		}

		new Thread(new ContinueTDMRunnable(semaphore, logger, handle)).start();
	}
}
