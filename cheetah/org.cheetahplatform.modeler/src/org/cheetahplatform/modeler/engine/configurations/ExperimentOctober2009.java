package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.RGB;

public class ExperimentOctober2009 extends AbstractExperimentalConfiguration {
	public static final Process EXPERIMENTAL_PROCESS_2009_10 = new Process("bp_notation_1.0");

	private static final String SEISMIC_ANALYSIS_CHANGE_1_INITIAL_MODEL = "2009_10_seismic_analysis_change_1.mxml";
	private static final String SEISMIC_ANALYSIS_CHANGE_2_INITIAL_MODEL = "2009_10_seismic_analysis_change_2.mxml";
	private static final String SEISMIC_ANALYSIS_CHANGE_3_INITIAL_MODEL = "2009_10_seismic_analysis_change_3.mxml";

	private static final String TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL = "2009_10_transport_of_equipment_change_1.mxml";
	private static final String TRANSPORT_OF_EQUIPMENT_CHANGE_2_INITIAL_MODEL = "2009_10_transport_of_equipment_change_2.mxml";

	private static final String EQUIPMENT_TRANSPORT_1_0 = "equipment_transport_1.0";
	private static final String SEISMIC_ANALYSIS_1_0 = "seismic_analysis_1.0";

	private static Process EQUIPMENT_TRANSPORT_PROCESS = new Process(EQUIPMENT_TRANSPORT_1_0);
	private static Process EQUIPMENT_TRANSPORT_CHANGE1 = new Process("equipment_transport_change1_1.0");
	private static Process EQUIPMENT_TRANSPORT_CHANGE2 = new Process("equipment_transport_change2_1.0");
	private static Process SEISMIC_ANALYSIS_PROCESS = new Process(SEISMIC_ANALYSIS_1_0);
	private static Process SEISMIC_ANALYSIS_CHANGE1 = new Process("seismic_analysis_change1_1.0");
	private static Process SEISMIC_ANALYSIS_CHANGE2 = new Process("seismic_analysis_change2_1.0");
	private static Process SEISMIC_ANALYSIS_CHANGE3 = new Process("seismic_analysis_change3_1.0");

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

		ComboInputAttribute comboChoice = new ComboInputAttribute("Which description matches best your current work status?", true);
		comboChoice.addChoice("Student");
		comboChoice.addChoice("Academic");
		comboChoice.addChoice("Professional");
		attributes.add(comboChoice);

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

		ComboInputAttribute dis = new ComboInputAttribute("I am very familiar with Disaster Management Processes.", true);
		addChoices(dis);
		attributes.add(dis);

