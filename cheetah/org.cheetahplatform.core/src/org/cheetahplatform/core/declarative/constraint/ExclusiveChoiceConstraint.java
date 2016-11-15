package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;
import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;

public class ExclusiveChoiceConstraint extends AbstractDeclarativeConstraintWithTwoActivities {

	private static final long serialVersionUID = 6860033793393397644L;

	protected ExclusiveChoiceConstraint() {
		super(); // hibernate
	}

	public ExclusiveChoiceConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		List<IDeclarativeNodeInstance> completedActivities1 = processInstance.getCompletedActivities(activity1);
		List<IDeclarativeNodeInstance> completedActivities2 = processInstance.getCompletedActivities(activity2);

		return !completedActivities1.isEmpty() || !completedActivities2.isEmpty();
	}

	public String getDescription() {
		String message = "''{0}'' or ''{1}'' has to be executed, but not  both.";
		return MessageFormat.format(message, activity1.getName(), activity2.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!(activity.equals(activity1) || activity.equals(activity2))) {
			return true;
		}

		DeclarativeActivity toCheck = null;
		if (activity.equals(activity1)) {
			toCheck = activity2;
		} else {
			toCheck = activity1;
		}

		return processInstance.getUncanceledCount(toCheck) == 0;
	}

}
