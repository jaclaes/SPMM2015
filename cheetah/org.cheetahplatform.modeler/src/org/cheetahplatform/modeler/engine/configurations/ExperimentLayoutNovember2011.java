package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivityWithLayout;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.LayoutTutorialActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ActivityPresenceEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.AndEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ConditionEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.DirectSuccessionEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.EdgePresenceEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ExclusiveEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ISemanticalCorrectnessEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.InitialActivityEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.LoopEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.OrEvalation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ParallelEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.SynchronizeEvaluation;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.EdgeConditionProvider;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.RGB;

public class ExperimentLayoutNovember2011 extends AbstractExperimentalConfiguration {
	public static final Process MODELING_TASK_1_WITH_LAYOUT = new Process("modeling_task1_with_layout_2.0");
	public static final Process MODELING_TASK_2_WITH_LAYOUT = new Process("modeling_task2_with_layout_2.0");
	public static final Process MODELING_TASK_1_WITHOUT_LAYOUT = new Process("modeling_task1_without_layout_2.0");
	public static final Process MODELING_TASK_2_WITHOUT_LAYOUT = new Process("modeling_task2_without_layout_2.0");
	public static final Process EXPERIMENTAL_PROCESS = new Process("layout_2.0");

	private static final String MODIFICATION_INITIALGRAPH = "layout_2.0/2011_10_transport_of_equipment.mxml";

	public static final int ALL_CUSTOMER_INFORMATION_AVAILABLE = 1001;
	public static final int MISSING_CUSTOMER_INFORMATION = 1002;
	public static final int BANK_REFUSES_MORTGAGE = 1003;
	public static final int BANK_ACCEPTS_MORTGAGE = 1004;
	public static final int CUSTOMER_REJECTS_OFFER = 1005;
	public static final int CUSTOMER_ACCEPTS_OFFER = 1006;
	public static final int NO_PREVIOUS_MORTGAGE = 1007;
	public static final int ONE_PREVIOUS_MORTGAGE = 1008;
	public static final int MORE_THAN_ONE_PREVIOUS_MORTGAGE = 1009;
	public static final int LESS_THAN_ONE_PREVIOUS_MORTGAGE = 1010;
	public static final int MORTGAGE_LESS_THAN_ONE_MIO = 1011;
	public static final int MORTGAGE_GREATER_OR_EQUAL_ONE_MIO = 1012;
	public static final int UPDATE_OFFER = 1013;
	public static final int UPDATE_OFFER_2_POINTS = 1016;
	public static final int DO_NOT_UPDATE_OFFER = 1014;
	public static final int LOOP_BACK_EDGE = 1015;

	public static void main(String[] args) throws Exception {
		// IDatabaseConnector connector = org.cheetahplatform.common.Activator.getDatabaseConnector();
		// connector.setDatabaseURL("jdbc:mysql://138.232.65.123:13306/ibm_layout");
		// connector.setAdminCredentials("ibm_layout_admin", "gfhsjk452");
		//
		// ExperimentLayoutNovember2011 experimentLayoutNovember2011 = new ExperimentLayoutNovember2011();
		// List<Paragraph> paragraphs = experimentLayoutNovember2011.getParagraphs();
		//
		// for (Paragraph paragraph : paragraphs) {
		// ParagraphProvider.save(connector.getDatabaseConnection(), paragraph);
		// System.out.println(paragraph);
		// }
	}

	private Paragraph makeInitialRequest;
	private Paragraph checkIfCustomerInformationIsAvailable;
	private Paragraph contactCustomerForAdditionalInformation;
	private Paragraph calculateAvailableFunds;
	private Paragraph calculateAnnualIncome;
	private Paragraph calculateRequiredFunds;
	private Paragraph semanticallyIncorrectTask1;
	private Paragraph superfluousTask1;
	private Paragraph queryDatabase;
	private Paragraph registerApplicationLocally;
	private Paragraph informHeadQuarters;
	private Paragraph assessMortgageValueToPropertyValue;
	private Paragraph checkEmployment;
	private Paragraph checkPaymentHistory;
	private Paragraph makeDecision;
	private Paragraph makeDecisionEmployee1;
	private Paragraph makeDecisionEmployee2;
	private Paragraph meetAndDiscuss;
	private Paragraph prepareOffer;
	private Paragraph sendOffer;
	private Paragraph evaluateResponseForm;
	private Paragraph contactCustomer;
	private Paragraph evaluateResponse;
	private Paragraph updateOffer;
	private Paragraph sendRejectionLetter;
	private Paragraph closeApplication;

