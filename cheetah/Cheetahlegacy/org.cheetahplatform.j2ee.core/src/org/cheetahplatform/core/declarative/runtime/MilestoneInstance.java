package org.cheetahplatform.core.declarative.runtime;

import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.Milestone;

/**
 * A class representing the run-time equivalent of a {@link Milestone}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.06.2009
 */
public class MilestoneInstance extends AbstractDeclarativeNodeInstance {
	private static final long serialVersionUID = -829889833793175243L;

	private Duration actualDurationSinceStart;
	private final Set<DeclarativeActivityInstance> activities;
	private boolean inFuture;

	/**
	 * Creates a new {@link MilestoneInstance}
	 * 
	 * @param milestone
	 *            the {@link Milestone}
	 */
	public MilestoneInstance(DeclarativeProcessInstance processInstance, Milestone milestone) {
		super(processInstance, milestone);

		Assert.isNotNull(processInstance);
		Assert.isNotNull(milestone);

		this.actualDurationSinceStart = milestone.getDurationSinceStart();
		this.activities = new HashSet<DeclarativeActivityInstance>();
		this.inFuture = true;
	}

	public void addActivity(DeclarativeActivityInstance activity) {
		activities.add(activity);
	}

	public void cancel() {
		throw new UnsupportedOperationException("Operation not supported");
	}

	public boolean containsActivity(DeclarativeActivityInstance instance) {
		return activities.contains(instance);
	}

	/**
	 * Return the activities.
	 * 
	 * @return the activities
	 */
	public Set<DeclarativeActivityInstance> getActivities() {
		return activities;
	}

	/**
	 * Returns the actualDurationSinceStart.
	 * 
	 * @return the actualDurationSinceStart
	 */
	public Duration getActualDurationSinceStart() {
		return actualDurationSinceStart;
	}

	public DateTime getEndTime() {
		return getTime();
	}

	public DateTime getStartTime() {
		return getTime();
	}

	private DateTime getTime() {
		DateTime time = new DateTime(instance.getStartTime());
		if (inFuture) {
			time.plus(getActualDurationSinceStart());
		} else {
			time.minus(getActualDurationSinceStart());
		}
		return time;
	}

	/**
	 * @param milestoneToCheck
	 * @return
	 */
	public boolean isInstanceForMilestone(Milestone milestoneToCheck) {
		return node.equals(milestoneToCheck);
	}

	public void removeActivity(IDeclarativeNodeInstance activity) {
		activities.remove(activity);
	}

	/**
	 * Sets the actualDurationSinceStart.
	 * 
	 * @param actualDurationSinceStart
	 *            the actualDurationSinceStart to set
	 */
	public void setActualDurationSinceStart(Duration actualDurationSinceStart) {
		this.actualDurationSinceStart = actualDurationSinceStart;
	}

	public void setEndTime(DateTime endTime) {
		setTime(endTime);
	}

	public void setStartTime(DateTime startTime) {
		setTime(startTime);
	}

	private void setTime(DateTime time) {
		inFuture = instance.getStartTime().compareTo(time) <= 0;
		Duration difference = instance.getStartTime().getDifference(time);
		this.actualDurationSinceStart = difference;
	}

	@Override
	public void skip(boolean propagate) {
		throw new UnsupportedOperationException("Operation not supported");
	}
}
