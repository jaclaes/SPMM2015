package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
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
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ConditionEvaluation;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.DirectSuccessionEvaluation;
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
import org.cheetahplatform.modeler.graph.mapping.ParagraphProvider;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.MultiLineStringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.graphics.RGB;

public class ExperimentLayout extends AbstractExperimentalConfiguration {
	public static final Process MODELING_TASK_1_WITH_LAYOUT = new Process("modeling_task1_with_layout_1.0");
	public static final Process MODELING_TASK_2_WITH_LAYOUT = new Process("modeling_task2_with_layout_1.0");
	public static final Process MODELING_TASK_1_WITHOUT_LAYOUT = new Process("modeling_task1_without_layout_1.0");
	public static final Process MODELING_TASK_2_WITHOUT_LAYOUT = new Process("modeling_task2_without_layout_1.0");
	public static final Process EXPERIMENTAL_PROCESS = new Process("layout_1.0");

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

	public static IExperimentalWorkflowActivity createDemographicSurveyActivity() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		attributes.add(new IntegerInputAttribute("How many years ago did you start process modeling?", true, 0, Integer.MAX_VALUE));
		attributes
				.add(new IntegerInputAttribute(
						"How many process models have you analyzed or read within the last 12 months? (A year has about 250 work days. In case you read one model per day, this would sum up to 250 models per year)",
						true, 0, Integer.MAX_VALUE));
		attributes.add(new IntegerInputAttribute("How many process model have you created or edited within the last 12 months", true, 0,
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

		ComboInputAttribute preFlight = new ComboInputAttribute("I am very familiar with mortgage processes.", true);
		addChoices(preFlight);
		attributes.add(preFlight);

		return new SurveyActivity(attributes);
	}

	public static void main(String[] args) throws Exception {
		IDatabaseConnector connector = Activator.getDatabaseConnector();
		// connector.setDatabaseURL("jdbc:mysql://localhost/ibm_layout");
		// connector.setAdminCredentials("root", "mysql");

		ExperimentLayout experimentLayout = new ExperimentLayout();
		// experimentLayout.paragraphs = new ArrayList<Paragraph>();
		// experimentLayout.createVerificationParagraphs();
		// System.out.println(experimentLayout.paragraphs.size());
		//

		ArrayList<Paragraph> paragraphs = new ArrayList<Paragraph>();
		experimentLayout.createParagraph(1, "Calculate required *and* available funds", new RGB(0, 153, 0), paragraphs);

		for (Paragraph paragraph : paragraphs) {
			ParagraphProvider.save(connector.getDatabaseConnection(), paragraph);
			System.out.println(paragraph);
		}
	}

	private Paragraph makeInitialRequest;
	private Paragraph checkIfCustomerInformationIsAvailable;
	private Paragraph contactCustomerForAdditionalInformation;
	private Paragraph calculateAvailableFunds;
	private Paragraph calculateAnnualIncome;
	private Paragraph calculateRequiredFunds;
	private Paragraph sendRejectionLetter;
	private Paragraph makeOfferToCustomer;
	private Paragraph sendDocuments;
	private Paragraph payOutMortgage;
	private Paragraph contactCustomerForReasonOfRejectingOffer;
	private Paragraph closeApplication;

	private Paragraph verifyCalculations;
	private Paragraph calculateAvailableFundsAndIncome;
	public static final int ALL_CUSTOMER_INFORMATION_AVAILABLE = 1;
	public static final int MISSING_CUSTOMER_INFORMATION = 2;
	public static final int BANK_REFUSES_MORTGAGE = 3;
	public static final int BANK_ACCEPTS_MORTGAGE = 4;
	public static final int CUSTOMER_REJECTS_OFFER = 5;
	public static final int CUSTOMER_ACCEPTS_OFFER = 6;
	public static final int NO_PREVIOUS_MORTGAGE = 7;
	public static final int ONE_PREVIOUS_MORTGAGE = 8;
	public static final int MORE_THAN_ONE_PREVIOUS_MORTGAGE = 9;
	public static final int ONE_CHECK_NEGATIVE = 10;
	public static final int ALL_CHECKS_POSITIVE = 11;

	public static final int MORTGAGE_LESS_THAN_ONE_MIO = 12;
	public static final int MORTGAGE_GREATER_OR_EQUAL_ONE_MIO = 13;
	private List<Paragraph> paragraphs;
	private Paragraph makeOfferAndSendDocuments;
	private Paragraph semanticallyIncorrectTask1;
	private Paragraph superfluousTask1;
	private List<ISemanticalCorrectnessEvaluation> task1Evaluations;
	private ArrayList<ISemanticalCorrectnessEvaluation> task2Evaluations;
	private Paragraph checkIfCustomerHasMortgage;
	private Paragraph registerApplicationLocally;
	private Paragraph semanticallyIncorrectTask2;
	private Paragraph superfluousTask2;
	private Paragraph informHeadQuarters;
	private Paragraph rejectAndCloseApplication;
	private Paragraph checkIfMortgageSmaller80Percent;
	private Paragraph checkIfApplicantIsEmployed;
	private Paragraph checkIfBlackListed;
	private Paragraph evaluateResults;
	private Paragraph analyzeMortgageInDetail;
	private Paragraph confirmMortgage;
	private Paragraph requestSupervisorApproval;
	private Paragraph registerGeneralInformation;
	private Paragraph confirmAndRequestSupervisorApproval;
	private Paragraph registerGeneralInformationAndAnalyzeInDetail;
	private Paragraph sendRejectionLetterAndCloseApplication;
	private Paragraph contactCustomerAndCloseApplication;

	private Paragraph calculateRequiredAndAvailableFunds;

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
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.add(new MessageSurveyAttribute("first_modeling_task", "Regarding the first modeling task:"));
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the first modeling task?", true);
		addMentalEffortChoices(mentalEffortCombo);
		attributes.add(mentalEffortCombo);

		ComboInputAttribute difficultyTask1 = new ComboInputAttribute("How would you assess the difficulty of the first modeling task?",
				true);
		addMentalEffortChoices(difficultyTask1);
		attributes.add(difficultyTask1);

		ComboInputAttribute perceivedQuality1 = new ComboInputAttribute("I am very satisfied with the first business process I modeled.",
				true);
		SurveyUtil.addSevenPointLikertScaleChoices(perceivedQuality1);
		attributes.add(perceivedQuality1);

		attributes.add(new MessageSurveyAttribute("separator_1", ""));
		attributes.add(new MessageSurveyAttribute("separator_2", ""));
		attributes.add(new MessageSurveyAttribute("separator_3", ""));

		attributes.add(new MessageSurveyAttribute("second_modeling_task", "Regarding the second modeling task:"));
		ComboInputAttribute mentalEffortComboChangeTask = new ComboInputAttribute(
				"How would you assess the mental effort for completing the second modeling task?", true);
		addMentalEffortChoices(mentalEffortComboChangeTask);
		attributes.add(mentalEffortComboChangeTask);

		ComboInputAttribute difficultyTask2 = new ComboInputAttribute("How would you assess the difficulty of the second modeling task?",
				true);
		addMentalEffortChoices(difficultyTask2);
		attributes.add(difficultyTask2);

		ComboInputAttribute perceivedQuality2 = new ComboInputAttribute("I am very satisfied with the second business process I modeled.",
				true);
		SurveyUtil.addSevenPointLikertScaleChoices(perceivedQuality2);
		attributes.add(perceivedQuality2);

		attributes.add(new MessageSurveyAttribute("separator_4", ""));
		attributes.add(new MessageSurveyAttribute("separator_5", ""));
		attributes.add(new MessageSurveyAttribute("separator_6", ""));

		attributes.add(new MessageSurveyAttribute("general_questions", "And, finally, some general questions regarding the layout:"));

		ComboInputAttribute layoutSatisfaction = new ComboInputAttribute("I consider the layout feature very useful.", true);
		SurveyUtil.addSevenPointLikertScaleChoices(layoutSatisfaction);
		attributes.add(layoutSatisfaction);

		ComboInputAttribute adaptLayout = new ComboInputAttribute(
				"I had to adapt the layout very often, i.e., rearrange parts of the model after layouting.", true);
		SurveyUtil.addSevenPointLikertScaleChoices(adaptLayout);
		attributes.add(adaptLayout);

		ComboInputAttribute adaptBehavior = new ComboInputAttribute(
				"I had to adapt my usual way of modeling very often to make use of the layout feature.", true);
		SurveyUtil.addSevenPointLikertScaleChoices(adaptBehavior);
		attributes.add(adaptBehavior);

		MultiLineStringInputAttribute adaptBehaviorExplanation = new MultiLineStringInputAttribute(
				"If you had to adapt your behavior to make use of the layout feature, please explain how.", false);
		attributes.add(adaptBehaviorExplanation);

		return new SurveyActivity(attributes);
	}