	private Paragraph makeMoneyAvailable;
	private ArrayList<Paragraph> paragraphs;
	private ArrayList<ISemanticalCorrectnessEvaluation> task1Evaluations;

	public void addActivityPresenceEvaluationsForTask1() {
		addPresenceEvaluation(makeInitialRequest, task1Evaluations);
		addPresenceEvaluation(checkIfCustomerInformationIsAvailable, task1Evaluations);
		addPresenceEvaluation(contactCustomerForAdditionalInformation, task1Evaluations);

		addPresenceEvaluation(calculateAnnualIncome, task1Evaluations);
		addPresenceEvaluation(calculateRequiredFunds, task1Evaluations);
		addPresenceEvaluation(calculateAvailableFunds, task1Evaluations);

		addPresenceEvaluation(queryDatabase, task1Evaluations);
		addPresenceEvaluation(registerApplicationLocally, task1Evaluations);
		addPresenceEvaluation(informHeadQuarters, task1Evaluations);

		addPresenceEvaluation(assessMortgageValueToPropertyValue, task1Evaluations);
		addPresenceEvaluation(checkEmployment, task1Evaluations);
		addPresenceEvaluation(checkPaymentHistory, task1Evaluations);

		addPresenceEvaluation(makeDecision, task1Evaluations);
		addPresenceEvaluation(makeDecisionEmployee1, task1Evaluations);
		addPresenceEvaluation(makeDecisionEmployee2, task1Evaluations);
		addPresenceEvaluation(meetAndDiscuss, task1Evaluations);

		addPresenceEvaluation(prepareOffer, task1Evaluations);
		addPresenceEvaluation(sendOffer, task1Evaluations);
		addPresenceEvaluation(evaluateResponseForm, task1Evaluations);

		addPresenceEvaluation(contactCustomer, task1Evaluations);
		addPresenceEvaluation(evaluateResponse, task1Evaluations);
		addPresenceEvaluation(updateOffer, task1Evaluations);
		addPresenceEvaluation(makeMoneyAvailable, task1Evaluations);
		addPresenceEvaluation(sendRejectionLetter, task1Evaluations);
		addPresenceEvaluation(closeApplication, task1Evaluations);

		addPresenceEvaluation(semanticallyIncorrectTask1, task1Evaluations);
		addPresenceEvaluation(superfluousTask1, task1Evaluations);
	}

	public void addMentalEffortQuestion(List<SurveyAttribute> questions, boolean mandatory) {
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the modeling task?", mandatory);
		SurveyUtil.addMentalEffortChoices(mentalEffortCombo);
		questions.add(mentalEffortCombo);
	}

