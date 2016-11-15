package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;

public class ChainedPrecedenceConstraint extends AbstractDeclarativeExecutionConstraintWithTwoActivities {

	private static final long serialVersionUID = -4310432049886088593L;

	public ChainedPrecedenceConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public String getDescription() {
		String description = "''{0}'' can only be executed if ''{1}'' has been executed immediately before.";
		return MessageFormat.format(description, activity2.getName(), activity1.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!activity.equals(activity2)) {
			return true;
		}

		IDeclarativeNodeInstance lastCompleted = processInstance.getLatestCompletedActivity();
		return lastCompleted != null && lastCompleted.getNode().equals(activity1);
	}

}
