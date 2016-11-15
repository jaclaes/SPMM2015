/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.common.logging;

import java.util.ArrayList;
import java.util.List;

public class Process extends DataContainer {
	private final String id;
	private final List<ProcessInstance> instances;

	public Process(String id) {
		this.id = id;
		this.instances = new ArrayList<ProcessInstance>();
	}

	public void add(ProcessInstance instance) {
		instances.add(instance);
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the instances.
	 * 
	 * @return the instances.
	 */
	public List<ProcessInstance> getInstances() {
		return instances;
	}

}
