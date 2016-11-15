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
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2010
 */
public class LabBarbaraWs2010BPMNConfiguration extends AbstractExperimentalConfiguration {
	private static final String CHANGE_PATTERN_MODELING_INITIALGRAPH = "changepattern/initialgraph.mxml";
	private static final String MODIFICATION_INITIALGRAPH = "changepattern/2010_06_transport_of_equipment_change_pattern.mxml";
	public static final Process CHANGE_PATTERN_EXPERIMENTAL_PROCESS_2_0 = new Process("bpmn_change_task_process_2.0");
	public static final Process CHANGE_PATTERN_MODELING_PROCESS_2_0 = new Process("bpmn_change_pattern_process_2.0");
	private static final Process CHANGE_PATTERN_MODIFICATION_PROCESS_2_0 = new Process("bpmn_change_pattern_modification_process_2.0");
	private static final Process BPMN_MODELING_PROCESS_2_0 = new Process("equipment_transport_2.0");
	private static final Process BPMN_MODIFICATION_PROCESS_2_0 = new Process("equipment_transport_change1_2.0");

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

	private WorkflowConfiguration createBPMNConfiguration() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(2573);
		configuration.add(createDemographicSurveyActivity());
		// configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_MODELING_PROCESS_2_0));

		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + MODIFICATION_INITIALGRAPH),
				new HashMap<Object, Object>());
		try {
			configuration.add(new BPMNModelingActivity(input.openStream(), graph, BPMN_MODIFICATION_PROCESS_2_0));
		} catch (Exception e) {
			Activator.logError("Could not create configuration for change task", e);
		}

		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating in the modeling session :-)"));
		return configuration;
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

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		configurations.add(createBPMNConfiguration());
		return configurations;
	}

	private IExperimentalWorkflowActivity createDemographicSurveyActivity() {
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

		addBPMNFamiliarity(attributes);

		ComboInputAttribute dis = new ComboInputAttribute("I am very familiar with Disaster Management Processes.", true);
		addChoices(dis);
		attributes.add(dis);

		return new SurveyActivity(attributes);
	}

	@Override
	public Process getExperimentProcess() {
		return CHANGE_PATTERN_EXPERIMENTAL_PROCESS_2_0;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		HashMap<Process, String> initialModels = new HashMap<Process, String>();
		initialModels.put(CHANGE_PATTERN_MODELING_PROCESS_2_0, CHANGE_PATTERN_MODELING_INITIALGRAPH);
		initialModels.put(CHANGE_PATTERN_MODIFICATION_PROCESS_2_0, MODIFICATION_INITIALGRAPH);
		initialModels.put(BPMN_MODIFICATION_PROCESS_2_0, MODIFICATION_INITIALGRAPH);
		return initialModels;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(CHANGE_PATTERN_MODELING_PROCESS_2_0);
		processes.add(CHANGE_PATTERN_MODIFICATION_PROCESS_2_0);
		processes.add(BPMN_MODELING_PROCESS_2_0);
		processes.add(BPMN_MODIFICATION_PROCESS_2_0);
		return processes;
	}
}