	private void addPresenceEvaluation(Paragraph paragraph, List<ISemanticalCorrectnessEvaluation> evaluations) {
		evaluations.add(new ActivityPresenceEvaluation(paragraph, 0.33));
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private WorkflowConfiguration createConfiguration1() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(8262);
		configuration.add(ExperimentLayout.createDemographicSurveyActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new LayoutTutorialActivity());
		configuration.add(new BPMNModelingActivityWithLayout(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1_WITH_LAYOUT));
		configuration.add(createPostModelingQuestionsForLayoutTask(true));

		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + MODIFICATION_INITIALGRAPH),
				new HashMap<Object, Object>());
		try {
			configuration.add(new BPMNModelingActivity(input.openStream(), graph, MODELING_TASK_2_WITHOUT_LAYOUT, true));
		} catch (Exception e) {
			Activator.logError("Could not create configuration for change task", e);
		}

		configuration.add(createPostModelingQuestionsForNonLayoutTask(false));

		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating in the modeling session :-)"));

		return configuration;
	}

	private WorkflowConfiguration createConfiguration2() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(9423);

		configuration.add(ExperimentLayout.createDemographicSurveyActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1_WITHOUT_LAYOUT));
		configuration.add(createPostModelingQuestionsForNonLayoutTask(true));
		configuration.add(new LayoutTutorialActivity());

		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + MODIFICATION_INITIALGRAPH),
				new HashMap<Object, Object>());
		try {
			configuration.add(new BPMNModelingActivityWithLayout(input.openStream(), graph, MODELING_TASK_2_WITH_LAYOUT, true));
		} catch (Exception e) {
			Activator.logError("Could not create configuration for change task", e);
		}

		configuration.add(createPostModelingQuestionsForLayoutTask(false));

		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating in the modeling session :-)"));

		return configuration;
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		configurations.add(createConfiguration1());
		configurations.add(createConfiguration2());

		return configurations;
	}

	private Paragraph createParagraph(String name, RGB color, List<Paragraph> paragraphs) throws Exception {
		String processId1 = MODELING_TASK_1_WITH_LAYOUT.getId();
		String processId2 = MODELING_TASK_1_WITHOUT_LAYOUT.getId();

		Paragraph paragraph1 = new Paragraph(processId1, name, color);
		paragraph1.addPossibleActivityName(name);
		paragraphs.add(paragraph1);

		Paragraph paragraph2 = new Paragraph(processId2, name, color);
		paragraph2.addPossibleActivityName(name);
		paragraphs.add(paragraph2);

		return paragraph1;
	}

	private List<SurveyAttribute> createPerceivedSemanticQualityQuestions(boolean mandatory) {
		ArrayList<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.add(SurveyUtil.createLikertQuestion("The conceptual model represents the business process correctly.", mandatory));
		attributes.add(SurveyUtil.createLikertQuestion("The conceptual model is a realistic representation of the business process.",
				mandatory));
		attributes.add(SurveyUtil.createLikertQuestion(
				"All the elements in the conceptual model are relevant for the representation of the business process.", mandatory));
		attributes.add(SurveyUtil.createLikertQuestion("The conceptual model gives a complete representation of the business process.",
				mandatory));
		return attributes;
	}

	private IExperimentalWorkflowActivity createPostModelingQuestionsForLayoutTask(boolean mandatory) {
		List<SurveyAttribute> questions = new ArrayList<SurveyAttribute>();

		questions.addAll(createPerceivedSemanticQualityQuestions(mandatory));
		addMentalEffortQuestion(questions, mandatory);
		questions.add(new MessageSurveyAttribute("separator_1", ""));
		questions.add(new MessageSurveyAttribute("separator_2", ""));
		questions
				.add(new MessageSurveyAttribute(
						"usefulness_ease_of_use",
						"For the following questions, please imagine that you are working as a process modeler. Please, assess whether the automated layout feature would help you in your daily job as a process modeler."));
		questions.addAll(new PerceivedUsefulnessQuestionnaire().createQuestions("the automated layout feature", mandatory));
		questions.addAll(new PerceivedEaseOfUseQuestionnaire().createQuestions("the automated layout feature", mandatory));

		return new SurveyActivity(questions);
	}

	private IExperimentalWorkflowActivity createPostModelingQuestionsForNonLayoutTask(boolean mandatory) {
		List<SurveyAttribute> questions = new ArrayList<SurveyAttribute>();

		questions.addAll(createPerceivedSemanticQualityQuestions(mandatory));
		addMentalEffortQuestion(questions, mandatory);
		return new SurveyActivity(questions);
	}

	private List<ISemanticalCorrectnessEvaluation> createTask1Evaluations() {
		if (task1Evaluations == null) {
			task1Evaluations = new ArrayList<ISemanticalCorrectnessEvaluation>();

			addActivityPresenceEvaluationsForTask1();

			task1Evaluations.add(new InitialActivityEvaluation(makeInitialRequest));
			task1Evaluations.add(new DirectSuccessionEvaluation(makeInitialRequest, checkIfCustomerInformationIsAvailable));
			task1Evaluations.add(new LoopEvaluation(checkIfCustomerInformationIsAvailable, contactCustomerForAdditionalInformation));

			task1Evaluations.add(new ParallelEvaluation(calculateAvailableFunds, calculateAnnualIncome, calculateRequiredFunds));
			task1Evaluations.add(new SynchronizeEvaluation(queryDatabase, calculateAnnualIncome, calculateAvailableFunds,
					calculateRequiredFunds));

			EdgeCondition moreThanOneMortgage = EdgeConditionProvider.getEdgeCondition(MORE_THAN_ONE_PREVIOUS_MORTGAGE);
			EdgeCondition lessThanOneMortgage = EdgeConditionProvider.getEdgeCondition(LESS_THAN_ONE_PREVIOUS_MORTGAGE);
			task1Evaluations.add(new ConditionEvaluation(moreThanOneMortgage, sendRejectionLetter));
			task1Evaluations.add(new ConditionEvaluation(lessThanOneMortgage, registerApplicationLocally));

			EdgeCondition onePreviousMortgage = EdgeConditionProvider.getEdgeCondition(ONE_PREVIOUS_MORTGAGE);
			EdgeCondition noPreviousMortgage = EdgeConditionProvider.getEdgeCondition(NO_PREVIOUS_MORTGAGE);
			task1Evaluations.add(new ExclusiveEvaluation(onePreviousMortgage, noPreviousMortgage));
			task1Evaluations.add(new ConditionEvaluation(onePreviousMortgage, informHeadQuarters));

			task1Evaluations.add(new ParallelEvaluation(assessMortgageValueToPropertyValue, checkEmployment, checkPaymentHistory));

			EdgeCondition greaterOneMillion = EdgeConditionProvider.getEdgeCondition(MORTGAGE_GREATER_OR_EQUAL_ONE_MIO);
			EdgeCondition lessThanOneMillion = EdgeConditionProvider.getEdgeCondition(MORTGAGE_LESS_THAN_ONE_MIO);

			task1Evaluations.add(new ExclusiveEvaluation(greaterOneMillion, lessThanOneMillion));

			ExclusiveEvaluation makeDecision1 = new ExclusiveEvaluation(null, null, makeDecision, makeDecisionEmployee1);
			ExclusiveEvaluation makeDecision2 = new ExclusiveEvaluation(null, null, makeDecision, makeDecisionEmployee2);
			ExclusiveEvaluation meetAndDiscussEvaluation = new ExclusiveEvaluation(null, null, makeDecision, meetAndDiscuss);
			task1Evaluations.add(new AndEvaluation("Decision Making", makeDecision1, makeDecision2, meetAndDiscussEvaluation));
			task1Evaluations.add(new ParallelEvaluation(makeDecisionEmployee1, makeDecisionEmployee2));

			EdgeCondition bankRefusesMortgage = EdgeConditionProvider.getEdgeCondition(BANK_REFUSES_MORTGAGE);
			EdgeCondition bankAcceptsMortgage = EdgeConditionProvider.getEdgeCondition(BANK_ACCEPTS_MORTGAGE);
			task1Evaluations.add(new ExclusiveEvaluation(bankRefusesMortgage, bankAcceptsMortgage));
			task1Evaluations.add(new ConditionEvaluation(bankRefusesMortgage, sendRejectionLetter));

			task1Evaluations.add(new DirectSuccessionEvaluation(prepareOffer, sendOffer));
			task1Evaluations.add(new DirectSuccessionEvaluation(sendOffer, evaluateResponseForm));

			EdgeCondition customerAcceptsOffer = EdgeConditionProvider.getEdgeCondition(CUSTOMER_ACCEPTS_OFFER);
			EdgeCondition customerRejectsOffer = EdgeConditionProvider.getEdgeCondition(CUSTOMER_REJECTS_OFFER);
			task1Evaluations.add(new ExclusiveEvaluation(customerAcceptsOffer, customerRejectsOffer));
			task1Evaluations.add(new ExclusiveEvaluation(null, null, contactCustomer, makeMoneyAvailable));

			task1Evaluations.add(new DirectSuccessionEvaluation(contactCustomer, evaluateResponse));
			task1Evaluations.add(new DirectSuccessionEvaluation(makeMoneyAvailable, closeApplication));

			EdgeCondition updateOfferCondition = EdgeConditionProvider.getEdgeCondition(UPDATE_OFFER);
			EdgeCondition updateOfferCondition2Points = EdgeConditionProvider.getEdgeCondition(UPDATE_OFFER_2_POINTS);
			EdgeCondition doNotUpdateOffer = EdgeConditionProvider.getEdgeCondition(DO_NOT_UPDATE_OFFER);
			ExclusiveEvaluation updateOffer1PointEvaluation = new ExclusiveEvaluation(updateOfferCondition, doNotUpdateOffer);
			ExclusiveEvaluation updateOffer2PointsEvaluation = new ExclusiveEvaluation(updateOfferCondition2Points, doNotUpdateOffer);
			task1Evaluations.add(new OrEvalation("Update Offer", updateOffer1PointEvaluation, updateOffer2PointsEvaluation));
			task1Evaluations.add(new ExclusiveEvaluation(null, null, updateOffer, sendRejectionLetter));

			task1Evaluations.add(new DirectSuccessionEvaluation(sendRejectionLetter, closeApplication));

			EdgeCondition loopBackToUpdateOffer = EdgeConditionProvider.getEdgeCondition(LOOP_BACK_EDGE);
			task1Evaluations.add(new EdgePresenceEvaluation(loopBackToUpdateOffer));
			task1Evaluations.add(new EdgePresenceEvaluation(updateOfferCondition2Points, 2));

			task1Evaluations.add(new EdgePresenceEvaluation(EdgeConditionProvider
					.getEdgeCondition(EdgePresenceEvaluation.INCORRECT_EDGE_ID)));
		}

		return task1Evaluations;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		Map<Process, String> map = new HashMap<Process, String>();
		map.put(MODELING_TASK_2_WITH_LAYOUT, MODIFICATION_INITIALGRAPH);
		map.put(MODELING_TASK_2_WITHOUT_LAYOUT, MODIFICATION_INITIALGRAPH);
		return map;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		if (paragraphs == null) {
			paragraphs = new ArrayList<Paragraph>();

			makeInitialRequest = createParagraph("Make initial request", new RGB(255, 255, 0), paragraphs);
			checkIfCustomerInformationIsAvailable = createParagraph("Check if all customer information is available", new RGB(255, 192, 0),
					paragraphs);
			contactCustomerForAdditionalInformation = createParagraph("Contact customer to get necessary information",
					new RGB(255, 80, 80), paragraphs);
			calculateAvailableFunds = createParagraph("Calculate available funds", new RGB(153, 0, 51), paragraphs);
			calculateAnnualIncome = createParagraph("Calculate annual income", new RGB(54, 96, 146), paragraphs);
			calculateRequiredFunds = createParagraph("Calculate required funds", new RGB(0, 153, 0), paragraphs);
			queryDatabase = createParagraph("Query Database", new RGB(100, 153, 0), paragraphs);

			registerApplicationLocally = createParagraph("Register application locally", new RGB(255, 192, 0), paragraphs);
			informHeadQuarters = createParagraph("Inform head quarters", new RGB(255, 80, 80), paragraphs);

			assessMortgageValueToPropertyValue = createParagraph("Assess Mortgage Value to Property Value", new RGB(249, 255, 80),
					paragraphs);
			checkEmployment = createParagraph("Check Employment", new RGB(109, 255, 80), paragraphs);
			checkPaymentHistory = createParagraph("Check Payment History", new RGB(80, 255, 241), paragraphs);

			makeDecision = createParagraph("Make Decision", new RGB(80, 117, 255), paragraphs);
			makeDecisionEmployee1 = createParagraph("Make Decision Employee 1", new RGB(162, 80, 255), paragraphs);
			makeDecisionEmployee2 = createParagraph("Make Decision Employee 2", new RGB(241, 80, 255), paragraphs);
			meetAndDiscuss = createParagraph("Meet and Discuss", new RGB(255, 80, 134), paragraphs);

			prepareOffer = createParagraph("Prepare Offer", new RGB(251, 171, 196), paragraphs);
			sendOffer = createParagraph("Send Offer", new RGB(244, 171, 251), paragraphs);
			evaluateResponseForm = createParagraph("Evaluate Response Form", new RGB(188, 171, 251), paragraphs);

			contactCustomer = createParagraph("Contact Customer", new RGB(171, 235, 251), paragraphs);
			evaluateResponse = createParagraph("Evaluate Response", new RGB(171, 251, 205), paragraphs);
			updateOffer = createParagraph("Update Offer", new RGB(224, 251, 171), paragraphs);
			sendRejectionLetter = createParagraph("Send Reject. Letter", new RGB(251, 243, 171), paragraphs);
			closeApplication = createParagraph("Close Application", new RGB(251, 211, 171), paragraphs);
			makeMoneyAvailable = createParagraph("Make Money Available", new RGB(251, 173, 171), paragraphs);

			semanticallyIncorrectTask1 = createParagraph(ActivityPresenceEvaluation.SEMANTICALLY_INCORRECT_ACTIVITY, new RGB(255, 0, 0),
					paragraphs);
			superfluousTask1 = createParagraph(ActivityPresenceEvaluation.SUPERFLUOUS_ACTIVITY, new RGB(75, 75, 75), paragraphs);
		}
		return paragraphs;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(MODELING_TASK_1_WITH_LAYOUT);
		processes.add(MODELING_TASK_2_WITH_LAYOUT);
		processes.add(MODELING_TASK_1_WITHOUT_LAYOUT);
		processes.add(MODELING_TASK_2_WITHOUT_LAYOUT);
		return processes;
	}

	@Override
	public List<ISemanticalCorrectnessEvaluation> getSemanticalCorrectnessEvaluations(String modelingProcess) {
		try {
			getParagraphs();
		} catch (Exception e) {
			Activator.logError("Could not create the paragraphs.", e);
			return Collections.emptyList();
		}

		return createTask1Evaluations();
	}
}
