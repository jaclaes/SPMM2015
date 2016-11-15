package org.cheetahplatform.modeler.engine;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_ACTIVITIES;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_PROCESS_SCHEMA;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_GRAPH;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_WORKSPACE;
import static org.cheetahplatform.modeler.engine.AbstractModelingActivity.loadInitialGraph;
import static org.cheetahplatform.tdm.dialog.DetermineConsistencyDialog.CONSISTENT;
import static org.cheetahplatform.tdm.dialog.DetermineConsistencyDialog.INCONSISTENT;

import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.PartListenerAdapter;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.core.service.ICheetahObjectLookup;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.Perspective;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask11Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask12Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask13Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask14Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask15Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask1Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask21Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask22Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask23Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask24Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask25Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask2Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask31Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask32Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask33Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask34Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask35Initializer;
import org.cheetahplatform.modeler.engine.tdm.ChangeTask3Initializer;
import org.cheetahplatform.modeler.engine.tdm.TDMReplicationDemo1Initializer;
import org.cheetahplatform.modeler.engine.tdm.TDMReplicationDemo2Initializer;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.TDMPerspective;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.dialog.DetermineConsistencyDialog;
import org.cheetahplatform.tdm.engine.TDMDecSerFlowGraphDescriptor;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.explorer.TDMProjectExplorerView;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerModel;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerView;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditor;
import org.cheetahplatform.tdm.modeler.test.TDMTestEditorInput;
import org.cheetahplatform.tdm.problemview.ProblemView;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public class TDMModelingActivity extends AbstractExperimentsWorkflowActivity {
	private class FinishTDMAction extends FinishAction {

		public FinishTDMAction(AbstractExperimentsWorkflowActivity activity, Graph graph) {
			super(activity, graph, false);
		}

		@Override
		public void run() {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			if (!MessageDialog.openQuestion(shell, Messages.FinishAction_3, Messages.FinishAction_4)) {
				return;
			}

			if (CheetahPlatformConfigurator.getBoolean(IConfiguration.TDM_ASK_FOR_CONSISTENCY)) {
				DetermineConsistencyDialog dialog = new DetermineConsistencyDialog(Display.getDefault().getActiveShell());
				int returnCode = dialog.open();

				if (returnCode == DetermineConsistencyDialog.RECONSIDER || returnCode == Window.CANCEL) {
					return;
				}

				consistency = dialog.getReturnCode();
			}

			activity.setFinished(true);
			CoolBarManager menuManager = ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow()).getCoolBarManager();
			menuManager.removeAll();
			menuManager.update(true);
		}

	}

	private static class TestEditorListener extends PartListenerAdapter {

		private IPromLogger logger;

		public TestEditorListener(IPromLogger logger) {
			this.logger = logger;
		}

		@Override
		public void partActivated(IWorkbenchPart part) {
			AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_SHOW_PART);
			entry.setAttribute(TDMCommand.ATTRIBUTE_PART_ID, part.getSite().getId());

			if (part.getSite().getId().equals(TDMTestEditor.ID)) {
				TDMTest test = ((TDMTestEditor) part).getModel().getTest();
				entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, test.getCheetahId());
				entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, test.getName());
			}

			logger.log(entry);
		}
	}

	// private static class RedoAction extends UndoRedoAction {
	// public RedoAction() {
	// setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/redo.png"));
	// setDisabledImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/redo_disabled.png"));
	// setText("Redo");
	// }
	//
	// @Override
	// public void historyNotification(OperationHistoryEvent event) {
	// boolean canRedo = OperationHistoryFactory.getOperationHistory().canRedo(IOperationHistory.GLOBAL_UNDO_CONTEXT);
	// setEnabled(canRedo);
	// }
	//
	// @Override
	// public void run() {
	// IOperationHistory history = OperationHistoryFactory.getOperationHistory();
	// try {
	// history.redo(IOperationHistory.GLOBAL_UNDO_CONTEXT, null, null);
	// } catch (ExecutionException e) {
	// Activator.logError("Could not redo an operation.", e);
	// }
	// }
	// }

	public static final String CONSISTENCY = "consistency";

	// private static class UndoAction extends UndoRedoAction {
	// public UndoAction() {
	// setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/undo.png"));
	// setDisabledImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/tdm/undo_disabled.png"));
	// setText("Undo");
	// }
	//
	// @Override
	// public void historyNotification(OperationHistoryEvent event) {
	// boolean canUndo = OperationHistoryFactory.getOperationHistory().canUndo(IOperationHistory.GLOBAL_UNDO_CONTEXT);
	// setEnabled(canUndo);
	// }
	//
	// @Override
	// public void run() {
	// IOperationHistory history = OperationHistoryFactory.getOperationHistory();
	// try {
	// history.undo(IOperationHistory.GLOBAL_UNDO_CONTEXT, null, null);
	// } catch (ExecutionException e) {
	// Activator.logError("Could not undo an operation.", e);
	// }
	// }
	// }

	// private abstract static class UndoRedoAction extends Action implements IOperationHistoryListener {
	// public UndoRedoAction() {
	// IOperationHistory history = OperationHistoryFactory.getOperationHistory();
	// history.addOperationHistoryListener(this);
	// historyNotification(null);
	// }
	// }

	public static final String TDM_MODELING = "TDM_MODELING";
	public static final int DEFAULT_MODEL = 0;
	public static final int MODEL_1 = 1;
	public static final long MODEL_2 = 2;
	public static final long MODEL_3 = 3;
	public static final long MODEL_11 = 11;
	public static final long MODEL_12 = 12;
	public static final long MODEL_13 = 13;
	public static final long MODEL_14 = 14;
	public static final long MODEL_15 = 15;
	public static final long MODEL_21 = 21;
	public static final long MODEL_22 = 22;
	public static final long MODEL_23 = 23;
	public static final long MODEL_24 = 24;
	public static final long MODEL_25 = 25;
	public static final long MODEL_31 = 31;
	public static final long MODEL_32 = 32;
	public static final long MODEL_33 = 33;
	public static final long MODEL_34 = 34;
	public static final long MODEL_35 = 35;
	public static final long MODEL_TDM_REPLICATION_DEMO_1 = 36;
	public static final long MODEL_TDM_REPLICATION_DEMO_2 = 37;

	public static void addTestEditorListener(IPromLogger logger) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		TEST_EDITOR_LISTENER = new TestEditorListener(logger);
		activePage.addPartListener(TEST_EDITOR_LISTENER);
	}

	public static void cleanUpTDM() {
		if (Platform.isRunning()) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				page.closeAllEditors(false);
				page.setPerspective(PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(Perspective.ID));
				ProblemView problemView = ProblemView.getInstance();
				if (problemView != null) {
					problemView.clear();
				}
			}
		}

		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();
		lookup.deleteNameSpace(NAMESPACE_DECLARATIVE_PROCESS_SCHEMA);
		lookup.deleteNameSpace(NAMESPACE_DECLARATIVE_ACTIVITIES);
		lookup.deleteNameSpace(NAMESPACE_GRAPH);
	}

	public static TDMProcess getProcess(long tdmProcessId, IPromLogger logger) {
		logger.setEnabled(false);

		TDMProcess tdmProcess = new TDMProcess("My Declarative Process");
		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();
		lookup.registerObject(NAMESPACE_DECLARATIVE_PROCESS_SCHEMA, tdmProcess.getProcess());
		lookup.deleteNameSpace(NAMESPACE_WORKSPACE);

		initializeProcess(tdmProcess, tdmProcessId);
		if (!Platform.isRunning()) {
			return tdmProcess;
		}

		IWorkbench workbench = PlatformUI.getWorkbench();
		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(TDMPerspective.ID);
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspective);

		ProblemView problemView = (ProblemView) activePage.findView(ProblemView.ID);
		problemView.setLogger(logger);

		TDMProjectExplorerView view = (TDMProjectExplorerView) activePage.findView(TDMProjectExplorerView.ID);
		view.setInput(tdmProcess, logger);

		TDMDeclarativeModelerView modelerView = (TDMDeclarativeModelerView) activePage.findView(TDMDeclarativeModelerView.ID);
		modelerView.setInput(tdmProcess, logger);

		for (TDMTest test : tdmProcess.getTests()) {
			try {
				AuditTrailEntry entry = new AuditTrailEntry(new Date(), TDMCommand.COMMAND_CREATE_TEST, test.getName());
				entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, test.getCheetahId());
				entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, test.getName());
				logger.append(entry);

				activePage.openEditor(new TDMTestEditorInput(test, logger), TDMTestEditor.ID);
				test.run();
			} catch (PartInitException e) {
				Activator.logError("Could not open a test editor.", e);
			}
		}

		logger.setEnabled(true);
		return tdmProcess;
	}

	public static TDMProcess initializeModel(TDMProcess process, String initialModel) {
		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.DECSERFLOW), new TDMDecSerFlowGraphDescriptor());
		TDMDeclarativeModelerModel model = new TDMDeclarativeModelerModel(graph, process);
		graph.addLogListener(model);
		loadInitialGraph(graph, initialModel);
		graph.removeLogListener(model);
		process.setInitialGraph(graph);

		return process;
	}

	private static void initializeProcess(TDMProcess process, long id) {
		if (id == DEFAULT_MODEL) {
			TDMTest test = new TDMTest(process, "My First Test");
			test.setCheetahId(1);
			process.add(test);
		} else if (id == MODEL_1) {
			initializeModel(process, "bp_notation_6.0/model_1.mxml");
			new ChangeTask1Initializer(process).initialize();
		} else if (id == MODEL_2) {
			initializeModel(process, "bp_notation_6.0/model_2.mxml");
			new ChangeTask2Initializer(process).initialize();
		} else if (id == MODEL_3) {
			initializeModel(process, "bp_notation_6.0/model_3.mxml");
			new ChangeTask3Initializer(process).initialize();
		} else if (id == MODEL_11) {
			initializeModel(process, "bp_notation_7.0/model_11.mxml");
			new ChangeTask11Initializer(process).initialize();
		} else if (id == MODEL_12) {
			initializeModel(process, "bp_notation_7.0/model_12.mxml");
			new ChangeTask12Initializer(process).initialize();
		} else if (id == MODEL_13) {
			initializeModel(process, "bp_notation_7.0/model_13.mxml");
			new ChangeTask13Initializer(process).initialize();
		} else if (id == MODEL_14) {
			initializeModel(process, "bp_notation_7.0/model_14.mxml");
			new ChangeTask14Initializer(process).initialize();
		} else if (id == MODEL_15) {
			initializeModel(process, "bp_notation_7.0/model_15.mxml");
			new ChangeTask15Initializer(process).initialize();
		} else if (id == MODEL_21) {
			initializeModel(process, "bp_notation_7.0/model_21.mxml");
			new ChangeTask21Initializer(process).initialize();
		} else if (id == MODEL_22) {
			initializeModel(process, "bp_notation_7.0/model_22.mxml");
			new ChangeTask22Initializer(process).initialize();
		} else if (id == MODEL_23) {
			initializeModel(process, "bp_notation_7.0/model_23.mxml");
			new ChangeTask23Initializer(process).initialize();
		} else if (id == MODEL_24) {
			initializeModel(process, "bp_notation_7.0/model_24.mxml");
			new ChangeTask24Initializer(process).initialize();
		} else if (id == MODEL_25) {
			initializeModel(process, "bp_notation_7.0/model_25.mxml");
			new ChangeTask25Initializer(process).initialize();
		} else if (id == MODEL_31) {
			initializeModel(process, "bp_notation_7.0/model_31.mxml");
			new ChangeTask31Initializer(process).initialize();
		} else if (id == MODEL_32) {
			initializeModel(process, "bp_notation_7.0/model_32.mxml");
			new ChangeTask32Initializer(process).initialize();
		} else if (id == MODEL_33) {
			initializeModel(process, "bp_notation_7.0/model_33.mxml");
			new ChangeTask33Initializer(process).initialize();
		} else if (id == MODEL_34) {
			initializeModel(process, "bp_notation_7.0/model_34.mxml");
			new ChangeTask34Initializer(process).initialize();
		} else if (id == MODEL_35) {
			initializeModel(process, "bp_notation_7.0/model_35.mxml");
			new ChangeTask35Initializer(process).initialize();
		} else if (id == MODEL_TDM_REPLICATION_DEMO_1) {
			initializeModel(process, "bp_notation_7.0/tdm_replication_demo_model_1.mxml");
			new TDMReplicationDemo1Initializer(process).initialize();
		} else if (id == MODEL_TDM_REPLICATION_DEMO_2) {
			initializeModel(process, "bp_notation_7.0/tdm_replication_demo_model_2.mxml");
			new TDMReplicationDemo2Initializer(process).initialize();
		}

		// if we provide existing tests, ensure that we don't mess up with existing ids!
	}

	private PromLogger logger;

	private static TestEditorListener TEST_EDITOR_LISTENER;
	private final long tdmProcessId;

	private final Process experimentalProcess;
	private int consistency;

	private boolean deleteEdgesWhenDeletingNodes;

	public TDMModelingActivity(long tdmProcessId, Process experimentalProcess) {
		super(TDM_MODELING);

		this.tdmProcessId = tdmProcessId;
		this.experimentalProcess = experimentalProcess;
	}

	private void addCoolBarMenu() {
		final CoolBarManager menuManager = ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow()).getCoolBarManager();
		menuManager.removeAll();

		ToolBarManager toolBarManager = new ToolBarManager();
		menuManager.add(toolBarManager);
		toolBarManager.add(new FinishTDMAction(this, getModelerView().getGraph()));

		// toolBarManager = new ToolBarManager();
		// menuManager.add(toolBarManager);
		// toolBarManager.add(new UndoAction());
		// toolBarManager.add(new RedoAction());

		menuManager.update(true);
	}

	@Override
	public void doExecute() {
		this.logger = new PromLogger();
		getProcess(tdmProcessId, logger);
		ProcessInstance instance = new ProcessInstance(ExperimentalWorkflowEngine.generateProcessInstanceId());
		instance.setAttribute(new Attribute(TDMConstants.ATTRIBUTE_TDM_PROCESS_ID, tdmProcessId));
		instance.setAttribute(new Attribute(CommonConstants.ATTRIBUTE_PROCESS, experimentalProcess.getId()));
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, TDM_MODELING);
		this.logger.append(experimentalProcess, instance);

		addTestEditorListener(this.logger);

		addCoolBarMenu();
		setFinished(false);
		deleteEdgesWhenDeletingNodes = CheetahPlatformConfigurator.getBoolean(IConfiguration.DELETE_EDGES_WHEN_DELETING_NODE);
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, logger.getProcessInstanceId()));
		if (consistency == CONSISTENT || consistency == INCONSISTENT) {
			// do not log consistency if we did not ask for it
			data.add(new Attribute(CONSISTENCY, consistency));
		}

		return data;
	}

	public GraphicalGraphViewerWithFlyoutPalette getModelerView() {
		TDMDeclarativeModelerView view = (TDMDeclarativeModelerView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(TDMDeclarativeModelerView.ID);
		return view.getViewer();
	}

	@Override
	public Object getName() {
		return "TDM Modeling";
	}

	@Override
	protected void postExecute() {
		// don't log any of the cleanup stuff (e.g., closing test editors)
		logger.setEnabled(false);
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			page.removePartListener(TEST_EDITOR_LISTENER);
		}

		cleanUpTDM();
		logger.setEnabled(true);
		CheetahPlatformConfigurator.setBoolean(IConfiguration.DELETE_EDGES_WHEN_DELETING_NODE, deleteEdgesWhenDeletingNodes);
	}

}
