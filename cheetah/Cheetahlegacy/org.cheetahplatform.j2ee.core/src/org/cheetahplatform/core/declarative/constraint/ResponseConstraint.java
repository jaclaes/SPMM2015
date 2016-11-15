package org.cheetahplatform.core.declarative.constraint;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;

public class ResponseConstraint extends AbstractDeclarativeTerminationConstraintWithTwoActivities {

	private static final long serialVersionUID = -3757766425923491434L;

	protected ResponseConstraint() {
		super(); // hibernate
	}

	public ResponseConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		List<IDeclarativeNodeInstance> instances1 = sortByCompletionDate(processInstance, activity1);
		if (instances1.isEmpty())
			return true;

		List<IDeclarativeNodeInstance> instances2 = sortByCompletionDate(processInstance, activity2);
		if (instances2.isEmpty())
			return false;

		IDeclarativeNodeInstance lastActivity1 = instances1.get(instances1.size() - 1);
		IDeclarativeNodeInstance lastActivity2 = instances2.get(instances2.size() - 1);

		return lastActivity1.getEndTime().compareTo(lastActivity2.getEndTime()) < 0;
	}

	public String getDescription() {
		return "If '" + activity1.getName() + "' is executed '" + activity2.getName() + "' has to be executed afterwards.";
	}

	protected List<IDeclarativeNodeInstance> sortByCompletionDate(DeclarativeProcessInstance instance, DeclarativeActivity activity) {
		List<IDeclarativeNodeInstance> activities = instance.getCompletedActivities(activity);
		Collections.sort(activities, new Comparator<IDeclarativeNodeInstance>() {
			public int compare(IDeclarativeNodeInstance instance1, IDeclarativeNodeInstance instance2) {
				return instance1.getEndTime().compareTo(instance2.getEndTime());
			}
		});

		return activities;
	}
}
