package org.cheetahplatform.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.modeling.ProcessInstance;

public class ProcessInstanceHandle extends AbstractHandle {
	private final List<ActivityInstanceHandle> activeActivities;

	public ProcessInstanceHandle(ProcessInstance instance) {
		this(instance.getName(), instance.getCheetahId());
	}

	public ProcessInstanceHandle(String name, long id) {
		super(id, name);

		this.activeActivities = new ArrayList<ActivityInstanceHandle>();
	}

	public void addActiveActivities(List<ActivityInstanceHandle> activities) {
		activeActivities.addAll(activities);
	}

	public void addActiveActivity(ActivityInstanceHandle activityHandle) {
		activeActivities.add(activityHandle);
	}

	/**
	 * @param activity
	 * @return
	 */
	public boolean contains(ActivityInstanceHandle toFind) {
		for (ActivityInstanceHandle activity : getActiveActivities()) {
			if (activity.equals(toFind))
				return true;
		}
		return false;
	}

	/**
	 * Returns the activeActivities.
	 * 
	 * @return the activeActivities
	 */
	public List<ActivityInstanceHandle> getActiveActivities() {
		return Collections.unmodifiableList(activeActivities);
	}

	public boolean hasActiveActivities() {
		return !activeActivities.isEmpty();
	}

}