	private WorkflowConfiguration createConfiguration1() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(1861);
		configuration.add(createDemographicSurveyActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new LayoutTutorialActivity());
		configuration.add(new BPMNModelingActivityWithLayout(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1_WITH_LAYOUT));
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_2_WITHOUT_LAYOUT));
		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating in the modeling session :-)"));

		return configuration;
	}

	private WorkflowConfiguration createConfiguration2() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(7509);

		configuration.add(createDemographicSurveyActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1_WITHOUT_LAYOUT));
		configuration.add(new LayoutTutorialActivity());
		configuration.add(new BPMNModelingActivityWithLayout(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_2_WITH_LAYOUT));
		configuration.add(createCognitiveLoadQuestions());
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

	protected void createMortgageParagraphs() throws Exception {
		makeInitialRequest = createParagraph(1, "Make initial request", new RGB(255, 255, 0), paragraphs);
		checkIfCustomerInformationIsAvailable = createParagraph(1, "Check if all customer information is available", new RGB(255, 192, 0),
				paragraphs);
		contactCustomerForAdditionalInformation = createParagraph(1, "Contact customer to get necessary information", new RGB(255, 80, 80),
				paragraphs);
		calculateAvailableFunds = createParagraph(1, "Calculate available funds", new RGB(153, 0, 51), paragraphs);
		calculateAnnualIncome = createParagraph(1, "Calculate annual income", new RGB(54, 96, 146), paragraphs);
		calculateAvailableFundsAndIncome = createParagraph(1, "Calculate available funds *and* income", new RGB(153, 0, 51), paragraphs);
		calculateRequiredFunds = createParagraph(1, "Calculate required funds", new RGB(0, 153, 0), paragraphs);
		calculateRequiredAndAvailableFunds = createParagraph(1, "Calculate required *and* available funds", new RGB(0, 153, 0), paragraphs);
		verifyCalculations = createParagraph(1, "Verify calculations", new RGB(102, 51, 0), paragraphs);
		sendRejectionLetter = createParagraph(1, "Send rejection letter", new RGB(128, 128, 128), paragraphs);
		sendRejectionLetterAndCloseApplication = createParagraph(1, "Send rejection letter *and* close application",
				new RGB(128, 128, 128), paragraphs);
		closeApplication = createParagraph(1, "Close application", new RGB(0, 0, 0), paragraphs);
		makeOfferToCustomer = createParagraph(1, "Make offer to customer", new RGB(146, 208, 80), paragraphs);
		sendDocuments = createParagraph(1, "Send documents", new RGB(83, 141, 213), paragraphs);
		makeOfferAndSendDocuments = createParagraph(1, "Make offer *and* send documents", new RGB(146, 208, 80), paragraphs);
		payOutMortgage = createParagraph(1, "Pay out mortgage", new RGB(96, 73, 122), paragraphs);
		contactCustomerForReasonOfRejectingOffer = createParagraph(1, "Contact customer for information why he did not accept the offer",
				new RGB(255, 102, 255), paragraphs);
		contactCustomerAndCloseApplication = createParagraph(1,
				"Contact customer for information why he did not accept the offer *and* close application", new RGB(255, 102, 255),
				paragraphs);

		semanticallyIncorrectTask1 = createParagraph(1, ActivityPresenceEvaluation.SEMANTICALLY_INCORRECT_ACTIVITY, new RGB(255, 0, 0),
				paragraphs);
		superfluousTask1 = createParagraph(1, ActivityPresenceEvaluation.SUPERFLUOUS_ACTIVITY, new RGB(75, 75, 75), paragraphs);
	}

	private Paragraph createParagraph(int process, String name, RGB color, List<Paragraph> paragraphs) throws Exception {
		String processId1 = null;
		String processId2 = null;

		if (process == 1) {
			processId1 = MODELING_TASK_1_WITH_LAYOUT.getId();
			processId2 = MODELING_TASK_1_WITHOUT_LAYOUT.getId();
		} else {
			processId1 = MODELING_TASK_2_WITH_LAYOUT.getId();
			processId2 = MODELING_TASK_2_WITHOUT_LAYOUT.getId();
		}

		Paragraph paragraph1 = new Paragraph(processId1, name, color);
		paragraph1.addPossibleActivityName(name);
		paragraphs.add(paragraph1);

		Paragraph paragraph2 = new Paragraph(processId2, name, color);
		paragraph2.addPossibleActivityName(name);
		paragraphs.add(paragraph2);

		return paragraph1;
	}

	private Collection<? extends Paragraph> createParagraphs() throws Exception {
		if (paragraphs == null) {
			paragraphs = new ArrayList<Paragraph>();
			createMortgageParagraphs();
			createVerificationParagraphs();
		}

		return paragraphs;
	}

	private List<ISemanticalCorrectnessEvaluation> createTask1Evaluations() {
		if (task1Evaluations == null) {
			task1Evaluations = new ArrayList<ISemanticalCorrectnessEvaluation>();
			EdgeCondition bankRefusesCondition = EdgeConditionProvider.getEdgeCondition(BANK_REFUSES_MORTGAGE);
			EdgeCondition bankAcceptsCondition = EdgeConditionProvider.getEdgeCondition(BANK_ACCEPTS_MORTGAGE);
			EdgeCondition customerRejectsCondition = EdgeConditionProvider.getEdgeCondition(CUSTOMER_REJECTS_OFFER);
			EdgeCondition customerAcceptsCondition = EdgeConditionProvider.getEdgeCondition(CUSTOMER_ACCEPTS_OFFER);

			// activity presence
			task1Evaluations.add(new ActivityPresenceEvaluation(makeInitialRequest));
			task1Evaluations.add(new ActivityPresenceEvaluation(checkIfCustomerInformationIsAvailable));
			task1Evaluations.add(new ActivityPresenceEvaluation(contactCustomerForAdditionalInformation));
			task1Evaluations.add(new OrEvalation("Calculate available funds", new ActivityPresenceEvaluation(calculateAvailableFunds),
					new ActivityPresenceEvaluation(calculateAvailableFundsAndIncome)));
			task1Evaluations.add(new OrEvalation("Calculate annual income", new ActivityPresenceEvaluation(calculateAnnualIncome),
					new ActivityPresenceEvaluation(calculateAvailableFundsAndIncome)));
			task1Evaluations.add(new ActivityPresenceEvaluation(calculateRequiredFunds));
			task1Evaluations.add(new ActivityPresenceEvaluation(verifyCalculations));
			task1Evaluations.add(new OrEvalation("Send rejection letter", new ActivityPresenceEvaluation(sendRejectionLetter),
					new ActivityPresenceEvaluation(sendRejectionLetterAndCloseApplication)));
			task1Evaluations.add(new OrEvalation("Close application", new ActivityPresenceEvaluation(closeApplication),
					new ActivityPresenceEvaluation(sendRejectionLetterAndCloseApplication), new ActivityPresenceEvaluation(
							contactCustomerAndCloseApplication)));
			task1Evaluations.add(new OrEvalation("Make offer to customer", new ActivityPresenceEvaluation(makeOfferToCustomer),
					new ActivityPresenceEvaluation(makeOfferAndSendDocuments)));
			task1Evaluations.add(new OrEvalation("Send documents", new ActivityPresenceEvaluation(sendDocuments),
					new ActivityPresenceEvaluation(makeOfferAndSendDocuments)));
			task1Evaluations.add(new ActivityPresenceEvaluation(payOutMortgage));
			task1Evaluations.add(new OrEvalation("Contact customer why he did not accept the offer", new ActivityPresenceEvaluation(
					contactCustomerForReasonOfRejectingOffer), new ActivityPresenceEvaluation(contactCustomerAndCloseApplication)));
			task1Evaluations.add(new ActivityPresenceEvaluation(semanticallyIncorrectTask1));
			task1Evaluations.add(new ActivityPresenceEvaluation(superfluousTask1));

			// other conditions
			task1Evaluations.add(new InitialActivityEvaluation(makeInitialRequest));
			task1Evaluations.add(new DirectSuccessionEvaluation(makeInitialRequest, checkIfCustomerInformationIsAvailable));
			task1Evaluations.add(new LoopEvaluation(checkIfCustomerInformationIsAvailable, contactCustomerForAdditionalInformation));

			ParallelEvaluation calculationsInParallel = new ParallelEvaluation(calculateAvailableFunds, calculateAnnualIncome,
					calculateRequiredFunds);
			ParallelEvaluation alternativeParallelCalculation = new ParallelEvaluation(calculateAvailableFundsAndIncome,
					calculateRequiredFunds);
			ParallelEvaluation alternativeParallelCalculation2 = new ParallelEvaluation(calculateAnnualIncome,
					calculateRequiredAndAvailableFunds);
			task1Evaluations.add(new OrEvalation("Calculations are performed in parallel", calculationsInParallel,
					alternativeParallelCalculation, alternativeParallelCalculation2));

			task1Evaluations.add(new ExclusiveEvaluation(bankAcceptsCondition, bankRefusesCondition));
			task1Evaluations.add(new OrEvalation("If the bank rejects the mortgage, a rejection letter is sent", new ConditionEvaluation(
					bankRefusesCondition, sendRejectionLetter), new ConditionEvaluation(bankRefusesCondition,
					sendRejectionLetterAndCloseApplication)));
			task1Evaluations.add(new OrEvalation("If the bank accepts the mortgage, it makes an offer", new ConditionEvaluation(
					bankAcceptsCondition, makeOfferToCustomer), new ConditionEvaluation(bankAcceptsCondition, makeOfferAndSendDocuments)));
			task1Evaluations.add(new OrEvalation("Make offer and send to customer", new DirectSuccessionEvaluation(makeOfferToCustomer,
					sendDocuments), new ActivityPresenceEvaluation(makeOfferAndSendDocuments)));
			task1Evaluations.add(new ExclusiveEvaluation(customerRejectsCondition, customerAcceptsCondition));
			task1Evaluations.add(new ConditionEvaluation(customerAcceptsCondition, payOutMortgage));
			task1Evaluations.add(new OrEvalation("If customer rejects offer, ask for reasons", new ConditionEvaluation(
					customerRejectsCondition, contactCustomerForReasonOfRejectingOffer), new ConditionEvaluation(customerRejectsCondition,
					contactCustomerAndCloseApplication)));
			task1Evaluations.add(new OrEvalation("Contact customer why he rejected the offer and close application",
					new DirectSuccessionEvaluation(contactCustomerForReasonOfRejectingOffer, closeApplication),
					new ActivityPresenceEvaluation(contactCustomerAndCloseApplication)));
			task1Evaluations.add(new OrEvalation("Close application after rejection letter is sent", new DirectSuccessionEvaluation(
					sendRejectionLetter, closeApplication), new ActivityPresenceEvaluation(sendRejectionLetterAndCloseApplication)));
		}

		return task1Evaluations;
	}

	private List<ISemanticalCorrectnessEvaluation> createTask2Evaluations() {
		if (task2Evaluations == null) {
			task2Evaluations = new ArrayList<ISemanticalCorrectnessEvaluation>();

			task2Evaluations.add(new ActivityPresenceEvaluation(checkIfCustomerHasMortgage));
			task2Evaluations.add(new ActivityPresenceEvaluation(registerApplicationLocally));
			task2Evaluations.add(new ActivityPresenceEvaluation(informHeadQuarters));
			task2Evaluations.add(new ActivityPresenceEvaluation(rejectAndCloseApplication));
			task2Evaluations.add(new ActivityPresenceEvaluation(checkIfMortgageSmaller80Percent));
			task2Evaluations.add(new ActivityPresenceEvaluation(checkIfApplicantIsEmployed));
			task2Evaluations.add(new ActivityPresenceEvaluation(checkIfBlackListed));
			task2Evaluations.add(new ActivityPresenceEvaluation(evaluateResults));
			task2Evaluations.add(new OrEvalation("Register general information",
					new ActivityPresenceEvaluation(registerGeneralInformation), new ActivityPresenceEvaluation(
							registerGeneralInformationAndAnalyzeInDetail)));
			task2Evaluations.add(new OrEvalation("Analyze mortgage in detail", new ActivityPresenceEvaluation(analyzeMortgageInDetail),
					new ActivityPresenceEvaluation(registerGeneralInformationAndAnalyzeInDetail)));
			task2Evaluations.add(new ActivityPresenceEvaluation(confirmMortgage));
			task2Evaluations.add(new OrEvalation("Approve mortgage by supervisor",
					new ActivityPresenceEvaluation(requestSupervisorApproval), new ActivityPresenceEvaluation(
							confirmAndRequestSupervisorApproval)));
			task2Evaluations.add(new ActivityPresenceEvaluation(semanticallyIncorrectTask2));
			task2Evaluations.add(new ActivityPresenceEvaluation(superfluousTask2));

			EdgeCondition noPreviousMortgage = EdgeConditionProvider.getEdgeCondition(NO_PREVIOUS_MORTGAGE);
			EdgeCondition onePreviousMortgage = EdgeConditionProvider.getEdgeCondition(ONE_PREVIOUS_MORTGAGE);
			EdgeCondition moreThanOneMortgage = EdgeConditionProvider.getEdgeCondition(MORE_THAN_ONE_PREVIOUS_MORTGAGE);
			EdgeCondition oneCheckNegative = EdgeConditionProvider.getEdgeCondition(ONE_CHECK_NEGATIVE);
			EdgeCondition allChecksPositive = EdgeConditionProvider.getEdgeCondition(ALL_CHECKS_POSITIVE);
			EdgeConditionProvider.getEdgeCondition(MORTGAGE_LESS_THAN_ONE_MIO);
			EdgeCondition mortgageEqualOrGreatherThanOneMio = EdgeConditionProvider.getEdgeCondition(MORTGAGE_GREATER_OR_EQUAL_ONE_MIO);

			task2Evaluations.add(new InitialActivityEvaluation(checkIfCustomerHasMortgage));
			task2Evaluations.add(new ConditionEvaluation(noPreviousMortgage, registerApplicationLocally));
			task2Evaluations.add(new ConditionEvaluation(onePreviousMortgage, informHeadQuarters));
			task2Evaluations.add(new ConditionEvaluation(moreThanOneMortgage, rejectAndCloseApplication));
			task2Evaluations.add(new ExclusiveEvaluation(null, null, registerApplicationLocally, informHeadQuarters));
			task2Evaluations.add(new ParallelEvaluation(checkIfMortgageSmaller80Percent, checkIfApplicantIsEmployed, checkIfBlackListed));
			task2Evaluations.add(new SynchronizeEvaluation(evaluateResults, checkIfMortgageSmaller80Percent, checkIfApplicantIsEmployed,
					checkIfBlackListed));
			task2Evaluations.add(new ConditionEvaluation(oneCheckNegative, rejectAndCloseApplication));
			task2Evaluations.add(new OrEvalation("If all checks positive, register general information", new ConditionEvaluation(
					allChecksPositive, registerGeneralInformation), new ConditionEvaluation(allChecksPositive,
					registerGeneralInformationAndAnalyzeInDetail)));
			task2Evaluations.add(new OrEvalation("If mortgage equal or greater one mio, then supervisor approval is required",
					new ConditionEvaluation(mortgageEqualOrGreatherThanOneMio, requestSupervisorApproval), new ConditionEvaluation(
							mortgageEqualOrGreatherThanOneMio, confirmAndRequestSupervisorApproval)));

		}

		return task2Evaluations;
	}

	private void createVerificationParagraphs() throws Exception {
		checkIfCustomerHasMortgage = createParagraph(2, "Check if the customer already has a mortgage", new RGB(255, 255, 0), paragraphs);
		registerApplicationLocally = createParagraph(2, "Register application locally", new RGB(255, 192, 0), paragraphs);
		informHeadQuarters = createParagraph(2, "Inform head quarters", new RGB(255, 80, 80), paragraphs);
		rejectAndCloseApplication = createParagraph(2, "Reject and close application", new RGB(153, 0, 51), paragraphs);
		checkIfMortgageSmaller80Percent = createParagraph(2, "Check if mortgage less than 80%", new RGB(54, 96, 146), paragraphs);
		checkIfApplicantIsEmployed = createParagraph(2, "Check if applicant is employed", new RGB(0, 153, 0), paragraphs);
		checkIfBlackListed = createParagraph(2, "Check if applicant is not internally blacklisted", new RGB(102, 51, 0), paragraphs);
		evaluateResults = createParagraph(2, "Evaluate results", new RGB(128, 128, 128), paragraphs);
		analyzeMortgageInDetail = createParagraph(2, "Analyze mortgage in detail", new RGB(146, 208, 80), paragraphs);
		confirmMortgage = createParagraph(2, "Confirm mortgage", new RGB(83, 141, 213), paragraphs);
		requestSupervisorApproval = createParagraph(2, "Request supervisor approval", new RGB(96, 73, 122), paragraphs);
		confirmAndRequestSupervisorApproval = createParagraph(2, "Confirm mortgage *and* request supervisor approval",
				new RGB(96, 73, 122), paragraphs);
		registerGeneralInformation = createParagraph(2, "Register general information", new RGB(255, 102, 255), paragraphs);
		registerGeneralInformationAndAnalyzeInDetail = createParagraph(2, "Register general information *and* analyze in detail", new RGB(
				255, 102, 255), paragraphs);

		semanticallyIncorrectTask2 = createParagraph(2, ActivityPresenceEvaluation.SEMANTICALLY_INCORRECT_ACTIVITY, new RGB(255, 0, 0),
				paragraphs);
		superfluousTask2 = createParagraph(2, ActivityPresenceEvaluation.SUPERFLUOUS_ACTIVITY, new RGB(75, 75, 75), paragraphs);
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		paragraphs.addAll(createParagraphs());

		return paragraphs;
	}

	@Override
	public List<Process> getProcesses() {
		ArrayList<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(MODELING_TASK_1_WITH_LAYOUT);
		processes.add(MODELING_TASK_2_WITH_LAYOUT);
		processes.add(MODELING_TASK_1_WITHOUT_LAYOUT);
		processes.add(MODELING_TASK_2_WITHOUT_LAYOUT);
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

		if (modelingProcess.equals(MODELING_TASK_1_WITH_LAYOUT.getId()) || modelingProcess.equals(MODELING_TASK_1_WITHOUT_LAYOUT.getId())) {
			return createTask1Evaluations();
		}
		if (modelingProcess.equals(MODELING_TASK_2_WITH_LAYOUT.getId()) || modelingProcess.equals(MODELING_TASK_2_WITHOUT_LAYOUT.getId())) {
			return createTask2Evaluations();
		}

		return Collections.emptyList();
	}
}
