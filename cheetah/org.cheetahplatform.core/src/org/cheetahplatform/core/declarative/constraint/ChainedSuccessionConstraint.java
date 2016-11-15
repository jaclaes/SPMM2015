package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class ChainedSuccessionConstraint extends AbstractDeclarativeConstraintWithTwoActivities {

	private static final long serialVersionUID = -3313913905020511018L;

	protected ChainedSuccessionConstraint() {
		super(); // hibernate
	}

	public ChainedSuccessionConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		IDeclarativeNodeInstance activity = processInstance.getLatestCompletedActivity();
		if (activity == null) {
			return true;
		}

		if (processInstance.getCount(activity1, INodeInstanceState.LAUNCHED) > 0) {
			return false;
		}

		return !activity1.equals(activity.getNode());
	}

	public String getDescription() {
		String message = "''{0}'' must be immediately preceded by ''{1}'', ''{0}'' must be executed immediately after ''{1}''.";
		return MessageFormat.format(message, activity2.getName(), activity1.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		IDeclarativeNodeInstance latestCompletedActivity = processInstance.getLatestCompletedActivity();

		if (latestCompletedActivity == null) {
			return !activity.equals(activity2);
		} else if (activity.equals(activity1)) {
			return !latestCompletedActivity.getNode().equals(activity1);
		} else if (activity.equals(activity2)) {
			return latestCompletedActivity.getNode().equals(activity1);
		}

		return !latestCompletedActivity.getNode().equals(activity1);
	}

}
