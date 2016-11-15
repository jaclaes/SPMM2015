package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TorrenceTestCreativeThinkingWorkflowActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.RadioButtonInputAttribute;
import org.cheetahplatform.survey.core.SectionHeaderAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class CreativityExperiment extends AbstractExperimentalConfiguration {
	private static final String INITAL_MODEL_AIRPORT = "creativity/creativity_inital_model_airport.mxml";
	private static final String INITAL_MODEL_COFFEE = "creativity/creativity_inital_model_coffee.mxml";
	private static final Process EXPERIMENTAL_PROCESS = new Process("creativity_1.0");
	private static final Process MODELING_PROCESS_1 = new Process("creativity_modeling_task1_1.0");
	private static final Process MODELING_PROCESS_2 = new Process("creativity_modeling_task2_1.0");

	private static void addChoices(ComboInputAttribute combo) {
		combo.addChoice("strongly agree");
		combo.addChoice("agree");
		combo.addChoice("somewhat agree");
		combo.addChoice("neutral");
		combo.addChoice("somewhat disagree");
		combo.addChoice("disagree");
		combo.addChoice("strongly disagree");
	}

	protected void addBPMNChoices(List<SurveyAttribute> attributes, ComboInputAttribute question1) {
		question1.addChoice("True");
		question1.addChoice("False");
		question1.addChoice("I don't know");
		attributes.add(question1);
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private IExperimentalWorkflowActivity createBPMNSurvey() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();

		attributes
				.add(new SectionHeaderAttribute(
						"BPMN_INTRO",
						"The following items test your knowledge on business process modeling. Which statements are correct/right?/ Die folgenden Fragen dienen dazu, um Ihren aktuellen Wissenstand im Bereich der Prozessmodellierung abzuschätzen. Welche der folgenden Aussagen zur Geschäftsprozessmodellierung sind Ihrer Kenntnis nach richtig?"));

		attributes.add(new MessageSurveyAttribute("SPACE_1", ""));

		ComboInputAttribute question1 = new ComboInputAttribute(
				"A business process consists of different tasks which have to be executed according to a pre-defined order to reach a specific goal./ Ein Geschäftsprozess besteht aus einer Menge miteinander verknüpfter Prozess- bzw. Arbeitsschritten, welche in einer bestimmten Reihenfolge ausgeführt werden, um ein festgelegtes Ziel zu erreichen.",
				true);
		addBPMNChoices(attributes, question1);
		attributes.add(new MessageSurveyAttribute("SPACE_2", ""));

		ComboInputAttribute question2 = new ComboInputAttribute(
				"The tasks in a business process have to be executed in a linear sequence. This allows for the automated processing of data afterwards./ Die verschiedenen Prozessschritte eines Geschäftsprozesses müssen streng sequenziell ausgeführt werden. Dies ermöglicht später die automatisierte Verarbeitung der Daten.",
				true);
		addBPMNChoices(attributes, question2);
		attributes.add(new MessageSurveyAttribute("SPACE_3", ""));

		ComboInputAttribute question3 = new ComboInputAttribute(
				"Business processes are well structured processes and are based on the division of labor as for instance procurement and customer support of a company./ Geschäftsprozesse sind gut strukturierbare, arbeitsteilige Prozesse, wie zum Beispiel der Wareneinkauf durch ein Handelsunternehmen oder der Kundenservice eines Produktionsunternehmens.",
				true);
		addBPMNChoices(attributes, question3);

		ComboInputAttribute question4 = new ComboInputAttribute(
				"For exclusive choices (XOR), exactly one of the alternative branches is activated./ Bei exklusiven Entscheidungen (XOR) wird pro Durchlauf genau einer der alternativen Pfade ausgeführt.",
				true);
		addBPMNChoices(attributes, question4);
		attributes.add(new MessageSurveyAttribute("SPACE_5", ""));

		ComboInputAttribute question5 = new ComboInputAttribute(
				"If two activities are modeled as being concurrent, then they must be executed at the same time./ Wenn zwei Prozessschritte in nebenläufigen (bzw. parallelen) Pfaden modelliert sind, müssen sie zum selben Zeitpunkt beginnen.",
				true);
		addBPMNChoices(attributes, question5);
		attributes.add(new MessageSurveyAttribute("SPACE_6", ""));

		ComboInputAttribute question6 = new ComboInputAttribute(
				"Exclusive choices can be used to model repetition./ Mit exklusiven Entscheidungen (XOR) können Wiederholungen modelliert werden.",
				true);
		addBPMNChoices(attributes, question6);
		attributes.add(new MessageSurveyAttribute("SPACE_7", ""));

		ComboInputAttribute question7 = new ComboInputAttribute(
				"In many process modeling languages, synchronization is modeled by an AND-join./ In vielen Prozessmodellierungssprachen wird eine Synchronisation durch eine UND-Zusammenführung (Join) modelliert.",
				true);
		addBPMNChoices(attributes, question7);

		ComboInputAttribute question8 = new ComboInputAttribute(
				"If an activity is modeled to be part of a loop, then it is always executed at least once./ Wenn ein Prozessschritt in einer Schleife modelliert wird, dann muss er mindestens einmal ausgeführt werden.",
				true);
		addBPMNChoices(attributes, question8);

		return new SurveyActivity(attributes, 700);
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		WorkflowConfiguration configuration = new WorkflowConfiguration(5437);
		configuration.add(new ParticipantInformationWorkflowActivity(30));
		configuration.add(createDemographicSurvey());
		configuration.add(createBPMNSurvey());
		configuration
				.add(new ShowMessageActivity(
						"BPMN Introduction",
						"Before proceeding with the experiment have a look at the BPMN introduction which was handed out to you with the task descriptions to familiarize yourself with the BPMN notation."));
		configuration.add(new TutorialActivity());

		URL coffeeInitalModel = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + INITAL_MODEL_COFFEE),
				new HashMap<Object, Object>());
		URL airportInitalModel = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + INITAL_MODEL_AIRPORT),
				new HashMap<Object, Object>());

		try {
			configuration
					.add(new ShowMessageActivity(
							"Airline Check-in",
							"The airline implements a new check in and guidance service that provides customers with valuable information via mobile phone. As soon as the customer checks in, the system generates relevant hints and guides the costumer based on relevant context information (location of customer, customer profile, weather). How can this service in cooperation with a specific airport best support costumers as well as business interests?\n\nPlease click 'finish modelling' on the top left if you are done."));
			configuration.add(new BPMNModelingActivity(airportInitalModel.openStream(), new Graph(EditorRegistry.getDescriptors(BPMN)),
					MODELING_PROCESS_1));
		} catch (Exception e) {
			Activator.logError("Could not create configuration for change task", e);
		}
		try {
			configuration
					.add(new ShowMessageActivity(
							"Coffee Shop",
							"The coffee shop service wants to improve customer satisfaction by enhancing customer experience. How can the process be changed to implement that improvement?\n\nPlease click 'finish modelling' on the top left if you are done."));
			configuration.add(new BPMNModelingActivity(coffeeInitalModel.openStream(), new Graph(EditorRegistry.getDescriptors(BPMN)),
					MODELING_PROCESS_2));
		} catch (Exception e) {
			Activator.logError("Could not create configuration for change task", e);
		}
		configuration.add(IntrinsicMotivationQuestionnaire.createActivity());
		configuration.add(KirtonAdaptionInnovationInventoryQuestionnaire.createActivity());
		configuration.add(new TorrenceTestCreativeThinkingWorkflowActivity());
		configuration.add(new FeedbackActivity());
		configurations.add(configuration);
		return configurations;
	}

	private SurveyActivity createDemographicSurvey() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.add(new IntegerInputAttribute("What is your age?", true, 10, 100));

		ComboInputAttribute gender = new ComboInputAttribute("What is your gender?", true);
		gender.addChoice("Female");
		gender.addChoice("Male");
		attributes.add(gender);

		ComboInputAttribute education = new ComboInputAttribute("What is the highest degree or level of school you have completed?", true);
		education.addChoice("High school graduate");
		education.addChoice("Bachelor's degree");
		education.addChoice("Master's degree");
		education.addChoice("Doctorate degree");
		attributes.add(education);

		attributes.add(new StringInputAttribute("\tWhat is your major subject?", false));

		ComboInputAttribute currentlyStudying = new ComboInputAttribute("Are you currently studying?", true);
		currentlyStudying.addChoice("No");
		currentlyStudying.addChoice("Yes, Bachelor");
		currentlyStudying.addChoice("Yes, Master");
		currentlyStudying.addChoice("Yes, Doctorate");
		attributes.add(currentlyStudying);
		attributes.add(new StringInputAttribute("\tIf yes, what is your major subject?", false));

		RadioButtonInputAttribute processModeling = new RadioButtonInputAttribute(
				"Have you ever done any process modelling (e.g., with EPCs, BPMN, Flowcharts, Petri Nets)?", true);
		processModeling.addChoice("Yes");
		processModeling.addChoice("No");
		attributes.add(processModeling);

		attributes.add(new SectionHeaderAttribute("MESSAGE_1",
				"In case you are familiar with process models, please rate your familiarity."));

		ComboInputAttribute processModelsFamiliarity = new ComboInputAttribute("Overall, I am very familiar with process models.", false);
		addChoices(processModelsFamiliarity);
		attributes.add(processModelsFamiliarity);

		ComboInputAttribute understandingProcessModels = new ComboInputAttribute("I feel very confident in understanding process models.",
				false);
		addChoices(understandingProcessModels);
		attributes.add(understandingProcessModels);

		ComboInputAttribute usingProcessModels = new ComboInputAttribute("I feel very competent in using process models.", false);
		addChoices(usingProcessModels);
		attributes.add(usingProcessModels);

		ComboInputAttribute workWithProcessModels = new ComboInputAttribute("I work with process models.", true);
		workWithProcessModels.addChoice("Never");
		workWithProcessModels.addChoice("Rarely");
		workWithProcessModels.addChoice("Sometimes");
		workWithProcessModels.addChoice("Often");
		workWithProcessModels.addChoice("Always");
		attributes.add(workWithProcessModels);

		IntegerInputAttribute integerInputAttribute = new IntegerInputAttribute(
				"Roughly, how many process models have you created to date? (insert 0 for none)", true, 0, Integer.MAX_VALUE);
		attributes.add(integerInputAttribute);

		attributes.add(new IntegerInputAttribute("Roughly, how many process models have you read to date? (insert 0 for none)", true, 0,
				Integer.MAX_VALUE));

		attributes
				.add(new IntegerInputAttribute(
						"During your working life, how often have you contributed to a process improvement initiative (e.g., to re-design a process, or to develop new software for a process) (insert 0 for never)?",
						true, 0, Integer.MAX_VALUE));

		return new SurveyActivity(attributes);
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		Map<Process, String> map = new HashMap<Process, String>();
		map.put(MODELING_PROCESS_1, INITAL_MODEL_AIRPORT);
		map.put(MODELING_PROCESS_2, INITAL_MODEL_COFFEE);
		return map;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(MODELING_PROCESS_1);
		processes.add(MODELING_PROCESS_2);
		return processes;
	}
}
