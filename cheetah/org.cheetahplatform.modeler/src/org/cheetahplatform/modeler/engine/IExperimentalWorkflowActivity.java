package org.cheetahplatform.modeler.engine;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;

/**
 * A step within the experimental workflow engine.
 * 
 * @author Stefan Zugal
 * 
 */
public interface IExperimentalWorkflowActivity {
	/**
	 * Execute the step - call must be blocking, the engine assumes that the activity after returning from this step.
	 * 
	 * @return a list of attributes, which are stored by the engine (e.g., produced data).
	 */
	List<Attribute> execute();

	/**
	 * Return an id identifying this kind of step.
	 * 
	 * @return an id
	 */
	String getId();

	/**
	 * Returns a human readable name of the activity.
	 * 
	 * @return a human readable name of the activity
	 */
	Object getName();

	/**
	 * Assumes the case that the activity could not be finished successfully. Tries to restart the activity, e.g., when the activity allows
	 * the user to create a model, the activity must recover the model.
	 * 
	 * @return a list of attributes, which are stored by the engine (e.g., produced data).
	 */
	List<Attribute> restart();
}
