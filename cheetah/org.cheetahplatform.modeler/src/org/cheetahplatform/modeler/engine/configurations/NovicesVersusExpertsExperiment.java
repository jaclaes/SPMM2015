package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ActivityPresenceEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ConditionEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.DirectSuccessionEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.EdgePresenceEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ExclusiveEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ISemanticalCorrectnessEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.InitialActivityEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ParallelEvaluation;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.EdgeConditionProvider;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.RGB;

public class NovicesVersusExpertsExperiment extends AbstractExperimentalConfiguration {

	public static final Process MODELING_TASK_1 = new Process("nve_modeling_task1_1.0");
	public static final Process MODELING_TASK_2 = new Process("nve_modeling_task2_1.0");
	public static final Process EXPERIMENTAL_PROCESS = new Process("novices_versus_experts_1.0");

	public static final int NO_TOWER_TAKE_OFF = 105;
	public static final int HAS_TOWER_TAKE_OFF = 104;
	public static final int NO_TOWER_CLEARANCE = 103;
	public static final int HAS_TOWER_CLEARANCE = 102;
	public static final int LARGE_AIRPORT = 101;
	public static final int FILE_FLIGHT_PLAN = 100;

	public static final long WATCH_TAPE_LOOP = 110;
	public static final long NOT_INTERESTED_AFTER_TALK = 111;
	public static final long NOT_INTERESTED_EVALUATION = 112;
	public static final long PLAYER_NOT_AVAILABLE = 113;
	public static final long PLAYER_NOT_AVAILABLE_OR_NOT_INTERESTED = 114;

	private static void addBPMNFamiliarity(List<SurveyAttribute> attributes) {
		ComboInputAttribute bpmnfamiliarity = new ComboInputAttribute("Overall, I am very familiar with the BPMN.", true);
		addChoices(bpmnfamiliarity);
		attributes.add(bpmnfamiliarity);

		ComboInputAttribute fam2 = new ComboInputAttribute("I feel very confident in understanding process models created with the BPMN.",
				true);
		addChoices(fam2);
		attributes.add(fam2);

		ComboInputAttribute fam3 = new ComboInputAttribute("I feel very competent in using the BPMN for process modeling.", true);
		addChoices(fam3);
		attributes.add(fam3);
		attributes
				.add(new IntegerInputAttribute(
						"How many months ago did you start using BPMN? (The first version of BPMN stems from May 2004, i.e. 60 months until May 2009) ",
						true, 0, Integer.MAX_VALUE));
	}

	private static void addChoices(ComboInputAttribute combo) {
		combo.addChoice("strongly agree");
		combo.addChoice("agree");
		combo.addChoice("somewhat agree");
		combo.addChoice("neutral");
		combo.addChoice("somewhat disagree");
		combo.addChoice("disagree");
		combo.addChoice("strongly disagree");
	}

	private List<Paragraph> preFlightParagraphs;

	private Paragraph checkWeatherActivity;
	private Paragraph fileFlightPlanActivity;
	private Paragraph checkEngineActivity;
	private Paragraph checkFuselageActivtiy;
	private Paragraph callClearanceDeliveryActivity;
	private Paragraph getTaxiClearanceActivity;
	private Paragraph announceTaxiingActivity;
	private Paragraph taxiingActivity;
	private Paragraph runUpChecksActivity;
	private Paragraph getTakeOffClearanceActivity;
	private Paragraph announceTakeOffActivity;
	private Paragraph takeOffActivity;
	private Paragraph semanticallyIncorrectActivity;
	private Paragraph superfluousActivity;
	private List<ISemanticalCorrectnessEvaluation> task1Evaluations;
	private List<ISemanticalCorrectnessEvaluation> task2Evaluations;
	private List<Paragraph> nflParagraphs;
	private Paragraph watchTapeActivity;
	private Paragraph visitGamesActivity;
	private Paragraph attendCombineActivity;
	private Paragraph talkToPlayerActivity;
	private Paragraph backgroundCheckActivity;
	private Paragraph talkToCoachesActivity;
	private Paragraph talkToFamilyActivity;
	private Paragraph draftPlayerActivity;
	private Paragraph semanticallyIncorrectNflDraftActivity;
	private Paragraph superfluousNflDraftActivity;

