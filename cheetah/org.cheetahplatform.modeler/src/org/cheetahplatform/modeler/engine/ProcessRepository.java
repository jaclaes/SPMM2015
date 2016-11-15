package org.cheetahplatform.modeler.engine;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.literatemodeling.ILiterateExperimentConfiguration;
import org.cheetahplatform.literatemodeling.LiterateInitialValues;
import org.cheetahplatform.modeler.engine.configurations.AbstractExperimentHolder;
import org.cheetahplatform.modeler.engine.configurations.CreativityExperiment;
import org.cheetahplatform.modeler.engine.configurations.ExperimentJanuary2010;
import org.cheetahplatform.modeler.engine.configurations.ExperimentLayout;
import org.cheetahplatform.modeler.engine.configurations.ExperimentLayoutNovember2011;
import org.cheetahplatform.modeler.engine.configurations.ExperimentOctober2009;
import org.cheetahplatform.modeler.engine.configurations.ExperimentSarahLoew;
import org.cheetahplatform.modeler.engine.configurations.ExperimentTDMMaintainability;
import org.cheetahplatform.modeler.engine.configurations.ExpertModelingExperiment;
import org.cheetahplatform.modeler.engine.configurations.ExpertModelingTwoModelingTasksExperiment;
import org.cheetahplatform.modeler.engine.configurations.IExperimentConfiguration;
import org.cheetahplatform.modeler.engine.configurations.LabBarbaraSS2011BPMNConfiguration;
import org.cheetahplatform.modeler.engine.configurations.LabBarbaraSS2011BPMNLayoutConfiguration;
import org.cheetahplatform.modeler.engine.configurations.LabBarbaraWs2010BPMNConfiguration;
import org.cheetahplatform.modeler.engine.configurations.LabBarbaraWs2010DecSerFlowConfiguration;
import org.cheetahplatform.modeler.engine.configurations.NovicesVersusExpertsExperiment;
import org.cheetahplatform.modeler.experiment.Experiment;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 *
 *         28.10.2009
 */
public class ProcessRepository {
	public static IExperimentConfiguration getExperiment(String experimentalProcessId) {
		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			if (experiment.getExperimentProcess().getId().equals(experimentalProcessId)) {
				return experiment;
			}
		}

