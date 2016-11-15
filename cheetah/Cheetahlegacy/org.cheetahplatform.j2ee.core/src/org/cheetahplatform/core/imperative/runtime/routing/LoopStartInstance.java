/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceStateChangeListener;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class LoopStartInstance extends AbstractRoutingInstance implements INodeInstanceStateChangeListener {

	private static final long serialVersionUID = -3748157585372023419L;

	public LoopStartInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitLoopStart(this);
		visitSuccessors(visitor);
	}

	@Override
	public void addPredecessor(IImperativeNodeInstance predecessor) {
		super.addPredecessor(predecessor);
		processInstance.getNodeRegistry().removeCachedInstance(node);
	}

	@Override
	public void requestActivation() {
		Assert.isTrue(predecessorInstances.size() == 1);

		INodeInstanceState predecessorState = predecessorInstances.get(0).getState();
		if (predecessorState.equals(INodeInstanceState.COMPLETED) || predecessorState.equals(INodeInstanceState.ACTIVATED)) {
			super.requestActivation();
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

		ensureSuccessorsCreated();
		for (IImperativeNodeInstance successor : successorInstances) {
			successor.skip(propagate);
		}
	}

	public void stateChanged(IImperativeNodeInstance instance) {
		INodeInstanceState instanceState = instance.getState();
		if (!this.state.equals(instanceState)) {
			testAndSet(instanceState);
			log(EventType.fromNodeInstanceState(instanceState));
		}
	}
}
