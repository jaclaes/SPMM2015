/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.declarative.modeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class DeclarativeProcessSchema extends ProcessSchema {

	private static final long serialVersionUID = 1522050764674922553L;
	private final Set<IDeclarativeConstraint> constraints;
	private final Set<Milestone> milestones;
	private List<IReminder> reminders;

	public DeclarativeProcessSchema() {
		this("");
	}

	public DeclarativeProcessSchema(String name) {
		super(name);

		constraints = new HashSet<IDeclarativeConstraint>();
		milestones = new HashSet<Milestone>();
		reminders = new ArrayList<IReminder>();
	}

	/**
	 * Adds a constraint to the process schema.
	 * 
	 * @param constraint
	 *            the constraint
	 */
	public void addConstraint(IDeclarativeConstraint constraint) {
		constraints.add(constraint);
	}

	/**
	 * @param milestone
	 */
	public void addMilestone(Milestone milestone) {
		milestones.add(milestone);
	}

	/**
	 * @param reminder
	 */
	public void addReminder(IReminder reminder) {
		reminders.add(reminder);
	}

	/**
	 * Create a new task and add it to the existing ones.
	 * 
	 * @param name
	 *            the name of the task
	 * @return a new task
	 */
	public DeclarativeActivity createActivity(String name) {
		DeclarativeActivity activity = new DeclarativeActivity(name);
		nodes.add(activity);
		return activity;
	}

	public Milestone createMilestone(String name, Duration durationSinceStart) {
		Milestone milestone = new Milestone(name, durationSinceStart);
		milestones.add(milestone);
		return milestone;
	}

	public List<DeclarativeActivity> getActivities(String name) {
		List<DeclarativeActivity> matches = new ArrayList<DeclarativeActivity>();
		for (INode node : nodes) {
			if (node.getName().equals(name)) {
				matches.add((DeclarativeActivity) node);
			}
		}

		return matches;
	}

	/**
	 * Returns the constraints.
	 * 
	 * @return the constraints
	 */
	public Set<IDeclarativeConstraint> getConstraints() {
		return Collections.unmodifiableSet(constraints);
	}

	/**
	 * Returns the milestones.
	 * 
	 * @return the milestones
	 */
	public Set<Milestone> getMilestones() {
		return Collections.unmodifiableSet(milestones);
	}

	public DeclarativeProcessInstance instantiate() {
		return instantiate("");
	}

	@Override
	public DeclarativeProcessInstance instantiate(String name) {
		DeclarativeProcessInstance declarativeProcessInstance = new DeclarativeProcessInstance(this, name);
		for (IReminder reminder : reminders) {
			reminder.instantiate(declarativeProcessInstance);
		}
		for (Milestone milestone : milestones) {
			milestone.instantiate(declarativeProcessInstance);
		}

		return declarativeProcessInstance;
	}

	public void removeActivity(DeclarativeActivity activity) {
		for (IDeclarativeConstraint constraint : constraints) {
			Assert.isTrue(!constraint.getActivities().contains(activity), "Activity is still in use by constraint.");
		}
		for (Milestone milestone : milestones) {
			Assert.isTrue(!milestone.getActivities().contains(activity), "Activity is still in use by milestone.");
		}

		nodes.remove(activity);
	}

	public void removeConstraint(IDeclarativeConstraint constraint) {
		constraints.remove(constraint);
	}

}
