/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime.routing;

import static org.cheetahplatform.core.common.modeling.INode.TYPE_ACTIVITY;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_LATE_BINDING_NODE;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_LATE_MODELING_NODE;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_LOOP_END;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.CREATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceStateChangeListener;
import org.cheetahplatform.core.imperative.runtime.INodeInstanceVisitor;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class LoopEndInstance extends AbstractRoutingInstance implements INodeInstanceStateChangeListener {

	private class LoopEndInstanceLogListener implements ILogListener {
		private boolean recursive;

		public void log(AuditTrailEntry entry) {
			if (recursive) {
				return;
			}

			recursive = true;
			logEventOccurred(entry);
			recursive = false;
		}

	}

	private static final long serialVersionUID = 1220838865635587730L;
	private LoopEndInstanceLogListener logListener;

	public LoopEndInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);

		logListener = new LoopEndInstanceLogListener();
		processInstance.addLogListener(logListener, false);
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitLoopEnd(this);
		visitSuccessors(visitor);
	}

	private void collectSuccessors(List<INodeInstance> successors, IImperativeNodeInstance root) {
		successors.add(root);

		for (INodeInstance successor : root.getSuccessors()) {
			collectSuccessors(successors, (IImperativeNodeInstance) successor);
		}
	}

	public void logEventOccurred(AuditTrailEntry entry) {
		if (entry.getEventType().equals(EventType.INSTANTIATE.name()) || entry.getEventType().equals(EventType.CREATE.name())) {
			return;
		}

		long id = Long.parseLong(entry.getWorkflowModelElement());
		INodeInstance nodeInstance = processInstance.getNodeInstance(id);
		String type = nodeInstance.getNode().getType();

		// remove the listener if no changes are possible anymore (i.e., some successor activity has been completed)
		List<INodeInstance> allSuccessors = new ArrayList<INodeInstance>();
		collectSuccessors(allSuccessors, this);
		for (INodeInstance successor : allSuccessors) {
			String currentType = successor.getNode().getType();
			if (currentType.equals(TYPE_LATE_BINDING_NODE) || currentType.equals(TYPE_LATE_MODELING_NODE)
					|| currentType.equals(TYPE_ACTIVITY)) {
				if (successor.getState().equals(INodeInstanceState.COMPLETED)) {
					processInstance.removeLogListener(logListener);
					for (IImperativeNodeInstance directSuccessor : successorInstances) {
						directSuccessor.removeNodeInstanceChangeListener(this);
					}
					processInstance.getNodeRegistry().removeCachedInstance(node);
					unlinkFromNonCompletedSuccessors();

					return;
				}
			}
		}

		if (nodeInstance.getNode().equals(node) && !nodeInstance.equals(this)) {
			processInstance.removeLogListener(logListener);
		} else if (type.equals(TYPE_LATE_BINDING_NODE) || type.equals(TYPE_LATE_MODELING_NODE) || type.equals(TYPE_ACTIVITY)) {
			if (successorInstances.size() != 2) {
				return;
			}

			List<INodeInstance> successors = new ArrayList<INodeInstance>();
			collectSuccessors(successors, this);
			if (!successors.contains(nodeInstance)) {
				return; // activity not relevant for the loop end
			}

			List<INodeInstance> successorsOfSuccessor0 = new ArrayList<INodeInstance>();
			collectSuccessors(successorsOfSuccessor0, successorInstances.get(0));
			List<INodeInstance> successorsOfSuccessor1 = new ArrayList<INodeInstance>();
			collectSuccessors(successorsOfSuccessor1, successorInstances.get(1));

			if (entry.getEventType().equals(EventType.LAUNCH.name())) {
				if (successorsOfSuccessor0.contains(nodeInstance)) {
					successorInstances.get(1).skip(false);
				} else {
					successorInstances.get(0).skip(false);
				}
			} else if (entry.getEventType().equals(EventType.SKIP.name()) || entry.getEventType().equals(EventType.CANCEL.name())) {
				IImperativeNodeInstance toActivate = null;

				if (successorsOfSuccessor0.contains(nodeInstance)) {
					toActivate = successorInstances.get(1);
				} else {
					toActivate = successorInstances.get(0);
				}

				if (!toActivate.getState().equals(INodeInstanceState.LAUNCHED)) {
					toActivate.requestActivation();
				}
			}
		}
	}

	@Override
	public void requestActivation() {
		if (!getState().equals(INodeInstanceState.COMPLETED)) {
			super.requestActivation();
			processInstance.getNodeRegistry().removeCachedInstance(getNode());

			testAndSet(INodeInstanceState.LAUNCHED);
			log(EventType.LAUNCH);
			testAndSet(INodeInstanceState.COMPLETED);
			log(EventType.COMPLETE);

			for (INode successor : node.getSuccessors()) {
				IImperativeNodeInstance instance = processInstance.getNodeRegistry().getInstance(successor);
				successorInstances.add(instance);
				instance.addPredecessor(this);
				instance.requestActivation();
				instance.addNodeInstanceChangeListener(this);
			}
		} else {
			// propagate the activation if required
			for (INodeInstance successor : successorInstances) {
				boolean isCompletedLoop = successor.getNode().getType().equals(TYPE_LOOP_END) && successor.getState().equals(COMPLETED);
				if (successor.getState().equals(CREATED) || successor.getState().equals(SKIPPED) || isCompletedLoop) {
					successor.requestActivation();
				}
			}
		}
	}

	@Override
	public void skip(boolean propagate) {
		if (getState().equals(INodeInstanceState.COMPLETED)) {
			return;
		}

		super.skip(propagate);

		if (!propagate) {
			return;
		}

		for (INode successorNode : node.getSuccessors()) {
			if (successorNode.getType().equals(IImperativeNode.TYPE_LOOP_START)) {
				continue;
			}

			// do not propogate skipping if the successor has been launched (and thus triggered the skip call)
			IImperativeNodeInstance instance = processInstance.getNodeRegistry().getInstance(successorNode);
			if (INodeInstanceState.LAUNCHED.equals(instance.getState())) {
				continue;
			}

			successorInstances.add(instance);
			instance.addPredecessor(this);
			instance.skip(true);
		}
	}

	public void stateChanged(IImperativeNodeInstance instance) {
		List<INodeInstance> allSuccessors = new ArrayList<INodeInstance>();
		collectSuccessors(allSuccessors, this);
		for (INodeInstance successor : allSuccessors) {
			if (successor.getNode().equals(node) && !successor.equals(this)) {
				processInstance.removeLogListener(logListener);
				return;
			}
		}

		INodeInstanceState state = instance.getState();
		if (state.equals(INodeInstanceState.SKIPPED)) {
			ensureSuccessorsCreated();

			for (INodeInstance successor : successorInstances) {
				if (((IImperativeNode) successor.getNode()).getType().equals(IImperativeNode.TYPE_LOOP_START) || successor.equals(instance)) {
					continue;
				}

				if (!successor.getState().equals(INodeInstanceState.LAUNCHED)) {
					successor.requestActivation();
				}
			}
		} else if (state.equals(INodeInstanceState.LAUNCHED)) {
			for (INodeInstance successor : successorInstances) {
				if (successor.equals(instance)) {
					continue;
				}

				successor.skip(false);
			}
		}
	}

	private void unlinkFromNonCompletedSuccessors() {
		Iterator<IImperativeNodeInstance> iterator = successorInstances.iterator();
		while (iterator.hasNext()) {
			IImperativeNodeInstance successor = iterator.next();

			if (!successor.getState().equals(INodeInstanceState.COMPLETED)) {
				successor.removePredecessor(this);
				iterator.remove();
			}
		}

	}
}