	private void addMentalEffortChoices(ComboInputAttribute mentalEffortCombo) {
		mentalEffortCombo.addChoice("Very High");
		mentalEffortCombo.addChoice("High");
		mentalEffortCombo.addChoice("Rather High");
		mentalEffortCombo.addChoice("Medium");
		mentalEffortCombo.addChoice("Rather Low");
		mentalEffortCombo.addChoice("Low");
		mentalEffortCombo.addChoice("Very Low");
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the first modeling task?", true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);

		ComboInputAttribute mentalEffortComboChangeTask = new ComboInputAttribute(
				"How would you assess the mental effort for completing the second modeling task?", true);
		addMentalEffortChoices(mentalEffortComboChangeTask);
		mentalEffortActivitiesModelingTask.add(mentalEffortComboChangeTask);

		return new SurveyActivity(mentalEffortActivitiesModelingTask);
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		WorkflowConfiguration configuration = new WorkflowConfiguration(5708);
		configuration.add(createDemographicSurveyActivity());
		configuration.add(NeedForCognitionQuestionnaire.createActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1));
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_2));
		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new FeedbackActivity());
		configurations.add(configuration);

		return configurations;
	}

	private IExperimentalWorkflowActivity createDemographicSurveyActivity() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		attributes.add(new StringInputAttribute("What is your current profession (e.g., student, consultant)?", true));

		ComboInputAttribute processModelingExpert = new ComboInputAttribute("I consider myself being a process modeling expert.", true);
		addChoices(processModelingExpert);
		attributes.add(processModelingExpert);

		attributes.add(new IntegerInputAttribute("How many years ago did you start process modeling?", true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many process models have you analyzed or read within the last 12 months? (A year has about 250 work days. In case you read one model per day, this would sum up to 250 models per year)",
						true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute("How many process models have you created or edited within the last 12 months?", true, 0,
				Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute("How many activities did all these models have on average?", true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many work days of formal training on process modeling have you received within the last 12 months? (This includes e.g. university lectures, certification courses, training courses. 15 weeks of a 90 minutes university lecture is roughly 3 work days)",
						true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many work days of self education have you made within the last 12 months? (This includes e.g. learning-by-doing, learning-on-the-fly, self-study of textbooks or specifications) ",
						true, 0, Integer.MAX_VALUE));

		addBPMNFamiliarity(attributes);

		ComboInputAttribute preFlight = new ComboInputAttribute("I am familiar with Pre-Flight Processes.", true);
		addChoices(preFlight);
		attributes.add(preFlight);

		ComboInputAttribute nfl = new ComboInputAttribute("I am familiar with the National Football League.", true);
		addChoices(nfl);
		attributes.add(nfl);

		return new SurveyActivity(attributes);
	}

	private List<ISemanticalCorrectnessEvaluation> createModelingTask1Evaluation() {
		if (task1Evaluations == null) {
			// Activity Evaluations
			task1Evaluations = new ArrayList<ISemanticalCorrectnessEvaluation>();
			double activityFoundValue = 1.0;

			task1Evaluations.add(new ActivityPresenceEvaluation(checkWeatherActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(fileFlightPlanActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(checkEngineActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(checkFuselageActivtiy, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(callClearanceDeliveryActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(getTaxiClearanceActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(announceTaxiingActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(taxiingActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(runUpChecksActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(getTakeOffClearanceActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(announceTakeOffActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(takeOffActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(superfluousActivity, activityFoundValue));
			task1Evaluations.add(new ActivityPresenceEvaluation(semanticallyIncorrectActivity, activityFoundValue));

			task1Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider
					.getEdgeCondition(EdgePresenceEvaluation.INCORRECT_EDGE_ID)));

			// more complex evaluations
			task1Evaluations.add(new InitialActivityEvaluation(checkWeatherActivity));

			task1Evaluations.add(new ParallelEvaluation(checkEngineActivity, checkFuselageActivtiy));
			EdgeCondition fileFlightPlanEdgeCondition = EdgeConditionProvider.getEdgeCondition(FILE_FLIGHT_PLAN);
			task1Evaluations.add(new ConditionEvaluation(fileFlightPlanEdgeCondition, fileFlightPlanActivity));
			EdgeCondition largeAirportEdgeCondition = EdgeConditionProvider.getEdgeCondition(LARGE_AIRPORT);
			task1Evaluations.add(new ConditionEvaluation(largeAirportEdgeCondition, callClearanceDeliveryActivity));

			EdgeCondition hasTowerClearancEdgeCondition = EdgeConditionProvider.getEdgeCondition(HAS_TOWER_CLEARANCE);
			task1Evaluations.add(new ConditionEvaluation(hasTowerClearancEdgeCondition, getTaxiClearanceActivity));

			EdgeCondition noTowerClearanceEdgeCondition = EdgeConditionProvider.getEdgeCondition(NO_TOWER_CLEARANCE);
			task1Evaluations.add(new ConditionEvaluation(noTowerClearanceEdgeCondition, announceTaxiingActivity));

			EdgeCondition hasTowerTakeOffEdgeCondition = EdgeConditionProvider.getEdgeCondition(HAS_TOWER_TAKE_OFF);
			task1Evaluations.add(new ConditionEvaluation(hasTowerTakeOffEdgeCondition, getTakeOffClearanceActivity));

			task1Evaluations.add(new DirectSuccessionEvaluation(taxiingActivity, runUpChecksActivity));

			EdgeCondition noTowerTakeOffEdgeCondition = EdgeConditionProvider.getEdgeCondition(NO_TOWER_TAKE_OFF);
			task1Evaluations.add(new ConditionEvaluation(noTowerTakeOffEdgeCondition, announceTakeOffActivity));

			task1Evaluations.add(new ExclusiveEvaluation(hasTowerClearancEdgeCondition, noTowerClearanceEdgeCondition));
			// task1Evaluations.add(new ExclusiveEvaluation(hasTowerTakeOffEdgeCondition, noTowerTakeOffEdgeCondition));
		}
		return task1Evaluations;
	}

	private List<ISemanticalCorrectnessEvaluation> createModelingTask2Evaluation() {
		if (task2Evaluations == null) {
			// Activity Evaluations
			task2Evaluations = new ArrayList<ISemanticalCorrectnessEvaluation>();
			task2Evaluations.add(new ActivityPresenceEvaluation(watchTapeActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(visitGamesActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(attendCombineActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(talkToPlayerActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(backgroundCheckActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(talkToCoachesActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(talkToFamilyActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(draftPlayerActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(superfluousNflDraftActivity));
			task2Evaluations.add(new ActivityPresenceEvaluation(semanticallyIncorrectNflDraftActivity));

			task2Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider.getEdgeCondition(WATCH_TAPE_LOOP)));
			task2Evaluations.add(new ParallelEvaluation(backgroundCheckActivity, talkToCoachesActivity, talkToFamilyActivity));
			task2Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider.getEdgeCondition(NOT_INTERESTED_AFTER_TALK)));
			task2Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider.getEdgeCondition(NOT_INTERESTED_EVALUATION)));
			task2Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider.getEdgeCondition(PLAYER_NOT_AVAILABLE)));
			task2Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider.getEdgeCondition(PLAYER_NOT_AVAILABLE_OR_NOT_INTERESTED),
					2));
			task2Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider
					.getEdgeCondition(EdgePresenceEvaluation.INCORRECT_EDGE_ID)));

		}
		return task2Evaluations;
	}

	private Collection<? extends Paragraph> createNflDraftParagraphs() throws Exception {
		if (nflParagraphs == null) {
			nflParagraphs = new ArrayList<Paragraph>();
			watchTapeActivity = createParagraph(MODELING_TASK_2, "Watch Tape", new RGB(255, 62, 62), nflParagraphs);
			visitGamesActivity = createParagraph(MODELING_TASK_2, "Visit Games", new RGB(249, 145, 145), nflParagraphs);
			attendCombineActivity = createParagraph(MODELING_TASK_2, "Attend NFL Combine", new RGB(251, 202, 54), nflParagraphs);
			talkToPlayerActivity = createParagraph(MODELING_TASK_2, "Talk to Player", new RGB(253, 227, 147), nflParagraphs);
			backgroundCheckActivity = createParagraph(MODELING_TASK_2, "Background Check", new RGB(182, 249, 38), nflParagraphs);
			talkToCoachesActivity = createParagraph(MODELING_TASK_2, "Talk to Coaches", new RGB(177, 252, 135), nflParagraphs);
			talkToFamilyActivity = createParagraph(MODELING_TASK_2, "Talk to Family", new RGB(43, 253, 201), nflParagraphs);
			draftPlayerActivity = createParagraph(MODELING_TASK_2, "Draft Player", new RGB(130, 156, 255), nflParagraphs);

			semanticallyIncorrectNflDraftActivity = createParagraph(MODELING_TASK_2,
					ActivityPresenceEvaluation.SEMANTICALLY_INCORRECT_ACTIVITY, new RGB(255, 0, 0), nflParagraphs);
			superfluousNflDraftActivity = createParagraph(MODELING_TASK_2, ActivityPresenceEvaluation.SUPERFLUOUS_ACTIVITY, new RGB(75, 75,
					75), nflParagraphs);
		}
		return nflParagraphs;
	}

	private Paragraph createParagraph(Process process, String name, RGB color, List<Paragraph> paragraphs) throws Exception {
		Paragraph paragraph = new Paragraph(process.getId(), name, color);
		paragraph.addPossibleActivityName(name);
		paragraphs.add(paragraph);
		return paragraph;
	}

	private Collection<? extends Paragraph> createPreFlightParagraphs() throws Exception {
		if (preFlightParagraphs == null) {
			preFlightParagraphs = new ArrayList<Paragraph>();
			checkWeatherActivity = createParagraph(MODELING_TASK_1, "Check Weather", new RGB(254, 62, 62), preFlightParagraphs);
			fileFlightPlanActivity = createParagraph(MODELING_TASK_1, "File Flight Plan", new RGB(254, 156, 156), preFlightParagraphs);
			checkEngineActivity = createParagraph(MODELING_TASK_1, "Check Engine", new RGB(255, 69, 235), preFlightParagraphs);
			checkFuselageActivtiy = createParagraph(MODELING_TASK_1, "Check Fuselage", new RGB(130, 69, 255), preFlightParagraphs);
			callClearanceDeliveryActivity = createParagraph(MODELING_TASK_1, "Call Clearance Delivery", new RGB(187, 154, 255),
					preFlightParagraphs);
			getTaxiClearanceActivity = createParagraph(MODELING_TASK_1, "Get Taxi Clearance", new RGB(49, 181, 249), preFlightParagraphs);
			announceTaxiingActivity = createParagraph(MODELING_TASK_1, "Announce Taxiing", new RGB(139, 214, 252), preFlightParagraphs);
			taxiingActivity = createParagraph(MODELING_TASK_1, "Taxiing", new RGB(37, 254, 129), preFlightParagraphs);
			runUpChecksActivity = createParagraph(MODELING_TASK_1, "Perform Run-Up Checks", new RGB(151, 254, 95), preFlightParagraphs);
			getTakeOffClearanceActivity = createParagraph(MODELING_TASK_1, "Get Take-Off Clearance", new RGB(236, 254, 39),
					preFlightParagraphs);
			announceTakeOffActivity = createParagraph(MODELING_TASK_1, "Announce Take Off", new RGB(254, 165, 61), preFlightParagraphs);
			takeOffActivity = createParagraph(MODELING_TASK_1, "Take Off", new RGB(255, 200, 136), preFlightParagraphs);

			semanticallyIncorrectActivity = createParagraph(MODELING_TASK_1, ActivityPresenceEvaluation.SEMANTICALLY_INCORRECT_ACTIVITY,
					new RGB(255, 0, 0), preFlightParagraphs);
			superfluousActivity = createParagraph(MODELING_TASK_1, ActivityPresenceEvaluation.SUPERFLUOUS_ACTIVITY, new RGB(75, 75, 75),
					preFlightParagraphs);

		}
		return preFlightParagraphs;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		paragraphs.addAll(createPreFlightParagraphs());
		paragraphs.addAll(createNflDraftParagraphs());
		return paragraphs;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(MODELING_TASK_1);
		processes.add(MODELING_TASK_2);

		return processes;
	}

	@Override
	public List<ISemanticalCorrectnessEvaluation> getSemanticalCorrectnessEvaluations(String modelingProcess) {
		// ensure that the paragraphs are initialized
		try {
			getParagraphs();
		} catch (Exception e) {
			Activator.logError("Could not create the paragraphs.", e);
			return Collections.emptyList();
		}

		Assert.isNotNull(modelingProcess);
		if (modelingProcess.equals(MODELING_TASK_1.getId())) {
			return createModelingTask1Evaluation();
		}
		if (modelingProcess.equals(MODELING_TASK_2.getId())) {
			return createModelingTask2Evaluation();
		}

		return Collections.emptyList();
	}
}
