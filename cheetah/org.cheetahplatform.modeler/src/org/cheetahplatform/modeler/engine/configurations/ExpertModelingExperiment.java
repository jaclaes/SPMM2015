package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.graphics.RGB;

public class ExpertModelingExperiment extends AbstractExperimentalConfiguration {
	private static final Process MODELING_TASK_1 = new Process("expert_modeling_task1_1.0");
	private static final Process EXPERIMENTAL_PROCESS = new Process("expert_modeling_1.0");

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

		return new SurveyActivity(mentalEffortActivitiesModelingTask);
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		WorkflowConfiguration configuration = new WorkflowConfiguration(9597);
		configuration.add(createDemographicSurveyActivity());
		configuration.add(NeedForCognitionQuestionnaire.createActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1));
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

		return new SurveyActivity(attributes);
	}

	private Paragraph createParagrah(Process process, String name, RGB color, List<Paragraph> paragraphs) throws Exception {
		Paragraph paragraph = new Paragraph(process.getId(), name, color);
		paragraph.addPossibleActivityName(name);
		paragraphs.add(paragraph);
		return paragraph;
	}

	private Collection<? extends Paragraph> createPreFlightParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		createParagrah(MODELING_TASK_1, "Check Weather", new RGB(254, 62, 62), paragraphs);
		createParagrah(MODELING_TASK_1, "File Flight Plan", new RGB(254, 156, 156), paragraphs);
		createParagrah(MODELING_TASK_1, "Check Engine", new RGB(255, 69, 235), paragraphs);
		createParagrah(MODELING_TASK_1, "Check Fuselage", new RGB(130, 69, 255), paragraphs);
		createParagrah(MODELING_TASK_1, "Call Clearance Delivery", new RGB(187, 154, 255), paragraphs);
		createParagrah(MODELING_TASK_1, "Get Taxi Clearance", new RGB(49, 181, 249), paragraphs);
		createParagrah(MODELING_TASK_1, "Announce Taxiing", new RGB(139, 214, 252), paragraphs);
		createParagrah(MODELING_TASK_1, "Taxiing", new RGB(37, 254, 129), paragraphs);
		createParagrah(MODELING_TASK_1, "Perform Run-Up Checks", new RGB(151, 254, 95), paragraphs);
		createParagrah(MODELING_TASK_1, "Get Take-Off Clearance", new RGB(236, 254, 39), paragraphs);
		createParagrah(MODELING_TASK_1, "Announce Take Off", new RGB(254, 165, 61), paragraphs);
		createParagrah(MODELING_TASK_1, "Take Off", new RGB(255, 200, 136), paragraphs);
		return paragraphs;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		List<Paragraph> list = new ArrayList<Paragraph>(0);
		list.addAll(createPreFlightParagraphs());
		return list;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(MODELING_TASK_1);
		return processes;
	}
}