		return null;
	}

	/**
	 * Returns all processes describing experiments.
	 *
	 * @return all experimental processes
	 */
	public static List<Process> getExperimentalProcesses() {
		List<Process> processes = new ArrayList<Process>();
		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			processes.add(experiment.getExperimentProcess());
		}

		return processes;
	}

	public static IExperimentConfiguration getExperimentByModelingProcess(Process modelingProcess) {
		for (IExperimentConfiguration configuration : EXPERIMENTS) {
			if (configuration.getProcesses().contains(modelingProcess)) {
				return configuration;
			}
		}

		throw new IllegalArgumentException("No configuration found for id: " + modelingProcess);
	}

	public static LiterateInitialValues getInitialLiterateModel(Process process) {
		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			if (experiment instanceof ILiterateExperimentConfiguration) {
				ILiterateExperimentConfiguration litEx = (ILiterateExperimentConfiguration) experiment;
				LiterateInitialValues val = litEx.getInitialLiterateModelMapping().get(process);
				if (val != null) {
					return val;
				}
			}
		}
		return null;
	}

	public static Graph getInitialModel(Process process) {
		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			Graph initialGraph = experiment.getInitialModelMap().get(process);
			if (initialGraph != null) {
				return initialGraph;
			}
		}

		return null;
	}

	public static String getInitialModelPath(Process process) {
		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			String modelPath = experiment.getInitialModelMapping().get(process);
			if (modelPath != null) {
				return modelPath;
			}
		}
		return null;
	}

	public static Process getProcess(String processId) {
		List<Process> all = new ArrayList<Process>();
		all.addAll(PROCESSES);

		for (Process process : PROCESSES) {
			if (process.getId().equals(processId)) {
				return process;
			}
		}

		throw new IllegalArgumentException("No process found for id: " + processId);
	}

	private static void initializeExperimentProcesses() {
		EXPERIMENTS.add(new ExperimentOctober2009());
		EXPERIMENTS.add(new ExperimentJanuary2010());
		EXPERIMENTS.add(new ExperimentSarahLoew());
		EXPERIMENTS.add(new CEPDemo());
		EXPERIMENTS.add(new ExperimentTDMMaintainability());
		EXPERIMENTS.add(new NovicesVersusExpertsExperiment());
		EXPERIMENTS.add(new LabBarbaraWs2010BPMNConfiguration());
		EXPERIMENTS.add(new LabBarbaraWs2010DecSerFlowConfiguration());
		EXPERIMENTS.add(new ExperimentLayout());
		EXPERIMENTS.add(new ExperimentLayoutNovember2011());
		EXPERIMENTS.add(new ExpertModelingExperiment());
		EXPERIMENTS.add(new ExpertModelingTwoModelingTasksExperiment());
		EXPERIMENTS.add(new CreativityExperiment());
		EXPERIMENTS.add(new LabBarbaraSS2011BPMNLayoutConfiguration());
		EXPERIMENTS.add(new LabBarbaraSS2011BPMNConfiguration());

		// SPMExperiment
		EXPERIMENTS.add(new AbstractExperimentHolder() {
			private final Process BPMN_CASE_1 = new Process("BPMNCase1");
			private final Process BPMN_CASE_2 = new Process("BPMNCase2");
			private final Process EXPERIMENTAL_PROCESS = new Process("spm2013");

			@Override
			public Process getExperimentProcess() {
				return EXPERIMENTAL_PROCESS;
			}

			@Override
			public List<Process> getProcesses() {
				List<Process> processes = new ArrayList<Process>();
				processes.add(EXPERIMENTAL_PROCESS);
				processes.add(BPMN_CASE_1);
				processes.add(BPMN_CASE_2);
				return processes;
			}
		});

		// REAxperiment2
		EXPERIMENTS.add(new AbstractExperimentHolder() {
			private final Process Experiment = new Process("Business Modelling Eperiment");
			private final Process REAmodelling = new Process("REA Business modelling");
			private final Process ERmodelling = new Process("ER Business modelling");

			@Override
			public Process getExperimentProcess() {
				return Experiment;
			}

			@Override
			public List<Process> getProcesses() {
				List<Process> processes = new ArrayList<Process>();
				processes.add(Experiment);
				processes.add(REAmodelling);
				processes.add(ERmodelling);
				return processes;
			}
		});

		// REAxperiment
		EXPERIMENTS.add(new AbstractExperimentHolder() {
			private final Process REAmodellingEperiment = new Process("REAmodellingEperiment");
			private final Process REAmodelling = new Process("REAmodelling");
			private final Process ERmodelling = new Process("REAmodelling");

			@Override
			public Process getExperimentProcess() {
				return REAmodellingEperiment;
			}

			@Override
			public List<Process> getProcesses() {
				List<Process> processes = new ArrayList<Process>();
				processes.add(REAmodellingEperiment);
				processes.add(REAmodelling);
				processes.add(ERmodelling);
				return processes;
			}
		});

		// SPMTExperiment
		EXPERIMENTS.add(new AbstractExperimentHolder() {
			private final Process BPMN_CASE_1 = new Process("BPMNCase1");
			private final Process EXPERIMENTAL_PROCESS = new Process("spmt2014");

			@Override
			public Process getExperimentProcess() {
				return EXPERIMENTAL_PROCESS;
			}

			@Override
			public List<Process> getProcesses() {
				List<Process> processes = new ArrayList<Process>();
				processes.add(EXPERIMENTAL_PROCESS);
				processes.add(BPMN_CASE_1);
				return processes;
			}
		});

		// MovesExperiment
		EXPERIMENTS.add(new AbstractExperimentHolder() {
			private final Process BPMN_CASE_1 = new Process("BPMNCase1");
			private final Process EXPERIMENTAL_PROCESS = new Process("moves2014");

			@Override
			public Process getExperimentProcess() {
				return EXPERIMENTAL_PROCESS;
			}

			@Override
			public List<Process> getProcesses() {
				List<Process> processes = new ArrayList<Process>();
				processes.add(EXPERIMENTAL_PROCESS);
				processes.add(BPMN_CASE_1);
				return processes;
			}
		});

		// SPMMExperiment
		EXPERIMENTS.add(new AbstractExperimentHolder() {
			private final Process EXPERIMENTAL_PROCESS = new Process("spmm2015");
			private final Process OSPAN = new Process("OSpan");
			private final Process CSPAN = new Process("CSpan");
			private final Process RSPAN = new Process("RSpan");
			private final Process HFT1 = new Process("HFT1");
			private final Process HFT2 = new Process("HFT2");
			private final Process BPMN_CASE_1 = new Process("BPMNCase1");
			private final Process BPMN_CASE_2 = new Process("BPMNCase2");
			private final Process BPMN_CASE_3 = new Process("BPMNCase3");

			@Override
			public Process getExperimentProcess() {
				return EXPERIMENTAL_PROCESS;
			}

			@Override
			public List<Process> getProcesses() {
				List<Process> processes = new ArrayList<Process>();
				processes.add(EXPERIMENTAL_PROCESS);
				processes.add(OSPAN);
				processes.add(CSPAN);
				processes.add(RSPAN);
				processes.add(HFT1);
				processes.add(HFT2);
				processes.add(BPMN_CASE_1);
				processes.add(BPMN_CASE_2);
				processes.add(BPMN_CASE_3);
				return processes;
			}
		});

		// the generic experiment that loads the config from the file experiment.xml in the workspace
		EXPERIMENTS.add(new Experiment());

		for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor("org.cheetahplatform.experiment")) {
			if (element.getName().equals("experiment")) {
				try {
					IExperimentConfiguration configuration = (IExperimentConfiguration) element.createExecutableExtension("class");
					EXPERIMENTS.add(configuration);
				} catch (CoreException e) {
					org.cheetahplatform.common.Activator.logError("Could not create an executable extension", e);
				}
			}
		}

		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			PROCESSES.addAll(experiment.getProcesses());
		}
		for (IExperimentConfiguration experiment : EXPERIMENTS) {
			PROCESSES.add(experiment.getExperimentProcess());
		}
	}

	public static boolean isExperimentalWorkflow(Process process) {
		for (IExperimentConfiguration configuration : EXPERIMENTS) {
			if (configuration.getExperimentProcess().getId().equals(process.getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param modelingInstance
	 * @return
	 */
	public static boolean isModelingProcess(ProcessInstance modelingInstance) {
		String id = modelingInstance.getAttributeSafely(CommonConstants.ATTRIBUTE_PROCESS);
		for (Process process : PROCESSES) {
			if (process.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	private static List<Process> PROCESSES;

	private static List<IExperimentConfiguration> EXPERIMENTS;

	static {
		PROCESSES = new ArrayList<Process>();
		EXPERIMENTS = new ArrayList<IExperimentConfiguration>();

		initializeExperimentProcesses();
		PROCESSES.add(ExperimentalWorkflowEngine.MODELING_PROCESS);
	}

}
