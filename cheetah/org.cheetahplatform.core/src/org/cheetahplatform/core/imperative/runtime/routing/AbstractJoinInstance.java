/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.LAUNCHED;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceStateChangeListener;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public abstract class AbstractJoinInstance extends AbstractRoutingInstance implements INodeInstanceStateChangeListener {

	private static final long serialVersionUID = -4573324682933612541L;

	protected AbstractJoinInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	protected boolean allPredecessorsInFinalState() {
		boolean allPredecessorsCreated = node.getPredecessors().size() == predecessorInstances.size();
		boolean allPredecessorsFinal = true;
		for (IImperativeNodeInstance predecessor : predecessorInstances) {
			if (!predecessor.isInFinalState()) {
				allPredecessorsFinal = false;
			}
		}

		allPredecessorsFinal = allPredecessorsFinal && allPredecessorsCreated;
		return allPredecessorsFinal;
	}

	protected void completeJoin() {
		for (IImperativeNodeInstance predecessor : predecessorInstances) {
			Assert.isTrue(predecessor.isInFinalState());
		}

		if (!LAUNCHED.equals(getState())) {
			requestActivation();
			testAndSet(INodeInstanceState.LAUNCHED);
		}
		testAndSet(INodeInstanceState.COMPLETED);
		log(EventType.COMPLETE);

		ensureSuccessorsCreated();
		for (IImperativeNodeInstance successor : successorInstances) {
			successor.requestActivation();
		}

		return;
	}

	protected abstract boolean populateSkipOnPartiallyCreatedPredecessors();

	protected void propagateSkip(boolean propagate) {
		if (!propagate) {
			return;
		}

		ensureSuccessorsCreated();
		for (IImperativeNodeInstance successor : successorInstances) {
			successor.skip(propagate);
		}
	}

	@Override
	public void skip(boolean propagate) {
		if (node.getPredecessors().size() != predecessorInstances.size()) {
			if (populateSkipOnPartiallyCreatedPredecessors()) {
				propagateSkip(propagate);
			}

			return;
		}

		boolean allSkipped = node.getPredecessors().size() == predecessorInstances.size();
		for (IImperativeNodeInstance predecessor : predecessorInstances) {
			if (!predecessor.getState().equals(INodeInstanceState.SKIPPED)) {
				allSkipped = false;
			}
			if (!predecessor.isInFinalState() || predecessor.getState().equals(COMPLETED)) {
				// then at least one predecessor is going to be completed
				return;
			}
		}

		if (!allSkipped) {
			completeJoin();
			return;
		}

		super.skip(propagate);
		propagateSkip(propagate);
	}

	public void stateChanged(IImperativeNodeInstance instance) {
		if (getState().equals(instance.getState())) {
			return; // ignore redundant calls
		}

		if (instance.getState().equals(INodeInstanceState.COMPLETED)) {
			testAndSet(INodeInstanceState.COMPLETED);
		}
		if (instance.getState().equals(INodeInstanceState.LAUNCHED)) {
			testAndSet(INodeInstanceState.LAUNCHED);
		}
		if (instance.getState().equals(INodeInstanceState.ACTIVATED)) {
			if (getState().equals(LAUNCHED)) {
				testAndSet(ACTIVATED);
			}
			for (IImperativeNodeInstance predecessor : getPredecessors()) {
				if (!COMPLETED.equals(predecessor.getState()) && !ACTIVATED.equals(predecessor.getState())) {
					predecessor.requestActivation();
				}
			}
		}
	}

	@Override
	protected void testAndSet(INodeInstanceState state) {
		super.testAndSet(state);

		if (COMPLETED.equals(state)) {
			for (IImperativeNodeInstance successor : successorInstances) {
				successor.removeNodeInstanceChangeListener(this);
			}

			processInstance.getNodeRegistry().removeCachedInstance(node);
		}
	}
}
