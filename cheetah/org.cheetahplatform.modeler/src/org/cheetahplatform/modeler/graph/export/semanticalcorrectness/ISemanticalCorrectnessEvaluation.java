package org.cheetahplatform.modeler.graph.export.semanticalcorrectness;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.model.Graph;

public interface ISemanticalCorrectnessEvaluation {
	/**
	 * Evaluate the graph's correctness.
	 * 
	 * @param graph
	 *            the graph to be evaluated
	 * 
	 * @param handle
	 *            the handle from which the graph has been restored
	 * @return a value representing the graph's correctness
	 */
	double evaluate(Graph graph, ProcessInstanceDatabaseHandle handle);

	/**
	 * Returns the metric's name.
	 * 
	 * @return the name
	 */
	String getName();
}
