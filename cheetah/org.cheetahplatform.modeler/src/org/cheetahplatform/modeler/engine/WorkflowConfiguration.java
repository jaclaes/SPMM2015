package org.cheetahplatform.modeler.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         19.10.2009
 */
public class WorkflowConfiguration {
	private int id;
	private List<IExperimentalWorkflowActivity> activities;

	public WorkflowConfiguration(int code) {
		this.id = code;
		activities = new ArrayList<IExperimentalWorkflowActivity>();
	}

	public void add(IExperimentalWorkflowActivity activity) {
		activities.add(activity);
	}

	public void addAll(List<IExperimentalWorkflowActivity> toAdd) {
		for (IExperimentalWorkflowActivity current : toAdd) {
			add(current);
		}
	}

	/**
	 * Returns the activities.
	 * 
	 * @return the activities
	 */
	public List<IExperimentalWorkflowActivity> getActivites() {
		return Collections.unmodifiableList(activities);
	}

	/**
	 * Returns the code.
	 * 
	 * @return the code
	 */
	public int getId() {
		return id;
	}

	/**
	 * Initialize the configuration, if necessary.
	 */
	public void initialize() {
		// no initialization required by default
	}

	public boolean matches(int code) {
		return this.id == code;
	}

	/**
	 * Sets the code
	 */
	public void setId(int id) {
		this.id = id;
	}

}
