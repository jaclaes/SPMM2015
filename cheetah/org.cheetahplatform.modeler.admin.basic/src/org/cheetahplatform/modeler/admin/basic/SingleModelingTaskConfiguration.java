package org.cheetahplatform.modeler.admin.basic;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.literatemodeling.ILiterateExperimentConfiguration;
import org.cheetahplatform.literatemodeling.LiterateInitialValues;
import org.cheetahplatform.literatemodeling.LiterateModelingActivity;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.changepattern.ChangePatternModelingActivity;
import org.cheetahplatform.modeler.engine.DecSerFlowModelingActivity;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.engine.configurations.AbstractExperimentalConfiguration;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class SingleModelingTaskConfiguration extends AbstractExperimentalConfiguration implements ILiterateExperimentConfiguration {

	private static final String CHANGE_PATTERN_1_0_INITIALGRAPH = "changepattern/initialgraph.mxml";
	private static final String CHANGE_PATTERN_MODIFICATION_1_0_INITIALGRAPH = "changepattern/2010_06_transport_of_equipment_change_pattern.mxml";
	private static final String CEP_DEMO_2010_05_TRANSPORT_OF_EQUIPMENT_CHANGE_1_MXML = "CEPDemo_2010_05_transport_of_equipment_change_1.mxml";
	public static final Process SINGLE_MODELING_TASK_PROCESS = new Process("single_modeling_task_process_1.0");
	public static final Process BPMN_CHANGE_TASK_PROCESS_1_0 = new Process("bpmn_change_task_process_1.0");
	private static final Process BPMN_CHANGE_PATTERN_PROCESS_1_0 = new Process("bpmn_change_pattern_process_1.0");
	private static final Process BPMN_CHANGE_PATTERN_MODIFICATION_PROCESS_1_0 = new Process("bpmn_change_pattern_modification_process_1.0");
	public static final Process LIPROMO_PROCESS = new Process("lipromo_modeling_process_1.0");

	private Map<Process, LiterateInitialValues> lmInitials;

	private List<WorkflowConfiguration> configurations;

	@Override
	public boolean allowsRecovering() {
		return false;
	}

	private WorkflowConfiguration createBPMNConfiguration() {
		WorkflowConfiguration bpmnConfiguration = new WorkflowConfiguration(1);
		bpmnConfiguration.add(new BPMNModelingActivity(null, ExperimentalWorkflowEngine.MODELING_PROCESS));
		return bpmnConfiguration;
	}

	private WorkflowConfiguration createCEPDemoConfiguration() {
		WorkflowConfiguration bpmnConfigurationWithIntialGraph = new WorkflowConfiguration(2);
		Graph graph = new Graph(EditorRegistry.getDescriptors(BPMN));
		URL input = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/"
				+ CEP_DEMO_2010_05_TRANSPORT_OF_EQUIPMENT_CHANGE_1_MXML), new HashMap<Object, Object>());
		try {
			BPMNModelingActivity activity = new BPMNModelingActivity(input.openStream(), graph, BPMN_CHANGE_TASK_PROCESS_1_0);
			bpmnConfigurationWithIntialGraph.add(activity);
		} catch (Exception e) {
			Activator.logError("Could not create configuration: " + CEP_DEMO_2010_05_TRANSPORT_OF_EQUIPMENT_CHANGE_1_MXML, e);
		}

		return bpmnConfigurationWithIntialGraph;
	}

	private WorkflowConfiguration createChangePatternConfiguration() {
		WorkflowConfiguration changePatternConfiguration = new WorkflowConfiguration(5);
		changePatternConfiguration.add(new ChangePatternModelingActivity(BPMN_CHANGE_PATTERN_PROCESS_1_0, CHANGE_PATTERN_1_0_INITIALGRAPH));
		return changePatternConfiguration;
	}

	private WorkflowConfiguration createChangePatternModificationConfiguration() {
		WorkflowConfiguration changePatternConfiguration = new WorkflowConfiguration(6);
		changePatternConfiguration.add(new ChangePatternModelingActivity(BPMN_CHANGE_PATTERN_MODIFICATION_PROCESS_1_0,
				CHANGE_PATTERN_MODIFICATION_1_0_INITIALGRAPH));
		return changePatternConfiguration;
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		if (configurations != null) {
			return configurations;
		}

		configurations = new ArrayList<WorkflowConfiguration>();

		configurations.add(createBPMNConfiguration());
		configurations.add(createDecSerFlowConfiguration());
		configurations.add(createCEPDemoConfiguration());
		configurations.add(createTDMConfiguration());
		configurations.add(createChangePatternConfiguration());
		configurations.add(createChangePatternModificationConfiguration());
		configurations.add(createLiProMoConfiguration());

		return configurations;
	}

	private WorkflowConfiguration createDecSerFlowConfiguration() {
		WorkflowConfiguration decSerFlowConfigration = new WorkflowConfiguration(3);
		decSerFlowConfigration.add(new DecSerFlowModelingActivity(null, ExperimentalWorkflowEngine.MODELING_PROCESS));
		return decSerFlowConfigration;
	}

	private WorkflowConfiguration createLiProMoConfiguration() {
		WorkflowConfiguration config = new WorkflowConfiguration(7);

		LiterateModel lmGraph = new LiterateModel(EditorRegistry.getDescriptors(BPMN), getInitialLiterateModelMapping()
				.get(LIPROMO_PROCESS));
		LiterateModelingActivity lma = new LiterateModelingActivity(lmGraph, LIPROMO_PROCESS);
		config.add(lma);

		return config;
	}

	private WorkflowConfiguration createTDMConfiguration() {
		WorkflowConfiguration tdmConfiguration = new WorkflowConfiguration(4);
		tdmConfiguration.add(new TDMModelingActivity(TDMModelingActivity.DEFAULT_MODEL, ExperimentalWorkflowEngine.MODELING_PROCESS));

		return tdmConfiguration;
	}

	@Override
	public Process getExperimentProcess() {
		return SINGLE_MODELING_TASK_PROCESS;
	}

	@Override
	public Map<Process, LiterateInitialValues> getInitialLiterateModelMapping() {
		if (lmInitials == null) {
			lmInitials = new HashMap<Process, LiterateInitialValues>();
			lmInitials.put(LIPROMO_PROCESS, new LiterateInitialValues("Process", "Short Description",
					"Please enter your process description."));
		}

		return lmInitials;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		HashMap<Process, String> initialModels = new HashMap<Process, String>();
		initialModels.put(BPMN_CHANGE_TASK_PROCESS_1_0, "CEPDemo_2010_05_transport_of_equipment_change_1.mxml");
		initialModels.put(BPMN_CHANGE_PATTERN_PROCESS_1_0, CHANGE_PATTERN_1_0_INITIALGRAPH);
		initialModels.put(BPMN_CHANGE_PATTERN_MODIFICATION_PROCESS_1_0, CHANGE_PATTERN_MODIFICATION_1_0_INITIALGRAPH);
		return initialModels;
	}

	@Override
	public List<Process> getProcesses() {
		List<Process> processes = new ArrayList<Process>();
		processes.add(ExperimentalWorkflowEngine.MODELING_PROCESS);
		processes.add(BPMN_CHANGE_TASK_PROCESS_1_0);
		processes.add(BPMN_CHANGE_PATTERN_PROCESS_1_0);
		processes.add(BPMN_CHANGE_PATTERN_MODIFICATION_PROCESS_1_0);
		processes.add(SINGLE_MODELING_TASK_PROCESS);
		processes.add(LIPROMO_PROCESS);

		return processes;
	}
}
