package org.cheetahplatform.modeler.engine.configurations;

import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ISemanticalCorrectnessEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         28.10.2009
 */
public interface IExperimentConfiguration {

	boolean allowsRecovering();

	List<WorkflowConfiguration> createConfigurations();

	/**
	 * Return the process describing the *entire* experiment.
	 * 
	 * @return the process
	 */
	Process getExperimentProcess();

	Map<Process, Graph> getInitialModelMap();

	Map<Process, String> getInitialModelMapping();

	List<Paragraph> getParagraphs() throws Exception;

	/**
	 * Returns all processes provided by this experiment, e.g., a process for modeling and another for a change task.
	 * 
	 * @return all processes
	 */
	List<Process> getProcesses();

	List<ISemanticalCorrectnessEvaluation> getSemanticalCorrectnessEvaluations(String modelingProcess);
}