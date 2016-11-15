package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.engine.configurations.ExperimentOctober2009.createModelingActivityWithInitialModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.CheckBoxAttributeValidator;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.UnderstandabilityCheckActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.survey.core.CheckBoxInputAttribute;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

public class ExperimentJanuary2010 extends AbstractExperimentalConfiguration {

	private static final String TRANSPORT_OF_EQUIPMENT_CHANGE_SEQUENTIAL_1_0_ID = "transport_of_equipment_change_sequential_1.0";
	private static final String TRANSPORT_OF_EQUIPMENT_CHANGE_CIRCUMSTANTIAL_1_0_ID = "transport_of_equipment_change_circumstantial_1.0";

	public static final Process EXPERIMENTAL_PROCESS_2010_01 = new Process("bp_notation_2.0");

	private static final String TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL = "2010_01_transport_of_equipment.mxml";
	private static final Process TRANSPORT_OF_EQUIPMENT_PROCESS_SEQUENTIAL = new Process(TRANSPORT_OF_EQUIPMENT_CHANGE_SEQUENTIAL_1_0_ID);
	private static final Process TRANSPORT_OF_EQUIPMENT_PROCESS_CIRCUMSTANTIAL = new Process(
			TRANSPORT_OF_EQUIPMENT_CHANGE_CIRCUMSTANTIAL_1_0_ID);

	private List<WorkflowConfiguration> configurations;

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

	private List<Paragraph> createCircumstantialModel() throws Exception {
		String processId = TRANSPORT_OF_EQUIPMENT_CHANGE_CIRCUMSTANTIAL_1_0_ID;
		List<Paragraph> paragraphs = createInitialModel(processId);

		Paragraph paragraph1 = new Paragraph(processId, "Fly TF Team 1 to destination area", new RGB(36, 220, 0));
		paragraphs.add(paragraph1);

		Paragraph paragraph2 = new Paragraph(processId, "Present at immigration (TF Team 1)", new RGB(30, 182, 0));
		paragraphs.add(paragraph2);

		Paragraph paragraph4 = new Paragraph(processId, "Fly TF Team 2 to destination area", new RGB(152, 252, 0));
		paragraphs.add(paragraph4);

		Paragraph paragraph3 = new Paragraph(processId, "Present at immigration (TF Team 2)", new RGB(152, 252, 132));
		paragraphs.add(paragraph3);

		Paragraph paragraph5 = new Paragraph(processId, "Contact other task force team members", new RGB(132, 252, 234));
		paragraphs.add(paragraph5);

		Paragraph paragraph6 = new Paragraph(processId, "Contact local geologists", new RGB(0, 214, 181));
		paragraphs.add(paragraph6);

		Paragraph paragraph7 = new Paragraph(processId, "Fly equipment to destination area", new RGB(225, 136, 255));
		paragraphs.add(paragraph7);

		return paragraphs;
	}

	public SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the first change task?", true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);

		ComboInputAttribute mentalEffortComboChangeTask = new ComboInputAttribute(
				"How would you assess the mental effort for completing the second change task?", true);
		addMentalEffortChoices(mentalEffortComboChangeTask);
		mentalEffortActivitiesModelingTask.add(mentalEffortComboChangeTask);

