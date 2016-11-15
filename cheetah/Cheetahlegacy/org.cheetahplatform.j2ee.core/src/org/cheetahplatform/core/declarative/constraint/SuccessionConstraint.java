package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class SuccessionConstraint extends AbstractDeclarativeConstraintWithTwoActivities {

	private static final long serialVersionUID = 4958225465646900461L;

	public SuccessionConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		Date completionDateActivity1 = getLastCompletionDate(processInstance, activity1);
		if (completionDateActivity1 == null) {
			return true;
		}

		Date completionDateActivity2 = getLastCompletionDate(processInstance, activity2);
		return completionDateActivity2 != null && completionDateActivity1.compareTo(completionDateActivity2) < 0;
	}

	public String getDescription() {
		return "Activity '" + activity1.getName() + "' must be executed before activity '" + activity2.getName()
				+ "' can be executed. In addition, after activity '" + activity1.getName()
				+ "' has been executed, the process instance cannot be terminated before activity '" + activity2.getName()
				+ "' has been executed afterwards.";
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (activity.equals(activity2)) {
			return processInstance.getCompletedNodeInstanceCount(activity1) > 0;
		}

		return true;
	}

}
