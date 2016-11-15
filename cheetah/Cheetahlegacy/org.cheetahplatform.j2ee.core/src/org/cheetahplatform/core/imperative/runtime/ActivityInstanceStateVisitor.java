/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class ActivityInstanceStateVisitor extends AbstractNodeInstanceVisitor {
	private final List<INodeInstance> instances;
	private INodeInstanceState state;

	public ActivityInstanceStateVisitor(INodeInstanceState state) {
		this.state = state;
		this.instances = new ArrayList<INodeInstance>();
	}

	public List<INodeInstance> getMatchedActivities() {
		HashSet<INodeInstance> unique = new HashSet<INodeInstance>(instances);
		return new ArrayList<INodeInstance>(unique);
	}

	private void handleActivity(INodeInstance instance) {
		if (instance.getState().equals(state)) {
			instances.add(instance);
		}
	}

	@Override
	public void visitActivity(ImperativeActivityInstance instance) {
		super.visitActivity(instance);
		handleActivity(instance);
	}

	@Override
	public void visitEndNodeInstance(EndNodeInstance instance) {
		super.visitEndNodeInstance(instance);
		handleActivity(instance);
	}

	@Override
	public void visitLateBindingBox(LateBindingBoxInstance instance) {
		super.visitLateBindingBox(instance);
		handleActivity(instance);
	}

	@Override
	public void visitLateModelingBox(LateModelingBoxInstance instance) {
		super.visitLateModelingBox(instance);
		handleActivity(instance);
	}
}
