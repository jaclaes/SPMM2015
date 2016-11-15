/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActiveLateBindingBoxVisitor extends AbstractNodeInstanceVisitor {
	private final List<LateBindingBoxInstance> instances;

	public ActiveLateBindingBoxVisitor() {
		this.instances = new ArrayList<LateBindingBoxInstance>();
	}

	/**
	 * Returns the instances.
	 * 
	 * @return the instances
	 */
	public List<LateBindingBoxInstance> getInstances() {
		return Collections.unmodifiableList(instances);
	}

	@Override
	public void visitLateBindingBox(LateBindingBoxInstance instance) {
		super.visitLateBindingBox(instance);

		if (instance.getState().equals(ACTIVATED)) {
			instances.add(instance);
		}
	}

}
