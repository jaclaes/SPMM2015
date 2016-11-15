/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;

import java.util.ArrayList;
import java.util.List;

public class ActiveLateModelingBoxesVisitor extends AbstractNodeInstanceVisitor {
	private final List<LateModelingBoxInstance> instances;

	public ActiveLateModelingBoxesVisitor() {
		instances = new ArrayList<LateModelingBoxInstance>();
	}

	/**
	 * Returns the instances.
	 * 
	 * @return the instances
	 */
	public List<LateModelingBoxInstance> getInstances() {
		return instances;
	}

	@Override
	public void visitLateModelingBox(LateModelingBoxInstance instance) {
		super.visitLateModelingBox(instance);

		if (instance.getState().equals(ACTIVATED)) {
			instances.add(instance);
		}
	}

}
