package org.cheetahplatform.modeler.engine.configurations;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.engine.ShowExperimentalWorkflowIdMessage;
import org.cheetahplatform.modeler.engine.ShowMessageActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class ExperimentSarahLoew extends AbstractExperimentalConfiguration {
	public static final Process EXPERIMENTAL_PROCESS_SARAH = new Process("bp_notation_3.0");

	private static final Process MODELING_TASK_1 = new Process("modeling_task_1_sarah_1.0");
	private static final Process MODELING_TASK_2 = new Process("modeling_task_2_sarah_1.0");

	private List<WorkflowConfiguration> configurations;

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		if (configurations != null) {
			return configurations;
		}

		configurations = new ArrayList<WorkflowConfiguration>();
		WorkflowConfiguration configuration = new WorkflowConfiguration(3249);
		configuration.add(new ShowExperimentalWorkflowIdMessage());
		configuration.add(new TutorialActivity());
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_1));
		configuration.add(new BPMNModelingActivity(new Graph(EditorRegistry.getDescriptors(BPMN)), MODELING_TASK_2));
		configuration.add(new ShowMessageActivity("Good Bye!",
				"Thank you very much for your cooperation - we hope you enjoyed participating the modeling session :-)"));
		configurations.add(configuration);

		return configurations;
	}

	@Override
	public Process getExperimentProcess() {
		return EXPERIMENTAL_PROCESS_SARAH;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(MODELING_TASK_1);
		processes.add(MODELING_TASK_2);

		return processes;
	}

}
