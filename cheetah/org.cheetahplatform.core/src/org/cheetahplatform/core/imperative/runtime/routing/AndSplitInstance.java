/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.CREATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class AndSplitInstance extends AbstractSplitInstance {

	private static final long serialVersionUID = 4055882875565855852L;

	public AndSplitInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	public void stateChanged(IImperativeNodeInstance instance) {
		INodeInstanceState state = instance.getState();

		if (state.equals(INodeInstanceState.COMPLETED)) {
			if (!this.state.equals(INodeInstanceState.COMPLETED)) {
				testAndSet(INodeInstanceState.COMPLETED);
			}
		} else if (state.equals(INodeInstanceState.LAUNCHED)) {
			successorLaunched();
		} else if (state.equals(INodeInstanceState.ACTIVATED)) {
			successorActivated();
		} else if (state.equals(INodeInstanceState.SKIPPED)) {
			Assert.isTrue(this.state.equals(INodeInstanceState.SKIPPED));
		}
	}

	/**
	 * A successor node has been activated.
	 */
	private void successorActivated() {
		if (this.state.equals(INodeInstanceState.ACTIVATED)) {
			return; // already activated
		}

		boolean switchToActive = true;
		for (IImperativeNodeInstance successor : successorInstances) {
			Assert.isTrue(!(successor.getState().equals(SKIPPED) || successor.getState().equals(CREATED)));
			if (!successor.getState().equals(INodeInstanceState.ACTIVATED)) {
				switchToActive = false;
			}
		}

		if (switchToActive) {
			testAndSet(INodeInstanceState.ACTIVATED);
		}
	}

	/**
	 * A successor node has been launched.
	 */
	private void successorLaunched() {
		boolean hasFinalSuccessor = false;

		for (IImperativeNodeInstance successor : successorInstances) {
			Assert.isTrue(!(successor.getState().equals(SKIPPED) || successor.getState().equals(CREATED)));
			if (successor.isInFinalState()) {
				hasFinalSuccessor = true;
			}
		}

		if (!hasFinalSuccessor && !this.state.equals(INodeInstanceState.LAUNCHED)) {
			testAndSet(INodeInstanceState.LAUNCHED);
		}
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitAndSplit(this);
		visitSuccessors(visitor);
	}
}
