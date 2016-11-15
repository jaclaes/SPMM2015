package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.FeedbackActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.SurveyActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;

public class LabBarbaraSS2011BPMNConfiguration extends AbstractExperimentalConfiguration {
	private static final Process BPMN_COMPLEX_EXPERIMENTAL_PROCESS_2_0 = new Process("lab_ss2010_complex_bpmn_modeling_1.0");
	private static final Process BPMN_COMPLEX_MODELING_PROCESS_2_0 = new Process("complex_mortgage_1.0");

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
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), BPMN_COMPLEX_MODELING_PROCESS_2_0));
		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new FeedbackActivity());
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating in the modeling session :-)"));
		return configuration;
	}

	private SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the modeling task?", true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);

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

		attributes.addAll(ProcessModelingExperienceQuestionnaire.getSurveyAttributes());
		attributes.addAll(BPMNFamilarityQuestionnaire.getSurveyAttributes());

		ComboInputAttribute dis = new ComboInputAttribute("I am very familiar with Disaster Management Processes.", true);
		addChoices(dis);
		attributes.add(dis);

		return new SurveyActivity(attributes);
	}

	@Override
	public Process getExperimentProcess() {
		return BPMN_COMPLEX_EXPERIMENTAL_PROCESS_2_0;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		return Collections.emptyMap();
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(BPMN_COMPLEX_MODELING_PROCESS_2_0);
		return processes;
	}
}
