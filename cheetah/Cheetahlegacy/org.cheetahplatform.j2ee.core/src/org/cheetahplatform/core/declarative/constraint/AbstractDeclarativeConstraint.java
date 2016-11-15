package org.cheetahplatform.core.declarative.constraint;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public abstract class AbstractDeclarativeConstraint extends IdentifiableObject implements IDeclarativeConstraint {

	private static final long serialVersionUID = -8888575422667316289L;

	/**
	 * Same as for {@link #getActivities()} - convenience method as no new list must be created.
	 * 
	 * @param activities
	 *            a list to collect the activities
	 */
	protected abstract void addActivities(List<DeclarativeActivity> activities);

	/**
	 * Same as for {@link #getEndActivities()} - convenience method as no new list must be created.
	 * 
	 * @param activities
	 *            a list to collect the activities
	 */
	protected abstract void addEndActivities(List<DeclarativeActivity> activities);

	/**
	 * Same as for {@link #getStartActivities()} - convenience method as no new list must be created.
	 * 
	 * @param activities
	 *            a list to collect the activities
	 */
	protected abstract void addStartActivities(List<DeclarativeActivity> activities);

	protected String concatenateActivities(List<DeclarativeActivity> activities) {
		StringBuilder listing = new StringBuilder();

		boolean first = true;
		for (DeclarativeActivity activity : activities) {
			if (!first) {
				listing.append(", ");
			}

			first = false;
			listing.append(activity.getName());
		}

		return listing.toString();
	}

	public List<DeclarativeActivity> getActivities() {
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();
		addActivities(activities);
		return activities;
	}

	public List<DeclarativeActivity> getEndActivities() {
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();
		addEndActivities(activities);
		return activities;
	}

	/**
	 * Determine when the last activity of given type has been completed for a process instance.
	 * 
	 * @param instance
	 *            the instance
	 * @param activity
	 *            the activity type to be checked
	 * @return the date when the last instance of the given type has been completed, <code>null</code> if no activity instance of the given
	 *         activity type has been ever completed for the given process instance
	 */
	protected Date getLastCompletionDate(DeclarativeProcessInstance instance, DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> instances = instance.getNodeInstances(activity);
		Date lastCompletion = null;

		for (IDeclarativeNodeInstance current : instances) {
			DateTime currentCompletion = current.getEndTime();
			if (lastCompletion == null || (currentCompletion != null && currentCompletion.compareTo(lastCompletion) > 0)) {
				lastCompletion = currentCompletion;
			}
		}

		return lastCompletion;
	}

	/**
	 * Determine the last completion date of a given list of activities.
	 * 
	 * @param instance
	 *            the process instance to be checked
	 * @param activities
	 *            the activities to be checked
	 * @return the last completion date, <code>null</code> if none of the given activities has been completed yet
	 */
	protected Date getLastCompletionDate(DeclarativeProcessInstance instance, List<DeclarativeActivity> activities) {
		Date lastCompletionDate = null;

		for (DeclarativeActivity activity : activities) {
			Date date = getLastCompletionDate(instance, activity);
			if (lastCompletionDate == null || (date != null && lastCompletionDate.compareTo(date) < 0)) {
				lastCompletionDate = date;
			}
		}

		return lastCompletionDate;
	}

	public List<DeclarativeActivity> getStartActivities() {
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();
		addStartActivities(activities);
		return activities;
	}

	/**
	 * Determine whether the given activity has been completed in the given instance.
	 * 
	 * @param instance
	 *            the process instance
	 * @param activity
	 *            the activity
	 * @return <code>true</code> if at least one activity of the given type has been completed in the given process instance,
	 *         <code>false</code> otherwise
	 */
	protected boolean hasBeenCompleted(DeclarativeProcessInstance instance, DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> instances = instance.getNodeInstances(activity);
		for (IDeclarativeNodeInstance current : instances) {
			if (INodeInstanceState.COMPLETED.equals(current.getState())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return getDescription();
	}
}
