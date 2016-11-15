package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;
import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class MultiExclusiveChoiceConstraint extends MultiActivityConstraint {

	private static final long serialVersionUID = -6200643543108354682L;

	private int amount;

	public MultiExclusiveChoiceConstraint() {
		super();
	}

	public MultiExclusiveChoiceConstraint(List<DeclarativeActivity> activities, int amount) {
		this.amount = amount;

		setStartActivities(activities);
	}

	@Override
	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		int completedCount = 0;
		for (DeclarativeActivity activity : getActivities()) {
			if (hasBeenCompleted(processInstance, activity)) {
				completedCount++;
			}
		}

		return completedCount == amount;
	}

	@Override
	public String getDescription() {
		String description = "From activitiies {0}, {1} activities have to be executed";
		String activities = concatenateActivities(getActivities());
		String message = MessageFormat.format(description, activities, amount);

		return message;
	}

	@Override
	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!getActivities().contains(activity)) {
			return true;
		}

		int startedOrCompleted = 0;
		for (DeclarativeActivity current : getActivities()) {
			if (processInstance.getUncanceledCount(current) > 0) {
				if (activity.equals(current)) {
					return true;// we already started this activity --> do not further restrict it
				}

				startedOrCompleted++;
				continue;
			}
		}

		return startedOrCompleted < amount;
	}

}
