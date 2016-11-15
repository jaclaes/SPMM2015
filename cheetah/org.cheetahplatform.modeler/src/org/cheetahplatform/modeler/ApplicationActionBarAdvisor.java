package org.cheetahplatform.modeler;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.action.AbstractReplayAction;
import org.cheetahplatform.modeler.action.AlignHorizontallyAction;
import org.cheetahplatform.modeler.action.AlignVerticallyAction;
import org.cheetahplatform.modeler.action.ChangePatternAction;
import org.cheetahplatform.modeler.action.ChangePatternModificationAction;
import org.cheetahplatform.modeler.action.ComputeConstraintsAction;
import org.cheetahplatform.modeler.action.ComputeSizeAction;
import org.cheetahplatform.modeler.action.ConfigureParagraphMappingAction;
import org.cheetahplatform.modeler.action.ContinueModelingAction;
import org.cheetahplatform.modeler.action.ConvertXMLLogAction;
import org.cheetahplatform.modeler.action.DemoReplayAction;
import org.cheetahplatform.modeler.action.DuplicateProcessAction;
import org.cheetahplatform.modeler.action.ExportActivityNamesAction;
import org.cheetahplatform.modeler.action.ExportAuditTrailEntriesToCSV;
import org.cheetahplatform.modeler.action.ExportCategorizationAction;
import org.cheetahplatform.modeler.action.ExportChunkOverviewAction;
import org.cheetahplatform.modeler.action.ExportChunksAction;
import org.cheetahplatform.modeler.action.ExportClusteringDataAction;
import org.cheetahplatform.modeler.action.ExportCsvAction;
import org.cheetahplatform.modeler.action.ExportCustomGraphNotationAction;
import org.cheetahplatform.modeler.action.ExportDeclareAction;
import org.cheetahplatform.modeler.action.ExportDurationToLayoutingAction;
import org.cheetahplatform.modeler.action.ExportExperimentalWorkflowToMXMLAction;
import org.cheetahplatform.modeler.action.ExportMappingAction;
import org.cheetahplatform.modeler.action.ExportMeasureClusteringDataAction;
import org.cheetahplatform.modeler.action.ExportModelToImageAction;
import org.cheetahplatform.modeler.action.ExportModelingPausesAction;
import org.cheetahplatform.modeler.action.ExportModelingPhasesDiagrammAction;
import org.cheetahplatform.modeler.action.ExportModelingStepsAction;
import org.cheetahplatform.modeler.action.ExportModelingTimeStepsAction;
import org.cheetahplatform.modeler.action.ExportProcessInstanceAction;
import org.cheetahplatform.modeler.action.ExportProcessOfProcessModelingIterationsAction;
import org.cheetahplatform.modeler.action.ExportSlidingWindowsAction;
import org.cheetahplatform.modeler.action.ExportStatisticsAction;
import org.cheetahplatform.modeler.action.FillInTDMCaseStudyQuestionsAction;
import org.cheetahplatform.modeler.action.FindRestoredWorkflowsAction;
import org.cheetahplatform.modeler.action.ImportAttributeAction;
import org.cheetahplatform.modeler.action.ImportBPMNAction;
import org.cheetahplatform.modeler.action.ImportModelingTranscriptsAction;
import org.cheetahplatform.modeler.action.LayoutAction;
import org.cheetahplatform.modeler.action.MapParagraphAction;
import org.cheetahplatform.modeler.action.MapParagraphBatchAction;
import org.cheetahplatform.modeler.action.NewBPMNChangeTaskAction;
import org.cheetahplatform.modeler.action.NewBPMNModelAction;
import org.cheetahplatform.modeler.action.NewDecSerFlowModelAction;
import org.cheetahplatform.modeler.action.NewLiProMoModelAction;
import org.cheetahplatform.modeler.action.PpmDashboardAction;
import org.cheetahplatform.modeler.action.RectangularAction;
import org.cheetahplatform.modeler.action.ReplayAction;
import org.cheetahplatform.modeler.action.ShowLayoutTutorialAction;
import org.cheetahplatform.modeler.action.ShowModelingInstanceMappingAction;
import org.cheetahplatform.modeler.action.ShowTutorialAction;
import org.cheetahplatform.modeler.action.TDMAction;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.OpenPreferencesAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	public static final String PROCESS_MENU = "org.cheetahplatform.modeler.menu.process";

	private List<IAction> processMenu;
	private List<IAction> tutorialMenu;
	private List<IAction> newModelMenu;
	private List<IAction> loadMenu;
	private List<IAction> paragraphMenu;
	private List<IAction> importMenu;
	private List<IAction> exportMenu;
	private List<IAction> metricsMenu;
	private List<IAction> layoutMenu;
	private List<IAction> miscMenu;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);

		this.processMenu = new ArrayList<IAction>();
		this.newModelMenu = new ArrayList<IAction>();
		this.loadMenu = new ArrayList<IAction>();
		this.paragraphMenu = new ArrayList<IAction>();
		this.exportMenu = new ArrayList<IAction>();
		this.importMenu = new ArrayList<IAction>();
		this.metricsMenu = new ArrayList<IAction>();
		this.layoutMenu = new ArrayList<IAction>();
		this.miscMenu = new ArrayList<IAction>();
		this.tutorialMenu = new ArrayList<IAction>();
	}

	private void addMenu(IMenuManager parent, String name, List<IAction> content) {
		if (content.isEmpty()) {
			return;
		}

		MenuManager childMenu = new MenuManager(name);
		parent.add(childMenu);

		for (IAction action : content) {
			childMenu.add(action);
		}
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_TOOL_BAR)) {
			ToolBarManager toolBarManager = new ToolBarManager();
			coolBar.add(toolBarManager);
			toolBarManager.add(new Action() {
				{
					setId("org.cheetahplatform.action.dummyAction");
					setText("");
				}
			});
		}
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		boolean hasNewModelMenu = !newModelMenu.isEmpty();
		boolean hasLoadMenu = !loadMenu.isEmpty();
		boolean hasParagraphMenu = !paragraphMenu.isEmpty();
		boolean hasImportMenu = !importMenu.isEmpty();
		boolean hasExportMenu = !exportMenu.isEmpty();
		boolean hasMetricsMenu = !metricsMenu.isEmpty();
		boolean hasLayoutMenu = !layoutMenu.isEmpty();
		boolean hasProcessMenu = !processMenu.isEmpty();
		boolean hasMiscMenu = !miscMenu.isEmpty();

		boolean hasMenu = hasNewModelMenu || hasLoadMenu || hasParagraphMenu || hasExportMenu || hasMetricsMenu || hasLayoutMenu
				|| hasProcessMenu || hasMiscMenu || hasImportMenu;

		if (!hasMenu) {
			return;
		}

		MenuManager processMenu = new MenuManager("Process");
		processMenu.setActionDefinitionId(PROCESS_MENU);
		menuBar.add(processMenu);

		addMenu(processMenu, "New", newModelMenu);
		addMenu(processMenu, "Load", loadMenu);
		addMenu(processMenu, "Paragraph Mapping", paragraphMenu);
		addMenu(processMenu, "Import", importMenu);
		addMenu(processMenu, "Export", exportMenu);
		addMenu(processMenu, "Metrics", metricsMenu);
		addMenu(processMenu, "Layout", layoutMenu);
		addMenu(processMenu, "Tutorial", tutorialMenu);
		addMenu(processMenu, "Misc", miscMenu);

		for (IAction action : this.processMenu) {
			processMenu.add(action);
		}
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_BPMN_MODEL)) {
			NewBPMNModelAction action = new NewBPMNModelAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_BPMN_CHANGE_TASK_MODEL)) {
			NewBPMNChangeTaskAction action = new NewBPMNChangeTaskAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_LIPROMO_MODEL)) {
			NewLiProMoModelAction action = new NewLiProMoModelAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.REPLAY)) {
			AbstractReplayAction action = new ReplayAction();
			loadMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.PPM_DASHBOARD_REPLAY)) {
			PpmDashboardAction action = new PpmDashboardAction();
			loadMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.DEMO_REPLAY)) {
			DemoReplayAction action = new DemoReplayAction();
			loadMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_MODELING_PROCESS)) {
			IAction exportAction = new ExportProcessInstanceAction();
			register(exportAction);
			exportMenu.add(exportAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_CSV)) {
			IAction exportCsvAction = new ExportCsvAction();
			register(exportCsvAction);
			exportMenu.add(exportCsvAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.PREFERENCES_DIALOG)) {
			IAction openPreferencesAction = new OpenPreferencesAction(window);
			openPreferencesAction.setId("org.cheetahplatform.actions.openPreferencesAction");
			register(openPreferencesAction);
			processMenu.add(openPreferencesAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_DECSERFLOW_MODEL)) {
			NewDecSerFlowModelAction action = new NewDecSerFlowModelAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_CHANGE_PATTERN_MODEL)) {
			ChangePatternAction action = new ChangePatternAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_CHANGE_PATTERN_CHANGE_TASK)) {
			ChangePatternModificationAction action = new ChangePatternModificationAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.MAP_PARAGRAPH)) {
			MapParagraphAction mappingAction = new MapParagraphAction();
			register(mappingAction);
			paragraphMenu.add(mappingAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.MAP_PARAGRAPH_BATCH_VERSION)) {
			MapParagraphBatchAction action = new MapParagraphBatchAction();
			register(action);
			paragraphMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_MODEL_TO_IMAGE)) {
			IAction exportModelToImageAction = new ExportModelToImageAction();
			register(exportModelToImageAction);
			exportMenu.add(exportModelToImageAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_DURATION_TO_LAYOUT)) {
			IAction exportDurationToLayout = new ExportDurationToLayoutingAction();
			register(exportDurationToLayout);
			exportMenu.add(exportDurationToLayout);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_STEP_CATEGORIZATION)) {
			IAction action = new ExportCategorizationAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CONFIGURE_PARAGRAPH_MAPPING)) {
			ConfigureParagraphMappingAction configureParagraphMappingAction = new ConfigureParagraphMappingAction();
			register(configureParagraphMappingAction);
			paragraphMenu.add(configureParagraphMappingAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_TUTORIAL)) {
			IAction showTutorialAction = new ShowTutorialAction();
			register(showTutorialAction);
			tutorialMenu.add(showTutorialAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PARAGRAPH_MAPPING)) {
			IAction exportMappingAction = new ExportMappingAction();
			register(exportMappingAction);
			exportMenu.add(exportMappingAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_CUSTOM_GRAPH_NOTATION)) {
			IAction exportCustomGraphNotationAction = new ExportCustomGraphNotationAction();
			register(exportCustomGraphNotationAction);
			exportMenu.add(exportCustomGraphNotationAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_MODELING_DIAGRAM)) {
			IAction exportModelingDiagramAction = new ExportModelingTimeStepsAction();
			register(exportModelingDiagramAction);
			exportMenu.add(exportModelingDiagramAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_MODELING_PHASES_DIAGRAM)) {
			IAction exportModelingPhasesDiagrammAction = new ExportModelingPhasesDiagrammAction();
			register(exportModelingPhasesDiagrammAction);
			exportMenu.add(exportModelingPhasesDiagrammAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_STATISTICS_ACTION)) {
			IAction exportLayoutStatisticsAction = new ExportStatisticsAction();
			register(exportLayoutStatisticsAction);
			exportMenu.add(exportLayoutStatisticsAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CONTINUE_MODELING_PROCESS)) {
			ContinueModelingAction action = new ContinueModelingAction();
			loadMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NEW_TDM_MODEL)) {
			TDMAction action = new TDMAction();
			newModelMenu.add(action);
			register(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_EXPERIMENTAL_WORKFLOW)) {
			IAction exportExperimentalWorkflowToMXMLAction = new ExportExperimentalWorkflowToMXMLAction();
			register(exportExperimentalWorkflowToMXMLAction);
			exportMenu.add(exportExperimentalWorkflowToMXMLAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_CHUNKS)) {
			IAction exportChunksAction = new ExportChunksAction();
			register(exportChunksAction);
			exportMenu.add(exportChunksAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_ITERATIONS)) {
			IAction exportIterationsAction = new ExportProcessOfProcessModelingIterationsAction();
			register(exportIterationsAction);
			exportMenu.add(exportIterationsAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_PHASE_SIMILARITY)) {
			IAction exportIterationsAction = new ExportSlidingWindowsAction();
			register(exportIterationsAction);
			exportMenu.add(exportIterationsAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_CLUSTERING_DATA)) {
			IAction action = new ExportClusteringDataAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_CLUSTERING_DATA)) {
			IAction action = new ExportMeasureClusteringDataAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_AUDIT_TRAIL_ENTRIES_TO_CSV)) {
			IAction action = new ExportAuditTrailEntriesToCSV();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_CHUNK_OVERVIEW)) {
			IAction exportChunkOverviewAction = new ExportChunkOverviewAction();
			register(exportChunkOverviewAction);
			exportMenu.add(exportChunkOverviewAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_MODELING_PAUSES)) {
			IAction exportModelingPausesAction = new ExportModelingPausesAction();
			register(exportModelingPausesAction);
			exportMenu.add(exportModelingPausesAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.COMPUTE_SIZE)) {
			IAction computeSizeAction = new ComputeSizeAction();
			register(computeSizeAction);
			metricsMenu.add(computeSizeAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.COMPUTE_CONSTRAINTS)) {
			IAction computeConstraintsAction = new ComputeConstraintsAction();
			register(computeConstraintsAction);
			metricsMenu.add(computeConstraintsAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.RECTANGULAR_EDGES)) {
			IAction rectangularAction = new RectangularAction();
			register(rectangularAction);
			layoutMenu.add(rectangularAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALIGN_HORIZONTALLY)) {
			IAction alignHorizontallyAction = new AlignHorizontallyAction();
			register(alignHorizontallyAction);
			layoutMenu.add(alignHorizontallyAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALIGN_VERTICALLY)) {
			IAction alignVerticallyAction = new AlignVerticallyAction();
			register(alignVerticallyAction);
			layoutMenu.add(alignVerticallyAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.LAYOUT)) {
			IAction layoutAction = new LayoutAction();
			register(layoutAction);
			layoutMenu.add(layoutAction);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CONVERT_XML_LOG_FILES)) {
			ConvertXMLLogAction action = new ConvertXMLLogAction();
			register(action);
			miscMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_MODELING_INSTANCE_MAPPING)) {
			ShowModelingInstanceMappingAction action = new ShowModelingInstanceMappingAction();
			register(action);
			miscMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.IMPORT_MODELING_TRANSCRIPTS)) {
			ImportModelingTranscriptsAction action = new ImportModelingTranscriptsAction();
			register(action);
			miscMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.IMPORT_ATTRIBUTE)) {
			ImportAttributeAction action = new ImportAttributeAction();
			register(action);
			miscMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.TDM_CASE_STUDY_QUESTIONNAIRE)) {
			FillInTDMCaseStudyQuestionsAction action = new FillInTDMCaseStudyQuestionsAction();
			register(action);
			newModelMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.DUPLICATE_PROCESS)) {
			DuplicateProcessAction action = new DuplicateProcessAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.FIND_RESTORED_WORKFLOWS)) {
			FindRestoredWorkflowsAction action = new FindRestoredWorkflowsAction();
			register(action);
			miscMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_LAYOUT_TUTORIAL)) {
			ShowLayoutTutorialAction action = new ShowLayoutTutorialAction();
			register(action);
			tutorialMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_DECLARE_MODEL)) {
			ExportDeclareAction action = new ExportDeclareAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_ACTIVITY_NAMES)) {
			ExportActivityNamesAction action = new ExportActivityNamesAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EXPORT_MODELING_STEPS)) {
			ExportModelingStepsAction action = new ExportModelingStepsAction();
			register(action);
			exportMenu.add(action);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.IMPORT_BPMN_XML)) {
			ImportBPMNAction action = new ImportBPMNAction();
			register(action);
			importMenu.add(action);
		}

		for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor("org.cheetahplatform.menu")) {
			if (element.getName().equals("menuItem")) {
				try {
					Action action = (Action) element.createExecutableExtension("class");
					register(action);
					String id = element.getAttribute("configurationId");
					if (!CheetahPlatformConfigurator.getBoolean(id)) {
						continue;
					}

					String path = element.getAttribute("path");

					if (path.equals("new")) {
						newModelMenu.add(action);
					} else if (path.equals("tutorial")) {
						tutorialMenu.add(action);
					} else if (path.equals("layout")) {
						layoutMenu.add(action);
					} else if (path.equals("misc")) {
						miscMenu.add(action);
					} else {
						throw new RuntimeException("Could not find a corresponding path: " + path);
					}
				} catch (CoreException e) {
					org.cheetahplatform.common.Activator.logError("Could not create an executable extension", e);
				}
			}
		}
	}

}
