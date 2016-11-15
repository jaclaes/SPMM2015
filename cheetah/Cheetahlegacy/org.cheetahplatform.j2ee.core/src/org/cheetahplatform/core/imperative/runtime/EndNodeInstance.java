/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;

public class EndNodeInstance extends ImperativeActivityInstance {

	private static final long serialVersionUID = -3075029033145854794L;

	public EndNodeInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);

		processInstance.setEndInstance(this);
	}

	@Override
	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitEndNodeInstance(this);
		visitSuccessors(visitor);
	}

	@Override
	public void launch() {
		super.launch();
		complete();
	}

	@Override
	public void requestActivation() {
		if (COMPLETED.equals(getState())) {
			return;
		}

		super.requestActivation();

		for (IImperativeNodeInstance predecessor : predecessorInstances) {
			if (!predecessor.getState().equals(COMPLETED)) {
				return;
			}
		}

		processInstance.requestTermination();
	}

	@Override
	public void skip(boolean propagate) {
		// ignore
	}

}