		return new SurveyActivity(mentalEffortActivitiesModelingTask);
	}

	private void createConfiguration(int id, Process assignment1, Process assignment2) {
		WorkflowConfiguration configuration = new WorkflowConfiguration(id);
		configuration.add(ExperimentOctober2009.createDemographicSurveyActivity());
		configuration.add(createUnderstandabilitySurvey());
		configuration.add(new TutorialActivity());
		configuration.add(createModelingActivity(assignment1));
		configuration.add(new ShowMessageActivity("Second Change Task Required", "Please wait until you received the second change task."));
		configuration.add(createModelingActivity(assignment2));

		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating the modeling session :-)"));

		configurations.add(configuration);
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		if (configurations != null) {
			return configurations;
		}

		configurations = new ArrayList<WorkflowConfiguration>();
		createConfiguration(3249, TRANSPORT_OF_EQUIPMENT_PROCESS_SEQUENTIAL, TRANSPORT_OF_EQUIPMENT_PROCESS_CIRCUMSTANTIAL);
		createConfiguration(6984, TRANSPORT_OF_EQUIPMENT_PROCESS_CIRCUMSTANTIAL, TRANSPORT_OF_EQUIPMENT_PROCESS_SEQUENTIAL);

		return configurations;
	}

	private List<Paragraph> createInitialModel(String processId) throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();

		RGB initialModelColor = new RGB(200, 200, 200);
		Paragraph paragraph1 = new Paragraph(processId,
				"The transport of the equipment to the disaster area is organized by the task force.", initialModelColor, 1);
		paragraphs.add(paragraph1);
		paragraph1.addPossibleActivityName("Organize transport of equipment");

		Paragraph paragraph2 = new Paragraph(processId, "The whole equipment is presented to German customs.", initialModelColor, 2);
		paragraphs.add(paragraph2);
		paragraph2.addPossibleActivityName("Present equipment at customs in Germany");

		Paragraph paragraph3 = new Paragraph(processId,
				"Customs might require the demonstration of equipment devices. In this case, the demonstration happens right away.",
				initialModelColor, 4);
		paragraphs.add(paragraph3);

		Paragraph paragraph30 = new Paragraph(
				processId,
				"Customs might require the demonstration of equipment devices at the host country. In this case, the demonstration happens right away.",
				initialModelColor, 16);
		paragraphs.add(paragraph30);

		Paragraph paragraph4 = new Paragraph(processId, "The task force flies to the disaster area in the host country.",
				initialModelColor, 6);
		paragraphs.add(paragraph4);
		paragraph4.addPossibleActivityName("Fly to destination");

		Paragraph paragraph5 = new Paragraph(processId,
				"The members of the task force present themselves at the immigration office in the host country.", initialModelColor, 7);
		paragraphs.add(paragraph5);
		paragraph5.addPossibleActivityName("Present at immigration");

		Paragraph paragraph6 = new Paragraph(processId, "The whole equipment is presented to customs of the host country.",
				initialModelColor, 10);
		paragraphs.add(paragraph6);
		paragraph6.addPossibleActivityName("Present equipment at customs in host country");

		Paragraph paragraph7 = new Paragraph(processId, "The equipment is transported from customs to a storage location.",
				initialModelColor, 21);
		paragraphs.add(paragraph7);
		paragraph7.addPossibleActivityName("Transport equipment to storage location");
		Paragraph paragraph8 = new Paragraph(processId,
				"The task force members organize an accommodation in the host country, preferably with electricity.", initialModelColor, 11);
		paragraphs.add(paragraph8);
		paragraph8.addPossibleActivityName("Organize accommodation with electricity");
		Paragraph paragraph9 = new Paragraph(processId, "The task force members get road maps for the disaster area in the host country.",
				initialModelColor, 12);
		paragraphs.add(paragraph9);
		paragraph9.addPossibleActivityName("Get road maps");

		Paragraph paragraph10 = new Paragraph(processId, "The task force members rent vehicles in the host country.", initialModelColor, 9);
		paragraphs.add(paragraph10);
		paragraph10.addPossibleActivityName("Rent vehicles to transport equipment");
		Paragraph paragraph11 = new Paragraph(
				processId,
				"It might be the case that local car rental companies cannot provide sufficient transport capabilities. In this case, the task force members seek for vehicles of partner organisations.",
				initialModelColor, 15);
		paragraphs.add(paragraph11);
		paragraph11.addPossibleActivityName("Seek for vehicles of partner organisations");

		return paragraphs;
	}

	private BPMNModelingActivity createModelingActivity(Process assignment1) {
		BPMNModelingActivity activity = createModelingActivityWithInitialModel(TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL, assignment1);
		activity.setInitialSize(new Point(140, 60));
		return activity;
	}

	private List<Paragraph> createSequentialModel() throws Exception {
		String processId = TRANSPORT_OF_EQUIPMENT_CHANGE_SEQUENTIAL_1_0_ID;
		List<Paragraph> paragraphs = createInitialModel(processId);

		Paragraph paragraph1 = new Paragraph(processId, "Negotiate with customs", new RGB(36, 220, 0));
		paragraphs.add(paragraph1);

		Paragraph paragraph2 = new Paragraph(processId, "Trigger support from higher-ranked authorities", new RGB(30, 182, 0));
		paragraphs.add(paragraph2);

		Paragraph paragraph3 = new Paragraph(processId, "Transport partial equipment to storage location", new RGB(152, 252, 132));
		paragraphs.add(paragraph3);

		Paragraph paragraph4 = new Paragraph(processId,
				"Transport partial equipment to storage location after missing documents have been retrieved", new RGB(152, 252, 0));
		paragraphs.add(paragraph4);

		Paragraph paragraph5 = new Paragraph(processId, "Retrieve missing documents from home", new RGB(132, 252, 234));
		paragraphs.add(paragraph5);

		return paragraphs;
	}

	private IExperimentalWorkflowActivity createUnderstandabilitySurvey() {
		String miscWrong1 = "The process contains more than one AND split.";
		String miscCorrect1 = "More than one activity is labeled \"Demonstrate devices\"";
		String miscCorrect2 = "There is only one activity labeled \"Present at immigration\"";

		String sequentialCorrect1 = "\"Fly to destination area\" is immediately followed by \"Present at immigration\"";
		String sequentialWrong1 = "\"Demonstrate devices\" can be executed before \"Organize transport of cargo\" has been executed";
		String sequentialWrong2 = "\"Get road maps\" can be executed before \"Fly to destination area\"";

		String circumstantialWrong1 = "Either \"Get road maps\" or \"Organize accommodation with eletricity\" can be executed, but not both.";
		String circumstantialCorrect1 = "\"Demonstrate devices\" is optional.";
		String circumstantialCorrect2 = "\"Get road maps\" and \"Present equipment at customs in host country\" can be executed in parallel.";

		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		List<String> correctAnswers = new ArrayList<String>();
		correctAnswers.add(miscCorrect1);
		correctAnswers.add(miscCorrect2);
		correctAnswers.add(sequentialCorrect1);
		correctAnswers.add(circumstantialCorrect1);
		correctAnswers.add(circumstantialCorrect2);
		List<String> wrongAnswers = new ArrayList<String>();
		wrongAnswers.add(miscWrong1);
		wrongAnswers.add(sequentialWrong1);
		wrongAnswers.add(sequentialWrong2);
		wrongAnswers.add(circumstantialWrong1);

		CheckBoxInputAttribute understandability = new CheckBoxInputAttribute(
				"Please study the process model and text document carefully.\nThen read the statements below and check correct ones.",
				new CheckBoxAttributeValidator(correctAnswers, wrongAnswers, 1));
		attributes.add(understandability);
		understandability.addChoice(miscWrong1);
		understandability.addChoice(miscCorrect1);
		understandability.addChoice(miscCorrect2);

		understandability.addChoice(sequentialCorrect1);
		understandability.addChoice(sequentialWrong1);
		understandability.addChoice(sequentialWrong2);

		understandability.addChoice(circumstantialWrong1);
		understandability.addChoice(circumstantialCorrect1);
		understandability.addChoice(circumstantialCorrect2);

		return new UnderstandabilityCheckActivity(attributes);
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS_2010_01;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		Map<Process, String> mapping = new HashMap<Process, String>();
		mapping.put(TRANSPORT_OF_EQUIPMENT_PROCESS_CIRCUMSTANTIAL, TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL);
		mapping.put(TRANSPORT_OF_EQUIPMENT_PROCESS_SEQUENTIAL, TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL);

		return mapping;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		paragraphs.addAll(createSequentialModel());
		paragraphs.addAll(createCircumstantialModel());

		return paragraphs;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(TRANSPORT_OF_EQUIPMENT_PROCESS_SEQUENTIAL);
		processes.add(TRANSPORT_OF_EQUIPMENT_PROCESS_CIRCUMSTANTIAL);

		return processes;
	}

}
