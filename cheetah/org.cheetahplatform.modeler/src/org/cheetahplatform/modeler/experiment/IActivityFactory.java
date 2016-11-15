package org.cheetahplatform.modeler.experiment;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.graph.model.Node;

public interface IActivityFactory {

	/**
	 * 
	 * @param type
	 *            The type of Experiment Workflow Activity to create
	 * @param subtype
	 *            The subtype of Experiment Workflow Activity to create - may be null or empty
	 * @param configuration
	 *            The configuration that is used to configure the new Experiment Workflow Activity. In case this is null no configuration
	 *            will be done
	 * @return A new Experiments Workflow Activity
	 */
	public AbstractExperimentsWorkflowActivity createActivity(String type, String subtype, Node configuration);
}
