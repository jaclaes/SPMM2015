/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class XorJoinInstance extends AbstractJoinInstance {

	private static final long serialVersionUID = -3320442126459455950L;

	public XorJoinInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	public void accept(INodeInstanceVisitor visitor) {
		if (!visitor.hasVisited(predecessorInstances) || visitor.hasVisited(this)) {
			return;
		}

		visitor.visitXorJoin(this);
		visitSuccessors(visitor);
	}

	@Override
	protected boolean populateSkipOnPartiallyCreatedPredecessors() {
		return true;
	}

	@Override
	protected void propagateSkip(boolean propagate) {
		boolean atLeastOneComplete = false;
		for (INodeInstance predecessor : predecessorInstances) {
			if (COMPLETED.equals(predecessor.getState())) {
				atLeastOneComplete = true;
			}
		}

		if (atLeastOneComplete) {
			completeJoin();
			return;
		}

		if (predecessorInstances.size() != node.getPredecessors().size()) {
			// happens when a launched successor on some unrelated branch has been selected, this whole branch gets skipped
			if (ACTIVATED.equals(getState())) {
				testAndSet(SKIPPED);
			}

			for (IImperativeNodeInstance successor : successorInstances) {
				successor.skip(false);
			}

			return;
		}

		super.propagateSkip(propagate);
	}

	@Override
	public void requestActivation() {
		boolean allPredecessorsFinal = allPredecessorsInFinalState();
		ensureSuccessorsCreated();
		if (!ACTIVATED.equals(getState()) && !COMPLETED.equals(getState())) {
			super.requestActivation(); // has possibly already been activated by other predecessor
		}

		boolean noPredecessorIsCompletedLoop = true;
		for (INodeInstance predecessor : predecessorInstances) {
			if (predecessor.getNode().getType().equals(IImperativeNode.TYPE_LOOP_END) && predecessor.getState().equals(COMPLETED)) {
				noPredecessorIsCompletedLoop = false;
				break;
			}
		}

		if (allPredecessorsFinal) {
			if (noPredecessorIsCompletedLoop) {
				testAndSet(INodeInstanceState.LAUNCHED);
				log(EventType.LAUNCH);
				testAndSet(INodeInstanceState.COMPLETED);
				log(EventType.COMPLETE);

				processInstance.getNodeRegistry().removeCachedInstance(node);
			}
		}

		for (IImperativeNodeInstance successor : successorInstances) {
			successor.requestActivation();
			if (!allPredecessorsFinal) {
				successor.addNodeInstanceChangeListener(this);
			}
		}
	}

	@Override
	public void skip(boolean propagate) {
		super.skip(propagate);

		if (getState().equals(INodeInstanceState.SKIPPED)) {
			return;
		}

		for (INodeInstance predecessor : predecessorInstances) {
			if (predecessor.getNode().getType().equals(IImperativeNode.TYPE_LOOP_END)) {
				testAndSet(INodeInstanceState.SKIPPED);
				return;
			}
		}
	}

}
