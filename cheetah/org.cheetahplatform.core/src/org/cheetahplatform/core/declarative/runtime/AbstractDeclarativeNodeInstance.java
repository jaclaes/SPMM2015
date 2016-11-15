package org.cheetahplatform.core.declarative.runtime;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.declarative.modeling.IDeclarativeNode;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.shared.CheetahConstants;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.06.2009
 */
public abstract class AbstractDeclarativeNodeInstance extends IdentifiableObject implements IDeclarativeNodeInstance {
	private static final long serialVersionUID = -8201541731442382722L;

	protected final IDeclarativeNode node;
	protected DeclarativeProcessInstance instance;
	protected INodeInstanceState state;

	protected AbstractDeclarativeNodeInstance(DeclarativeProcessInstance instance, IDeclarativeNode node) {
		this.instance = instance;
		this.node = node;
		this.state = INodeInstanceState.CREATED;

		String nodeId = String.valueOf(((IIdentifiableObject) node).getCheetahId());
		instance.log(EventType.CREATE, this, CheetahConstants.NODE, nodeId, CheetahConstants.PROCESS_INSTANCE, String.valueOf(instance
				.getCheetahId()));
	}

	public void complete() {
		testAndSet(INodeInstanceState.COMPLETED);
		log(EventType.COMPLETE);
	}

	public INode getNode() {
		return node;
	}

	/**
	 * @return the instance
	 */
	public DeclarativeProcessInstance getProcessInstance() {
		return instance;
	}

	public INodeInstanceState getState() {
		return state;
	}

	public boolean isInFinalState() {
		return state.isFinal();
	}

	public void launch() {
		testAndSet(INodeInstanceState.LAUNCHED);
		log(EventType.LAUNCH);
	}

	protected void log(EventType type) {
		instance.log(type, this);
	}

	public void requestActivation() {
		testAndSet(INodeInstanceState.ACTIVATED);
		log(EventType.ACTIVATE);
	}

	public void skip(boolean propagate) {
		testAndSet(INodeInstanceState.SKIPPED);
		log(EventType.SKIP);
	}

	protected void testAndSet(INodeInstanceState newState) {
		String message = "Tried to change state of " + getNode().getName() + " from " + this.state.getName() + " to " + newState.getName();
		Assert.isTrue(this.state.isValidTransition(newState), message);
		this.state = newState;
	}
}
