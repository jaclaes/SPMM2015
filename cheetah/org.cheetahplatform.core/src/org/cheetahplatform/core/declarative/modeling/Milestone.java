package org.cheetahplatform.core.declarative.modeling;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.NamedIdentifiableObject;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;

/**
 * A class representing a milestone of the business process.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.06.2009
 */
public class Milestone extends NamedIdentifiableObject implements IDeclarativeNode {

	private static class MilestoneInformation {
		private boolean fixed;

		public MilestoneInformation(boolean fixed) {
			this.fixed = fixed;
		}

		/**
		 * Return the fixed.
		 * 
		 * @return the fixed
		 */
		public boolean isFixed() {
			return fixed;
		}
	}

	private static final long serialVersionUID = -1775333782641135652L;

	private Duration durationSinceStart;
	private Map<DeclarativeActivity, MilestoneInformation> activities;

	protected Milestone() {
		// hibernate
	}

	/**
	 * Creates a new milestone
	 * 
	 * @param name
	 *            the name
	 * @param durationSinceStart
	 *            the duration since the start of the {@link ProcessSchema}
	 */
	public Milestone(String name, Duration durationSinceStart) {
		super(name);
		this.durationSinceStart = durationSinceStart;
		this.activities = new HashMap<DeclarativeActivity, MilestoneInformation>();
	}

	public void addActivity(DeclarativeActivity activity) {
		addActivity(activity, false);
	}

	/**
	 * Add a new activity to this milestone.
	 * 
	 * @param activity
	 *            the activity
	 * @param fixed
	 *            <code>true</code> if the activity cannot be moved to another milestone
	 */
	public void addActivity(DeclarativeActivity activity, boolean fixed) {
		MilestoneInformation info = activities.get(activity);
		if (info == null) {
			info = new MilestoneInformation(fixed);
		}
		activities.put(activity, info);
	}

	/**
	 * Determine whether this milestone prevents the moving of an activity to another milestone, e.g., because the activity is bound to this
	 * milestone.
	 * 
	 * @param activity
	 *            the activity
	 * @return <code>true</code> if the given activity can be moved to any other milestone, <code>false</code> otherwise
	 */
	public boolean allowsMoving(DeclarativeActivity activity) {
		MilestoneInformation info = activities.get(activity);
		if (info == null) {
			return true;
		}

		return !info.isFixed();
	}

	/**
	 * Return the activities.
	 * 
	 * @return the activities
	 */
	public Set<DeclarativeActivity> getActivities() {
		return activities.keySet();
	}

	/**
	 * Returns the durationSinceStart.
	 * 
	 * @return the durationSinceStart
	 */
	public Duration getDurationSinceStart() {
		return durationSinceStart;
	}

	public String getType() {
		return IDeclarativeNode.TYPE_MILESTONE;
	}

	public INodeInstance instantiate(IProcessInstance processInstance) {
		MilestoneInstance instance = new MilestoneInstance((DeclarativeProcessInstance) processInstance, this);
		((DeclarativeProcessInstance) processInstance).addMilestone(instance);
		return instance;
	}
}
