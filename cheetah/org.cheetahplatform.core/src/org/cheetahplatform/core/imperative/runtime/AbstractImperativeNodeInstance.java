/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ListenerList;

/**
 * Default implementation of the {@link IImperativeNodeInstance} interface.
 * 
 * @author Jakob Pinggera <br>
 *         Stefan Zugal<br>
 * 
 */
public abstract class AbstractImperativeNodeInstance extends IdentifiableObject implements IImperativeNodeInstance {
	private static final long serialVersionUID = 8453131195267981498L;
	protected ImperativeProcessInstance processInstance;
	protected IImperativeNode node;
	protected INodeInstanceState state;
	protected List<IImperativeNodeInstance> successorInstances;
	protected List<IImperativeNodeInstance> predecessorInstances;
	protected ListenerList stateChangeListeners;

	protected AbstractImperativeNodeInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		this.processInstance = processInstance;
		this.node = node;
		this.successorInstances = new ArrayList<IImperativeNodeInstance>();
		this.predecessorInstances = new ArrayList<IImperativeNodeInstance>();
		this.stateChangeListeners = new ListenerList();
		this.state = INodeInstanceState.CREATED;

		processInstance.log(EventType.CREATE, this, CheetahConstants.NODE, String.valueOf(((IIdentifiableObject) node).getCheetahId()));
	}

	public void addNodeInstanceChangeListener(INodeInstanceStateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

	public void addPredecessor(IImperativeNodeInstance predecessor) {
		ListIterator<IImperativeNodeInstance> listIterator = predecessorInstances.listIterator();
		while (listIterator.hasNext()) {
			IImperativeNodeInstance instance = listIterator.next();
			if (instance.getNode().equals(predecessor.getNode())) {
				listIterator.remove();
				break;
			}
		}

		predecessorInstances.add(predecessor);
	}

	public void cancel() {
		Assert.isTrue(state.equals(INodeInstanceState.LAUNCHED));
		testAndSet(INodeInstanceState.ACTIVATED);
		log(EventType.CANCEL);
	}

	public void complete() {
		testAndSet(INodeInstanceState.COMPLETED);
		processInstance.getNodeRegistry().removeCachedInstance(node);
		log(EventType.COMPLETE);
	}

	/**
	 * Create the successor instances of the node. If the successors have already been created, the method does not do anything.
	 */
	protected void ensureSuccessorsCreated() {
		if (successorInstances.size() == node.getSuccessors().size()) {
			return; // successors already created
		}

		for (INode successor : node.getSuccessors()) {
			IImperativeNodeInstance instance = processInstance.getNodeRegistry().getInstance(successor);
			successorInstances.add(instance);
			instance.addPredecessor(this);
		}

		// avoid any further modifications
		successorInstances = Collections.unmodifiableList(successorInstances);
	}

	public INode getNode() {
		return node;
	}

	public List<IImperativeNodeInstance> getPredecessors() {
		return Collections.unmodifiableList(predecessorInstances);
	}

	public INodeInstanceState getState() {
		return state;
	}

	public List<IImperativeNodeInstance> getSuccessors() {
		return Collections.unmodifiableList(successorInstances);
	}

	public boolean isInFinalState() {
		return state.isFinal();
	}

	public void launch() {
		testAndSet(INodeInstanceState.LAUNCHED);
		log(EventType.LAUNCH);
	}

	protected void log(EventType type) {
		processInstance.log(type, this);
	}

	public void removeNodeInstanceChangeListener(INodeInstanceStateChangeListener listener) {
		stateChangeListeners.remove(listener);
	}

	public void removePredecessor(IImperativeNodeInstance node) {
		Assert.isTrue(predecessorInstances.remove(node));
	}

	public void requestActivation() {
		if (this.state.equals(INodeInstanceState.ACTIVATED)) {
			// ignore calls which enable already activated nodes
			return;
		}

		testAndSet(INodeInstanceState.ACTIVATED);
		log(EventType.ACTIVATE);
	}

	public void skip(boolean propagate) {
		if (this.state.equals(INodeInstanceState.SKIPPED)) {
			// similar to activation, skipping a skipped node does not cause problems
			return;
		}

		testAndSet(INodeInstanceState.SKIPPED);
		processInstance.getNodeRegistry().removeCachedInstance(node);
		log(EventType.SKIP);
	}

	protected void testAndSet(INodeInstanceState state) {
		String message = MessageFormat.format("{0}: Tried to change state of {1} from {2} to {3}", toString(), getNode().getName(),
				this.state.getName(), state.getName());
		Assert.isTrue(this.state.isValidTransition(state), message);
		this.state = state;

		for (Object listener : stateChangeListeners.getListeners()) {
			((INodeInstanceStateChangeListener) listener).stateChanged(this);
		}
	}

	@Override
	public String toString() {
		return getNode() + ": " + getState();
	}

	/**
	 * Visit all successor nodes.
	 * 
	 * @param visitor
	 *            the visitor which should visit all successor nodes
	 */
	protected void visitSuccessors(INodeInstanceVisitor visitor) {
		for (IImperativeNodeInstance successor : successorInstances) {
			successor.accept(visitor);
		}
	}
}