		return new SurveyActivity(attributes);
	}

	public static BPMNModelingActivity createModelingActivityWithInitialModel(String intialModel, Process process) {
		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		String path = "resource/" + intialModel;
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path(path), new HashMap<Object, Object>());

		try {
			return new BPMNModelingActivity(input.openStream(), graph, process);
		} catch (Exception e) {
			Activator.logError("Could not create configuration: " + path, e);
			throw new RuntimeException(e);
		}
	}

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

	private SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the modeling task (first task)?", true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);

		ComboInputAttribute mentalEffortComboChangeTask = new ComboInputAttribute(
				"How would you assess the mental effort for completing the change task (second task)?", true);
		addMentalEffortChoices(mentalEffortComboChangeTask);
		mentalEffortActivitiesModelingTask.add(mentalEffortComboChangeTask);

		return new SurveyActivity(mentalEffortActivitiesModelingTask);
	}

	private void createConfiguration(int id, String initialModel, Process processId1, Process processId2) {
		WorkflowConfiguration configuration = new WorkflowConfiguration(id);
		configuration.add(createDemographicSurveyActivity());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), processId1));

		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + initialModel),
				new HashMap<Object, Object>());
		try {
			configuration.add(new BPMNModelingActivity(input.openStream(), graph, processId2));
		} catch (Exception e) {
			Activator.logError("Could not create configuration: " + initialModel, e);
		}

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
		// Process 1
		createConfiguration(8351, TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL, EQUIPMENT_TRANSPORT_PROCESS, EQUIPMENT_TRANSPORT_CHANGE1);
		createConfiguration(8236, TRANSPORT_OF_EQUIPMENT_CHANGE_2_INITIAL_MODEL, EQUIPMENT_TRANSPORT_PROCESS, EQUIPMENT_TRANSPORT_CHANGE2);

		createConfiguration(9844, TRANSPORT_OF_EQUIPMENT_CHANGE_2_INITIAL_MODEL, EQUIPMENT_TRANSPORT_PROCESS, EQUIPMENT_TRANSPORT_CHANGE2);
		createConfiguration(3437, TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL, EQUIPMENT_TRANSPORT_PROCESS, EQUIPMENT_TRANSPORT_CHANGE1);

		createConfiguration(5684, TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL, EQUIPMENT_TRANSPORT_PROCESS, EQUIPMENT_TRANSPORT_CHANGE1);
		createConfiguration(8813, TRANSPORT_OF_EQUIPMENT_CHANGE_2_INITIAL_MODEL, EQUIPMENT_TRANSPORT_PROCESS, EQUIPMENT_TRANSPORT_CHANGE2);

		// Process 2
		createConfiguration(9412, SEISMIC_ANALYSIS_CHANGE_1_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE1);
		createConfiguration(3211, SEISMIC_ANALYSIS_CHANGE_2_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE2);
		createConfiguration(1528, SEISMIC_ANALYSIS_CHANGE_3_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE3);

		createConfiguration(6481, SEISMIC_ANALYSIS_CHANGE_2_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE2);
		createConfiguration(3065, SEISMIC_ANALYSIS_CHANGE_3_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE3);
		createConfiguration(7524, SEISMIC_ANALYSIS_CHANGE_1_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE1);

		createConfiguration(1092, SEISMIC_ANALYSIS_CHANGE_3_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE3);
		createConfiguration(2048, SEISMIC_ANALYSIS_CHANGE_1_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE1);
		createConfiguration(7502, SEISMIC_ANALYSIS_CHANGE_2_INITIAL_MODEL, SEISMIC_ANALYSIS_PROCESS, SEISMIC_ANALYSIS_CHANGE2);

		validateIds();

		return configurations;
	}

	private List<Paragraph> createEquipmentTransportParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();

		Paragraph paragraph1 = new Paragraph(EQUIPMENT_TRANSPORT_1_0,
				"The transport of the equipment to the disaster area is organized by the task force.", new RGB(36, 220, 0));
		paragraphs.add(paragraph1);
		paragraph1.addPossibleActivityName("Organize transport of equipment");

		Paragraph paragraph2 = new Paragraph(EQUIPMENT_TRANSPORT_1_0, "The whole equipment is presented to German customs.", new RGB(30,
				182, 0));
		paragraphs.add(paragraph2);
		paragraph2.addPossibleActivityName("Present equipment at customs in Germany");

		Paragraph paragraph3 = new Paragraph(EQUIPMENT_TRANSPORT_1_0,
				"Customs might require the demonstration of equipment devices. In this case, the demonstration happens right away.",
				new RGB(152, 252, 132));
		paragraphs.add(paragraph3);

		Paragraph paragraph30 = new Paragraph(
				EQUIPMENT_TRANSPORT_1_0,
				"Customs might require the demonstration of equipment devices at the host country. In this case, the demonstration happens right away.",
				new RGB(152, 252, 0));
		paragraphs.add(paragraph30);

		Paragraph paragraph4 = new Paragraph(EQUIPMENT_TRANSPORT_1_0, "The task force flies to the disaster area in the host country.",
				new RGB(132, 252, 234));
		paragraphs.add(paragraph4);
		paragraph4.addPossibleActivityName("Fly to destination");
		Paragraph paragraph5 = new Paragraph(EQUIPMENT_TRANSPORT_1_0,
				"The members of the task force present themselves at the immigration office in the host country.", new RGB(0, 214, 181));
		paragraphs.add(paragraph5);
		paragraph5.addPossibleActivityName("Present at immigration");

		Paragraph paragraph6 = new Paragraph(EQUIPMENT_TRANSPORT_1_0, "The whole equipment is presented to customs of the host country.",
				new RGB(225, 136, 255));
		paragraphs.add(paragraph6);
		paragraph6.addPossibleActivityName("Present equipment at customs in host country");

		Paragraph paragraph7 = new Paragraph(EQUIPMENT_TRANSPORT_1_0, "The equipment is transported from customs to a storage location.",
				new RGB(30, 182, 0));
		paragraphs.add(paragraph7);
		paragraph7.addPossibleActivityName("Transport equipment to storage location");
		Paragraph paragraph8 = new Paragraph(EQUIPMENT_TRANSPORT_1_0,
				"The task force members organize an accommodation in the host country, preferably with electricity.", new RGB(182, 0, 242));
		paragraphs.add(paragraph8);
		paragraph8.addPossibleActivityName("Organize accommodation with electricity");
		Paragraph paragraph9 = new Paragraph(EQUIPMENT_TRANSPORT_1_0,
				"The task force members get road maps for the disaster area in the host country.", new RGB(255, 101, 123));
		paragraphs.add(paragraph9);
		paragraph9.addPossibleActivityName("Get road maps");

		Paragraph paragraph10 = new Paragraph(EQUIPMENT_TRANSPORT_1_0, "The task force members rent vehicles in the host country.",
				new RGB(229, 0, 32));
		paragraphs.add(paragraph10);
		paragraph10.addPossibleActivityName("Rent vehicles to transport equipment");
		Paragraph paragraph11 = new Paragraph(
				EQUIPMENT_TRANSPORT_1_0,
				"It might be the case that local car rental companies cannot provide sufficient transport capabilities. In this case, the task force members seek for vehicles of partner organisations.",
				new RGB(255, 250, 108));
		paragraphs.add(paragraph11);
		paragraph11.addPossibleActivityName("Seek for vehicles of partner organisations");

		return paragraphs;
	}

	private List<Paragraph> createSeismicAnalysisParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();

		Paragraph seismicParagraph1 = new Paragraph(SEISMIC_ANALYSIS_1_0, "The task force members design the network of seismic stations.",
				new RGB(235, 152, 91));
		paragraphs.add(seismicParagraph1);
		seismicParagraph1.addPossibleActivityName("Design seismic station network");

		Paragraph seismicParagraph2 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"For a tour, the task force members choose two or more seismic station locations.", new RGB(240, 217, 71));
		paragraphs.add(seismicParagraph2);

		Paragraph seismicParagraph3 = new Paragraph(SEISMIC_ANALYSIS_1_0, "The task force members drive to a seismic station location.",
				new RGB(254, 252, 60));
		paragraphs.add(seismicParagraph3);

		Paragraph seismicParagraph4 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"The task force members check the accessibility of the location.", new RGB(212, 254, 66));
		paragraphs.add(seismicParagraph4);
		seismicParagraph4.addPossibleActivityName("Check accessibility to location");

		Paragraph seismicParagraph5 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"The task force members check the condition of the ground at the location.", new RGB(150, 254, 66));
		paragraphs.add(seismicParagraph5);
		seismicParagraph5.addPossibleActivityName("Check condition of ground");

		Paragraph seismicParagraph6 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"A location might be inadequate for deploying a seismic station.", new RGB(101, 154, 66));
		paragraphs.add(seismicParagraph6);
		Paragraph seismicParagraph7 = new Paragraph(
				SEISMIC_ANALYSIS_1_0,
				"If work has been finished at one location, the next location of the tour is considered. In case the last location has been visited, a new tour is started.",
				new RGB(66, 254, 145));
		paragraphs.add(seismicParagraph7);

		Paragraph seismicParagraph9 = new Paragraph(SEISMIC_ANALYSIS_1_0, "The location is prepared for the seismic station.", new RGB(66,
				254, 221));
		paragraphs.add(seismicParagraph9);
		seismicParagraph9.addPossibleActivityName("Prepare location for seismic station");

		Paragraph seismicParagraph10 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"It might be the case that a concrete foundation is necessary in order to deploy a seismic station.", new RGB(66, 207, 254));
		paragraphs.add(seismicParagraph10);
		Paragraph seismicParagraph11 = new Paragraph(SEISMIC_ANALYSIS_1_0, "A concrete foundation is built for the seismic station.",
				new RGB(66, 132, 254));
		seismicParagraph11.addPossibleActivityName("Build concrete foundation");
		paragraphs.add(seismicParagraph11);
		Paragraph seismicParagraph12 = new Paragraph(SEISMIC_ANALYSIS_1_0, "The seismic station is deployed.", new RGB(79, 66, 254));
		paragraphs.add(seismicParagraph12);
		seismicParagraph12.addPossibleActivityName("Install seismic station");

		Paragraph seismicParagraph13 = new Paragraph(SEISMIC_ANALYSIS_1_0, "The location is re-scheduled for another tour.", new RGB(154,
				66, 254));
		seismicParagraph13.addPossibleActivityName("Re-schedule location for another tour");

		paragraphs.add(seismicParagraph13);
		Paragraph seismicParagraph14 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"Deployment of seismic stations takes place until the set-up time is over or the whole network of stations is set-up.",
				new RGB(97, 66, 254));
		paragraphs.add(seismicParagraph14);

		Paragraph seismicParagraph15 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"The task force members read the data from the hard drive of the seismic station.", new RGB(243, 66, 254));
		paragraphs.add(seismicParagraph15);
		seismicParagraph15.addPossibleActivityName("Read data from hard drive of seismic station");
		Paragraph seismicParagraph16 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"The task force members check the condition of the seismic station.", new RGB(254, 66, 194));
		paragraphs.add(seismicParagraph16);
		seismicParagraph16.addPossibleActivityName("Check condition of seismic station");
		Paragraph seismicParagraph17 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"In case of depleted batteries, the batteries of the seismic station are replaced.", new RGB(254, 66, 128));
		paragraphs.add(seismicParagraph17);
		seismicParagraph17.addPossibleActivityName("Replace batteries");
		Paragraph seismicParagraph18 = new Paragraph(SEISMIC_ANALYSIS_1_0,
				"Checking of seismic stations takes place until the mission time is over.", new RGB(254, 66, 75));
		paragraphs.add(seismicParagraph18);
		Paragraph seismicParagraph19 = new Paragraph(SEISMIC_ANALYSIS_1_0, "The task force members undeploy all seismic stations.",
				new RGB(214, 10, 20));
		seismicParagraph19.addPossibleActivityName("Undeploy seismic stations");
		paragraphs.add(seismicParagraph19);

		return paragraphs;
	}

	private List<Paragraph> createTransportChange1Paragraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		String processId = EQUIPMENT_TRANSPORT_CHANGE1.getId();
		RGB color = new RGB(200, 200, 200);

		// base process
		Paragraph paragraph1 = new Paragraph(processId,
				"The transport of the equipment to the disaster area is organized by the task force.", color, 1);
		paragraphs.add(paragraph1);
		paragraph1.addPossibleActivityName("Organize transport of equipment");

		Paragraph paragraph2 = new Paragraph(processId, "The whole equipment is presented to German customs.", color, 2);
		paragraphs.add(paragraph2);
		paragraph2.addPossibleActivityName("Present equipment at customs in Germany");

		Paragraph paragraph3 = new Paragraph(processId,
				"Customs might require the demonstration of equipment devices. In this case, the demonstration happens right away.", color,
				4);
		paragraphs.add(paragraph3);

		Paragraph paragraph30 = new Paragraph(
				processId,
				"Customs might require the demonstration of equipment devices at the host country. In this case, the demonstration happens right away.",
				color, 16);
		paragraphs.add(paragraph30);

		Paragraph paragraph4 = new Paragraph(processId, "The task force flies to the disaster area in the host country.", color, 6);
		paragraphs.add(paragraph4);
		paragraph4.addPossibleActivityName("Fly to destination");

		Paragraph paragraph5 = new Paragraph(processId,
				"The members of the task force present themselves at the immigration office in the host country.", color, 7);
		paragraphs.add(paragraph5);
		paragraph5.addPossibleActivityName("Present at immigration");

		Paragraph paragraph6 = new Paragraph(processId, "The whole equipment is presented to customs of the host country.", color, 10);
		paragraphs.add(paragraph6);
		paragraph6.addPossibleActivityName("Present equipment at customs in host country");

		Paragraph paragraph7 = new Paragraph(processId, "The equipment is transported from customs to a storage location.", color, 21);
		paragraphs.add(paragraph7);
		paragraph7.addPossibleActivityName("Transport equipment to storage location");

		Paragraph paragraph8 = new Paragraph(processId,
				"The task force members organize an accommodation in the host country, preferably with electricity.", color, 11);
		paragraphs.add(paragraph8);
		paragraph8.addPossibleActivityName("Organize accommodation with electricity");

		Paragraph paragraph9 = new Paragraph(processId, "The task force members get road maps for the disaster area in the host country.",
				color, 12);
		paragraphs.add(paragraph9);
		paragraph9.addPossibleActivityName("Get road maps");

		Paragraph paragraph10 = new Paragraph(processId, "The task force members rent vehicles in the host country.", color, 9);
		paragraphs.add(paragraph10);
		paragraph10.addPossibleActivityName("Rent vehicles to transport equipment");

		Paragraph paragraph11 = new Paragraph(
				processId,
				"It might be the case that local car rental companies cannot provide sufficient transport capabilities. In this case, the task force members seek for vehicles of partner organisations.",
				color, 15);
		paragraphs.add(paragraph11);
		paragraph11.addPossibleActivityName("Seek for vehicles of partner organisations");

		// additional activities
		Paragraph paragraph12 = new Paragraph(processId, "Negotiate with customs", new RGB(36, 220, 0));
		paragraphs.add(paragraph12);

		Paragraph paragraph13 = new Paragraph(processId, "Trigger support from higher-ranked authorities", new RGB(30, 182, 0));
		paragraphs.add(paragraph13);

		Paragraph paragraph14 = new Paragraph(processId, "Transport partial equipment to storage location", new RGB(152, 252, 132));
		paragraphs.add(paragraph14);

		Paragraph paragraph15 = new Paragraph(processId,
				"Transport partial equipment to storage location after missing documents have been retrieved", new RGB(152, 252, 0));
		paragraphs.add(paragraph15);

		Paragraph paragraph16 = new Paragraph(processId, "Retrieve missing documents from home", new RGB(132, 252, 234));
		paragraphs.add(paragraph16);

		return paragraphs;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS_2009_10;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		Map<Process, String> mapping = new HashMap<Process, String>();
		mapping.put(EQUIPMENT_TRANSPORT_CHANGE1, TRANSPORT_OF_EQUIPMENT_CHANGE_1_INITIAL_MODEL);
		mapping.put(EQUIPMENT_TRANSPORT_CHANGE2, TRANSPORT_OF_EQUIPMENT_CHANGE_2_INITIAL_MODEL);
		mapping.put(SEISMIC_ANALYSIS_CHANGE1, SEISMIC_ANALYSIS_CHANGE_1_INITIAL_MODEL);
		mapping.put(SEISMIC_ANALYSIS_CHANGE2, SEISMIC_ANALYSIS_CHANGE_2_INITIAL_MODEL);
		mapping.put(SEISMIC_ANALYSIS_CHANGE3, SEISMIC_ANALYSIS_CHANGE_3_INITIAL_MODEL);
		return mapping;
	}

	@Override
	public List<Paragraph> getParagraphs() throws Exception {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();

		paragraphs.addAll(createEquipmentTransportParagraphs());
		paragraphs.addAll(createSeismicAnalysisParagraphs());
		paragraphs.addAll(createTransportChange1Paragraphs());

		return paragraphs;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EQUIPMENT_TRANSPORT_PROCESS);
		processes.add(EQUIPMENT_TRANSPORT_CHANGE1);
		processes.add(EQUIPMENT_TRANSPORT_CHANGE2);

		processes.add(SEISMIC_ANALYSIS_PROCESS);
		processes.add(SEISMIC_ANALYSIS_CHANGE1);
		processes.add(SEISMIC_ANALYSIS_CHANGE2);
		processes.add(SEISMIC_ANALYSIS_CHANGE3);
		return processes;
	}

	private void validateIds() {
		Set<Integer> ids = new HashSet<Integer>();
		for (WorkflowConfiguration configuration : configurations) {
			int id = configuration.getId();
			Assert.isTrue(!ids.contains(id));
			ids.add(id);
		}
	}
}
