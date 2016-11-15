package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.engine.DecSerFlowModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.07.2010
 */
public class LabBarbaraWs2010DecSerFlowConfiguration extends AbstractExperimentalConfiguration {
	private static final Process DECSERFLOW_MODELING_PROCESS_1_0 = new Process("decserflow_1.0");
	private static final Process EXPERIMENTAL_PROCESS = new Process("lab_ws_2010_decserflow_experimental_process_1.0");

	private static void addChoices(ComboInputAttribute combo) {
		combo.addChoice("strongly agree");
		combo.addChoice("agree");
		combo.addChoice("somewhat agree");
		combo.addChoice("neutral");
		combo.addChoice("somewhat disagree");
		combo.addChoice("disagree");
		combo.addChoice("strongly disagree");
	}

	private static void addDecSerFlowFamiliarity(List<SurveyAttribute> attributes) {
		ComboInputAttribute decSerFlowFamiliarity = new ComboInputAttribute("Overall, I am very familiar with DecSerFlow.", true);
		addChoices(decSerFlowFamiliarity);
		attributes.add(decSerFlowFamiliarity);

		ComboInputAttribute fam2 = new ComboInputAttribute(
				"I feel very confident in understanding process models created with DecSerFlow.", true);
		addChoices(fam2);
		attributes.add(fam2);

		ComboInputAttribute fam3 = new ComboInputAttribute("I feel very competent in using DecSerFlow for process modeling.", true);
		addChoices(fam3);
		attributes.add(fam3);
	}

	private void addMentalEffortChoices(ComboInputAttribute combo) {
		combo.addChoice("Very High");
		combo.addChoice("High");
		combo.addChoice("Rather High");
		combo.addChoice("Medium");
		combo.addChoice("Rather Low");
		combo.addChoice("Low");
		combo.addChoice("Very Low");
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private WorkflowConfiguration createBPMNConfiguration() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(6841);
		configuration.add(createDemographicSurveyActivity());
		configuration.add(new DecSerFlowModelingActivity(new Graph(EditorRegistry.getDescriptors(EditorRegistry.DECSERFLOW)),
				DECSERFLOW_MODELING_PROCESS_1_0));

		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating in the modeling session :-)"));
		return configuration;
	}

	private SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute("How would you assess the mental effort for completing the task?",
				true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);

		// ComboInputAttribute mentalEffortComboChangeTask = new ComboInputAttribute(
		// "How would you assess the mental effort for completing the second task?", true);
		// addMentalEffortChoices(mentalEffortComboChangeTask);
		// mentalEffortActivitiesModelingTask.add(mentalEffortComboChangeTask);

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

		addDecSerFlowFamiliarity(attributes);

		return new SurveyActivity(attributes);
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(DECSERFLOW_MODELING_PROCESS_1_0);

		return processes;
	}
}
