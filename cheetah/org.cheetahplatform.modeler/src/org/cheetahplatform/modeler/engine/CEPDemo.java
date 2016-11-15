package org.cheetahplatform.modeler.engine;

import static org.cheetahplatform.modeler.engine.configurations.ExperimentOctober2009.createModelingActivityWithInitialModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.configurations.AbstractExperimentalConfiguration;
import org.cheetahplatform.modeler.engine.configurations.ExperimentOctober2009;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.draw2d.geometry.Point;

public class CEPDemo extends AbstractExperimentalConfiguration {
	public static final Process EXPERIMENTAL_PROCESS_CEP_DEMO = new Process("bp_notation_4.0");
	private static final String INITIAL_MODEL = "CEPDemo_2010_05_transport_of_equipment.mxml";
	private static final String DEMONSTRATION_MODEL_1_0 = "demonstration_model_1.0";
	private static Process DEMONSTRATION_PROCESS = new Process(DEMONSTRATION_MODEL_1_0);

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

	public SurveyActivity createCognitiveLoadQuestions() {
		List<SurveyAttribute> mentalEffortActivitiesModelingTask = new ArrayList<SurveyAttribute>();
		ComboInputAttribute mentalEffortCombo = new ComboInputAttribute(
				"How would you assess the mental effort for completing the first change task?", true);
		addMentalEffortChoices(mentalEffortCombo);
		mentalEffortActivitiesModelingTask.add(mentalEffortCombo);

		return new SurveyActivity(mentalEffortActivitiesModelingTask);
	}

	private void createConfiguration(int id, Process assignment) {
		WorkflowConfiguration configuration = new WorkflowConfiguration(id);
		configuration
				.add(new ShowMessageActivity(
						"Welcome to CEP!",
						"Welcome to the Presentation of Cheetah Experimental Platform. The following steps will guide you through an experiment recently conducted with CEP."));

		configuration.add(ExperimentOctober2009.createDemographicSurveyActivity());
		configuration.add(new TutorialActivity());
		configuration.add(createModelingActivity(assignment));

		configuration.add(createCognitiveLoadQuestions());
		configuration.add(new ShowMessageActivity("Good Bye!", "Thank you for trying Cheetah Experimental Platform."));

		configurations.add(configuration);
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		if (configurations != null) {
			return configurations;
		}

		configurations = new ArrayList<WorkflowConfiguration>();
		createConfiguration(3249, DEMONSTRATION_PROCESS);

		return configurations;
	}

	private BPMNModelingActivity createModelingActivity(Process assignment1) {
		BPMNModelingActivity activity = createModelingActivityWithInitialModel(INITIAL_MODEL, assignment1);
		activity.setInitialSize(new Point(140, 60));
		return activity;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS_CEP_DEMO;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		Map<Process, String> mapping = new HashMap<Process, String>();
		mapping.put(DEMONSTRATION_PROCESS, INITIAL_MODEL);

		return mapping;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(DEMONSTRATION_PROCESS);
		return processes;
	}
}
