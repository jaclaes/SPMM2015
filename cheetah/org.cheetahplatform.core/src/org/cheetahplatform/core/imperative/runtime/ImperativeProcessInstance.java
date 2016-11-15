/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class ImperativeProcessInstance extends ProcessInstance {
	private class EndNodeCompleteListener implements INodeInstanceStateChangeListener {
		private static final long serialVersionUID = -1826601809158485356L;

		public void stateChanged(IImperativeNodeInstance instance) {
			if (instance.getState().equals(INodeInstanceState.COMPLETED)) {
				fireInstanceTerminated();
			}
		}
	}

	private static final long serialVersionUID = -1822188747966318006L;

	private final INodeInstanceStateChangeListener endNodeListener;

	private final NodeRegistry<IImperativeNodeInstance> registry;

	private IImperativeNodeInstance startInstance;
	private IImperativeNodeInstance endInstance;

	public ImperativeProcessInstance(ImperativeProcessSchema schema) {
		super(schema);
		this.endNodeListener = new EndNodeCompleteListener();
		this.registry = new NodeRegistry<IImperativeNodeInstance>(this);

		start();
	}

	public ImperativeProcessInstance(ImperativeProcessSchema schema, String name) {
		super(schema, name);
		this.endNodeListener = new EndNodeCompleteListener();
		this.registry = new NodeRegistry<IImperativeNodeInstance>(this);

		start();
	}

	/**
	 * Determine whether the process instance can be terminated automatically, i.e., terminating the process instance is the only choice. In
	 * particular, this means that the end instance is the only enabled activity and no other activities are launched.
	 * 
	 * @return <code>true</code> if the instance can be terminated, <code>false</code> otherwise
	 */
	public boolean canTerminateAutomatically() {
		boolean hasEndInstance = endInstance != null;
		if (!hasEndInstance) {
			return false;
		}

		boolean isEndInstanceActivated = endInstance.getState().equals(ACTIVATED);
		boolean endInstanceIsTheOnlyExecutableActivity = getActiveActivities().size() == 1;
		boolean noLaunchedActivities = getLaunchedActivities().size() == 0;

		return isEndInstanceActivated && endInstanceIsTheOnlyExecutableActivity && noLaunchedActivities;
	}

	/**
	 * Determine whether the process instance can be terminated explicitely, i.e., the end node is activated and no other activities are
	 * launched.
	 * 
	 * @return <code>true</code> if the instance can be terminated explicitely
	 */
	public boolean canTerminateExplicitely() {
		List<INodeInstance> activeActivities = getActiveActivities();
		for (INodeInstance activity : activeActivities) {
			if (activity.getNode().getType().equals(IImperativeNode.TYPE_END_NODE)) {
				return getLaunchedActivities().size() == 0;
			}
		}

		return false;
	}

	/**
	 * Returns all activities which are currently active, i.e., can be selected by the user.
	 * 
	 * @return all active activities
	 */
	public List<INodeInstance> getActiveActivities() {
		return getActivities(INodeInstanceState.ACTIVATED);
	}

	public List<LateBindingBoxInstance> getActiveLateBindingBoxes() {
		ActiveLateBindingBoxVisitor visitor = new ActiveLateBindingBoxVisitor();
		startInstance.accept(visitor);
		return visitor.getInstances();
	}

	public List<LateModelingBoxInstance> getActiveLateModelingBoxes() {
		ActiveLateModelingBoxesVisitor visitor = new ActiveLateModelingBoxesVisitor();
		startInstance.accept(visitor);
		return visitor.getInstances();
	}

	private List<INodeInstance> getActivities(INodeInstanceState state) {
		if (startInstance == null) {
			return Collections.emptyList();
		}

		ActivityInstanceStateVisitor visitor = new ActivityInstanceStateVisitor(state);
		startInstance.accept(visitor);

		return visitor.getMatchedActivities();
	}

	public List<IImperativeNodeInstance> getAllActivites() {
		Assert.isTrue(startInstance != null);
		ActivityInstanceVisitor visitor = new ActivityInstanceVisitor();
		startInstance.accept(visitor);

		return visitor.getActivites();
	}

	public List<INodeInstance> getCompletedActivities() {
		return getActivities(INodeInstanceState.COMPLETED);
	}

	/**
	 * @return the endInstance
	 */
	public IImperativeNodeInstance getEndInstance() {
		return endInstance;
	}

	public List<INodeInstance> getLaunchedActivities() {
		return getActivities(INodeInstanceState.LAUNCHED);
	}

	@Override
	public INodeInstance getNodeInstance(long id) {
		AbstractNodeInstanceVisitor visitor = new AbstractNodeInstanceVisitor();
		startInstance.accept(visitor);

		Set<IImperativeNodeInstance> all = visitor.getVisitedInstances();
		for (IImperativeNodeInstance instance : all) {
			if (instance.getCheetahId() == id) {
				return instance;
			}
		}

		return null;
	}

	public NodeRegistry<IImperativeNodeInstance> getNodeRegistry() {
		return registry;
	}

	@Override
	public ImperativeProcessSchema getSchema() {
		return (ImperativeProcessSchema) super.getSchema();
	}

	/**
	 * Returns the startInstance.
	 * 
	 * @return the startInstance
	 */
	public IImperativeNodeInstance getStartInstance() {
		return startInstance;
	}

	/**
	 * Check if the instance has been terminated.
	 * 
	 * @return <code>true</code> if the instance is terminated, <code>false</code> otherwise
	 */
	public boolean isTerminated() {
		return endInstance != null && endInstance.getState().equals(COMPLETED);
	}

	/**
	 * Request the termination of the instance, i.e., terminate it if possible.
	 */
	public void requestTermination() {
		if (canTerminateAutomatically()) {
			endInstance.launch();
		}
	}

	/**
	 * Sets the endInstance.
	 * 
	 * @param endInstance
	 *            the endInstance to set
	 */
	void setEndInstance(IImperativeNodeInstance endInstance) {
		if (this.endInstance != null) {
			this.endInstance.removeNodeInstanceChangeListener(endNodeListener);
		}

		this.endInstance = endInstance;
		this.endInstance.addNodeInstanceChangeListener(endNodeListener);
	}

	/**
	 * Instantiates the start node of the process and triggers the further execution.
	 */
	private void start() {
		startInstance = registry.getInstance(((ImperativeProcessSchema) schema).getStart());
		startInstance.requestActivation();
	}

	/**
	 * Uses the given visitor to visit all nodes of the process schema.
	 * 
	 * @param visitor
	 *            the visitor
	 */
	public void visit(INodeInstanceVisitor visitor) {
		startInstance.accept(visitor);
	}
}
