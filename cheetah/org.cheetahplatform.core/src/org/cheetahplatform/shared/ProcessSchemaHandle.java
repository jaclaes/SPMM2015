package org.cheetahplatform.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.modeling.ProcessSchema;

public class ProcessSchemaHandle extends AbstractHandle {
	private final List<ProcessInstanceHandle> instances;
	/**
	 * Optional hint of how to visualize the process.
	 */
	private GraphHandle visualization;

	public ProcessSchemaHandle(long id, String name, GraphHandle visualization) {
		super(id, name);

		this.visualization = visualization;
		this.instances = new ArrayList<ProcessInstanceHandle>();
	}

	public ProcessSchemaHandle(ProcessSchema schema) {
		this(schema, null);
	}

	public ProcessSchemaHandle(ProcessSchema schema, GraphHandle visualization) {
		this(schema.getCheetahId(), schema.getName(), visualization);
	}

	public void addInstance(ProcessInstanceHandle instance) {
		instances.add(instance);
	}

	/**
	 * Returns the instances.
	 * 
	 * @return the instances
	 */
	public List<ProcessInstanceHandle> getInstances() {
		return Collections.unmodifiableList(instances);
	}

	public List<ProcessInstanceHandle> getInstancesWithActiveActivities() {
		List<ProcessInstanceHandle> instancesWithActiveActivities = new ArrayList<ProcessInstanceHandle>();
		for (ProcessInstanceHandle instance : instances) {
			if (instance.hasActiveActivities()) {
				instancesWithActiveActivities.add(instance);
			}
		}

		return instancesWithActiveActivities;
	}

	/**
	 * @return the visualization
	 */
	public GraphHandle getVisualization() {
		return visualization;
	}

	public boolean hasActiveActivities() {
		for (ProcessInstanceHandle instance : instances) {
			if (instance.hasActiveActivities()) {
				return true;
			}
		}

		return false;
	}

	public boolean hasInstances() {
		return !instances.isEmpty();
	}

	/**
	 * @param visualization
	 *            the visualization to set
	 */
	public void setVisualization(GraphHandle visualization) {
		this.visualization = visualization;
	}

}
