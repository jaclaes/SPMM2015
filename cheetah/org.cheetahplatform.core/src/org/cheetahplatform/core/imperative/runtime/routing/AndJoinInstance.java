/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_AND_JOIN;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_XOR_JOIN;

import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class AndJoinInstance extends AbstractJoinInstance {

	private static final long serialVersionUID = 9199694839748911094L;

	public AndJoinInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	public void accept(INodeInstanceVisitor visitor) {
		if (!visitor.hasVisited(predecessorInstances)) {
			return;
		}

		visitor.visitAndJoin(this);
		visitSuccessors(visitor);
	}

	@Override
	protected boolean populateSkipOnPartiallyCreatedPredecessors() {
		return false;
	}

	@Override
	public void requestActivation() {
		if (predecessorInstances.size() != node.getPredecessors().size()) {
			return;
		}

		boolean predecessorIsLoop = false;
		for (IImperativeNodeInstance predecessor : predecessorInstances) {
			if (predecessor.getNode().getType().equals(IImperativeNode.TYPE_LOOP_END)) {
				predecessorIsLoop = true;
			}

			boolean predecessorNotCompleted = !predecessor.getState().equals(INodeInstanceState.COMPLETED);
			if (predecessorNotCompleted) {
				String predecessorType = predecessor.getNode().getType();
				if (TYPE_AND_JOIN.equals(predecessorType) || TYPE_XOR_JOIN.equals(predecessorType)) {
					predecessorIsLoop = true;
				} else {
					return;
				}
			}
		}

		super.requestActivation();
		if (!predecessorIsLoop) {
			testAndSet(INodeInstanceState.LAUNCHED);
			log(EventType.LAUNCH);
			testAndSet(INodeInstanceState.COMPLETED);
			log(EventType.COMPLETE);
		}

		ensureSuccessorsCreated();

		for (IImperativeNodeInstance successor : successorInstances) {
			successor.requestActivation();
			successor.addNodeInstanceChangeListener(this);
		}
	}

	@Override
	public void skip(boolean propagate) {
		super.skip(propagate);

		for (INodeInstance successor : successorInstances) {
			successor.skip(propagate);
		}
	}

}
