package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.engine.configurations.SurveyUtil.createMandatoryLikertQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.engine.DecSerFlowModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.ShowExperimentalWorkflowIdMessage;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.MessageSurveyAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class ExperimentTDMMaintainability extends AbstractExperimentalConfiguration {

	private static final String INITIAL_MODEL_FOR_CHANGE_TASK_2 = "bp_notation_6.0/model_3.mxml";
	private static final String INITIAL_MODEL_FOR_CHANGE_TASK_1 = "bp_notation_6.0/model_2.mxml";
	private static final Process EXPERIMENTAL_PROCESS = new Process("bp_notation_6.0");
	private static final Process CHANGE_TASK_1 = new Process("tdm_experiment_change_task_1");
	private static final Process CHANGE_TASK_2 = new Process("tdm_experiment_change_task_2");

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private IExperimentalWorkflowActivity createConcludingSurveyActivity() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		attributes.add(new MessageSurveyAttribute("tests",
				"Please answer the following questions with respect to the change tasks where tests were provided:"));
		attributes.add(createMandatoryLikertQuestion("I feel very confident that the changes I made are correct (with tests)."));
		ComboInputAttribute cognitiveLoadTests = new ComboInputAttribute(
				"How would you assess the mental effort for completing the change tasks (with tests)? ", true);
		SurveyUtil.addMentalEffortChoices(cognitiveLoadTests);
		attributes.add(cognitiveLoadTests);

		attributes.add(new MessageSurveyAttribute("separator", ""));
		attributes.add(new MessageSurveyAttribute("without tests",
				"Please answer the following questions with respect to the change tasks where *no* tests were provided:"));
		attributes.add(createMandatoryLikertQuestion("I feel very confident that the changes I made are correct (without tests)."));
		ComboInputAttribute cognitiveLoadWithoutTests = new ComboInputAttribute(
				"How would you assess the mental effort for completing the change tasks (without tests)?", true);
		SurveyUtil.addMentalEffortChoices(cognitiveLoadWithoutTests);
		attributes.add(cognitiveLoadWithoutTests);

		return new SurveyActivity(attributes);
	}

	private WorkflowConfiguration createConfiguration1() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(3482);
		configuration.add(createSociodemographicSurveyActivity());
		configuration.add(new ShowExperimentalWorkflowIdMessage());
		configuration.add(new TDMModelingActivity(TDMModelingActivity.MODEL_2, CHANGE_TASK_1));
		configuration.add(DecSerFlowModelingActivity.createActivity(CHANGE_TASK_2, INITIAL_MODEL_FOR_CHANGE_TASK_2));
		configuration.add(createConcludingSurveyActivity());
		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!", "We hope you enjoyed participating in the modeling session :-)"));

		return configuration;
	}

	private WorkflowConfiguration createConfiguration2() {
		WorkflowConfiguration configuration = new WorkflowConfiguration(7198);
		configuration.add(createSociodemographicSurveyActivity());
		configuration.add(new ShowExperimentalWorkflowIdMessage());
		configuration.add(DecSerFlowModelingActivity.createActivity(CHANGE_TASK_1, INITIAL_MODEL_FOR_CHANGE_TASK_1));
		configuration.add(new TDMModelingActivity(TDMModelingActivity.MODEL_3, CHANGE_TASK_2));
		configuration.add(createConcludingSurveyActivity());
		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!", "We hope you enjoyed participating in the modeling session :-)"));
		return configuration;
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		configurations.add(createConfiguration1());
		configurations.add(createConfiguration2());

		return configurations;
	}

	private IExperimentalWorkflowActivity createSociodemographicSurveyActivity() {
		List<SurveyAttribute> attributes = new ArrayList<SurveyAttribute>();
		ComboInputAttribute professionAttribute = new ComboInputAttribute("Which description matches best your current work status?", true);
		professionAttribute.addChoice("Student");
		professionAttribute.addChoice("Academic");
		professionAttribute.addChoice("Professional");
		attributes.add(professionAttribute);

		attributes.add(new IntegerInputAttribute("How many years ago did you start process modeling?", true, 0, 1000));
		attributes
				.add(new IntegerInputAttribute(
						"How many process models have you analyzed or read within the last 12 months? A year has about 250 work days. In case you read one model per day, this would sum up to 250 models per year.",
						true, 0, 1000));
		attributes.add(new IntegerInputAttribute("How many process model have you created or edited within the last 12 months?", true, 0,
				1000));
		attributes.add(new IntegerInputAttribute("How many activities did all these models have on average?", true, 0, 1000));
		attributes
				.add(new IntegerInputAttribute(
						"How many work days of formal training on process modeling have you received within the last 12 months? This includes e.g. university lectures, certification courses, training courses. 15 weeks of a 90 minutes university lecture is roughly 3 work days.",
						true, 0, 1000));
		attributes
				.add(new IntegerInputAttribute(
						"How many work days of self education have you made within the last 12 months? This includes e.g. learning-by-doing, learning-on-the-fly, self-study of textbooks or specifications.",
						true, 0, 1000));
		attributes.add(createMandatoryLikertQuestion("Overall, I am very familiar with DecSerFlow."));
		attributes.add(createMandatoryLikertQuestion("I feel very confident in understanding process models created with DecSerFlow."));
		attributes.add(createMandatoryLikertQuestion("I feel very competent in using DecSerFlow for process modeling."));
		attributes.add(new IntegerInputAttribute("How many months ago did you start using DecSerFlow?", true, 0, 1000));

		return new SurveyActivity(attributes);
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		Map<Process, String> mapping = new HashMap<Process, String>();
		mapping.put(CHANGE_TASK_1, INITIAL_MODEL_FOR_CHANGE_TASK_1);
		mapping.put(CHANGE_TASK_2, INITIAL_MODEL_FOR_CHANGE_TASK_2);

		return mapping;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(CHANGE_TASK_1);
		processes.add(EXPERIMENTAL_PROCESS);
		processes.add(CHANGE_TASK_1);
		processes.add(CHANGE_TASK_2);

		return processes;
	}

}
