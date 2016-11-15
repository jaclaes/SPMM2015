/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.CREATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.LAUNCHED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class XorSplitInstance extends AbstractSplitInstance {
	private static final long serialVersionUID = 7890404946835066264L;
	private boolean recursive;

	public XorSplitInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitXorSplit(this);
		visitSuccessors(visitor);
	}

	@Override
	public XorSplit getNode() {
		return (XorSplit) super.getNode();
	}

	private boolean skipSuccessors(IImperativeNodeInstance instance) {
		// ignore any events from directly connected joins
		XorSplit split = getNode();
		XorJoin join = split.getJoin();

		for (IImperativeNodeInstance successor : successorInstances) {
			if (successor.equals(instance)) {
				continue;
			}
			if (successor.getNode().equals(join) && successor.getState().isFinal()) {
				continue;
			}

			successor.skip(true);
		}

		return true;
	}

	public void stateChanged(IImperativeNodeInstance instance) {
		if (recursive) {
			return;
		}

		recursive = true;
		INodeInstanceState state = instance.getState();

		if (state.equals(LAUNCHED)) {
			successorLaunched(instance);
		} else if (state.equals(ACTIVATED)) {
			successorActivated(instance);
		} else if (state.equals(INodeInstanceState.COMPLETED)) {
			successorCompleted();
		}

		recursive = false;
	}

	/**
	 * A successor has been activated.
	 * 
	 * @param instance
	 *            the activated successor
	 */
	private void successorActivated(IImperativeNodeInstance instance) {
		for (IImperativeNodeInstance successor : successorInstances) {
			INodeInstanceState successorState = successor.getState();
			Assert.isTrue(successorState.equals(ACTIVATED) || successorState.equals(SKIPPED) || successorState.equals(CREATED),
					successorState.toString());
		}

		for (IImperativeNodeInstance successor : successorInstances) {
			if (successor.equals(instance)) {
				continue;
			}

			successor.requestActivation();
		}

		if (!ACTIVATED.equals(getState())) {
			testAndSet(ACTIVATED);
		}
	}

	/**
	 * A successor node has been completed.
	 * 
	 * @param instance
	 *            the completed node
	 */
	private void successorCompleted() {
		testAndSet(INodeInstanceState.COMPLETED);
		processInstance.getNodeRegistry().removeCachedInstance(node);
	}

	/**
	 * A successor node has been launched.
	 * 
	 * @param instance
	 *            the launched node
	 */
	private void successorLaunched(IImperativeNodeInstance instance) {
		skipSuccessors(instance);

		testAndSet(INodeInstanceState.LAUNCHED);
	}

	@Override
	protected void testAndSet(INodeInstanceState state) {
		super.testAndSet(state);

		if (COMPLETED.equals(state)) {
			for (IImperativeNodeInstance successor : getSuccessors()) {
				successor.removeNodeInstanceChangeListener(this);
			}
		}
	}
}
