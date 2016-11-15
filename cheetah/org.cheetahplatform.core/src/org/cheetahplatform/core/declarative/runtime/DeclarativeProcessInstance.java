/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.declarative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.ProcessInstance;
import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class DeclarativeProcessInstance extends ProcessInstance {

	private static final long serialVersionUID = 1173927984818145294L;

	private Map<INode, List<IDeclarativeNodeInstance>> nodeInstances;
	private Set<MilestoneInstance> milestones;
	private DateTime startTime;
	private final List<IReminderInstance> reminders;

	private boolean terminated;

	public DeclarativeProcessInstance(DeclarativeProcessSchema schema) {
		this(schema, "");
	}

	public DeclarativeProcessInstance(DeclarativeProcessSchema schema, String name) {
		super(schema, name);

		this.nodeInstances = new HashMap<INode, List<IDeclarativeNodeInstance>>();
		this.startTime = DateTimeProvider.getDateTimeSource().getCurrentTime(true);
		this.milestones = new HashSet<MilestoneInstance>();
		this.reminders = new ArrayList<IReminderInstance>();
	}

	/**
	 * @param declarativeActivityInstance
	 */
	public void addActivityInstance(IDeclarativeNodeInstance declarativeActivityInstance) {
		INode node = declarativeActivityInstance.getNode();
		List<IDeclarativeNodeInstance> instances = nodeInstances.get(node);
		if (instances == null) {
			instances = new ArrayList<IDeclarativeNodeInstance>();
		}
		instances.add(declarativeActivityInstance);
		nodeInstances.put(node, instances);
	}

	public void addMilestone(MilestoneInstance milestone) {
		milestones.add(milestone);
	}

	/**
	 * @param reminder
	 */
	public void addReminder(IReminderInstance reminder) {
		reminders.add(reminder);
	}

	/**
	 * @return
	 */
	public ConstraintEvaluation canTerminate() {
		if (terminated) {
			return new ConstraintEvaluation(false, null); // already terminated
		}

		Set<IDeclarativeConstraint> constraints = (getSchema()).getConstraints();
		for (IDeclarativeConstraint constraint : constraints) {
			if (!constraint.canTerminate(this))
				return new ConstraintEvaluation(false, constraint);
		}

		return new ConstraintEvaluation(true, null);
	}

	/**
	 * @param milestone
	 * @return
	 */
	public MilestoneInstance findMilestoneInstance(Milestone milestone) {
		for (MilestoneInstance milestoneInstance : getMilestones()) {
			if (milestoneInstance.isInstanceForMilestone(milestone))
				return milestoneInstance;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<DeclarativeActivity> getActiveActivities() {
		if (terminated) {
			return Collections.emptyList();
		}

		List<DeclarativeActivity> executableActivities = new ArrayList<DeclarativeActivity>();
		executableActivities.addAll((Collection<? extends DeclarativeActivity>) getSchema().getNodes());

		for (INode declarativeActivity : getSchema().getNodes()) {
			ConstraintEvaluation result = isExecutable((DeclarativeActivity) declarativeActivity);
			boolean isExecutable = result.getResult();

			if (!isExecutable) {
				executableActivities.remove(declarativeActivity);
			}
		}

		return executableActivities;
	}

	/**
	 * Returns all active reminders.
	 * 
	 * @return all active reminders
	 */
	public List<IReminderInstance> getActiveReminders() {
		List<IReminderInstance> activeReminders = new ArrayList<IReminderInstance>();
		for (IReminderInstance reminder : reminders) {
			if (reminder.isActive(this))
				activeReminders.add(reminder);
		}

		return activeReminders;
	}

	public List<IDeclarativeNodeInstance> getCompletedActivities() {
		List<IDeclarativeNodeInstance> completed = new ArrayList<IDeclarativeNodeInstance>();
		for (INode activity : nodeInstances.keySet()) {
			completed.addAll(getCompletedActivities((DeclarativeActivity) activity));
		}

		return completed;
	}

	public List<IDeclarativeNodeInstance> getCompletedActivities(DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> completed = new ArrayList<IDeclarativeNodeInstance>();

		for (IDeclarativeNodeInstance instance : getNodeInstances(activity)) {
			if (instance.getState().equals(COMPLETED)) {
				completed.add(instance);
			}
		}

		return completed;
	}

	public int getCompletedNodeInstanceCount(DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> candidates = getNodeInstances(activity);
		int completed = 0;
		for (IDeclarativeNodeInstance candidate : candidates) {
			if (candidate.getState().equals(COMPLETED)) {
				completed++;
			}
		}

		return completed;
	}

	public int getCount(DeclarativeActivity activity, INodeInstanceState... states) {
		List<IDeclarativeNodeInstance> instances = getNodeInstances(activity);
		int count = 0;
		Set<INodeInstanceState> toCheck = new HashSet<INodeInstanceState>(Arrays.asList(states));

		for (IDeclarativeNodeInstance instance : instances) {
			if (toCheck.contains(instance.getState())) {
				count++;
			}
		}

		return count;
	}

	public int getCount(INodeInstanceState... states) {
		int count = 0;
		for (INode node : schema.getNodes()) {
			count += getCount((DeclarativeActivity) node, states);
		}

		return count;
	}

	public IDeclarativeNodeInstance getLatestCompletedActivity() {
		List<IDeclarativeNodeInstance> completed = getCompletedActivities();
		if (completed.isEmpty()) {
			return null;
		}

		Collections.sort(completed, new Comparator<IDeclarativeNodeInstance>() {

			public int compare(IDeclarativeNodeInstance node1, IDeclarativeNodeInstance node2) {
				return node1.getEndTime().compareTo(node2.getEndTime());
			}
		});

		return completed.get(completed.size() - 1);
	}

	/**
	 * Return the milestones.
	 * 
	 * @return the milestones
	 */
	public Set<MilestoneInstance> getMilestones() {
		return Collections.unmodifiableSet(milestones);
	}

	@Override
	public INodeInstance getNodeInstance(long id) {
		for (List<IDeclarativeNodeInstance> nodes : nodeInstances.values()) {
			for (IDeclarativeNodeInstance node : nodes) {
				if (node.getCheetahId() == id) {
					return node;
				}
			}
		}

		return null;
	}

	public List<IDeclarativeNodeInstance> getNodeInstances() {
		List<IDeclarativeNodeInstance> instances = new ArrayList<IDeclarativeNodeInstance>();
		for (List<IDeclarativeNodeInstance> toAdd : nodeInstances.values()) {
			instances.addAll(toAdd);
		}

		return instances;
	}

	public List<IDeclarativeNodeInstance> getNodeInstances(DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> instances = nodeInstances.get(activity);
		if (instances == null)
			return Collections.emptyList();

		return Collections.unmodifiableList(instances);
	}

	/**
	 * Returns the reminders.
	 * 
	 * @return the reminders
	 */
	public List<IReminderInstance> getReminders() {
		return Collections.unmodifiableList(reminders);
	}

	@Override
	public DeclarativeProcessSchema getSchema() {
		return (DeclarativeProcessSchema) super.getSchema();
	}

	/**
	 * Returns the startTime.
	 * 
	 * @return the startTime
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	public int getUncanceledCount(DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> instances = getNodeInstances(activity);
		int count = 0;

		for (IDeclarativeNodeInstance instance : instances) {
			if (!instance.getState().equals(SKIPPED)) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Determine whether the process instance contains an instantiation of the given activity and the state is not canceled.
	 * 
	 * @param activity
	 *            the activity
	 * @return <code>true</code> if there is an instance, <code>false</code> if not
	 */
	public boolean hasUncanceledInstanceOf(DeclarativeActivity activity) {
		return getUncanceledCount(activity) > 0;
	}

	public ConstraintEvaluation isExecutable(DeclarativeActivity declarativeActivity) {
		for (IDeclarativeConstraint constraint : getSchema().getConstraints()) {
			if (!constraint.isExecutable(declarativeActivity, this)) {
				return new ConstraintEvaluation(false, constraint);
			}
		}

		return new ConstraintEvaluation(true, null);
	}

	/**
	 * Set the startTime.
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public void terminate() {
		Assert.isTrue(canTerminate().getResult(), "Instance cannot be terminated.");

		log(EventType.TERMINATE, this);
		terminated = true;
	}

}
