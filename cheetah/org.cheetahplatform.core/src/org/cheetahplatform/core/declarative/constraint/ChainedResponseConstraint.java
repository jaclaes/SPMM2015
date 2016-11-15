package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class ChainedResponseConstraint extends AbstractDeclarativeConstraintWithTwoActivities {

	private static final long serialVersionUID = 5355483826879668899L;

	protected ChainedResponseConstraint() {
		super(); // hibernate
	}

	public ChainedResponseConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
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
		return MessageFormat.format("After executing ''{0}'', ''{1}'' has to be executed immediately.", activity1.getName(),
				activity2.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		// for each completed A there should be B following - not necessary immediately, as some parallel activity that has been started
		// earlier may finish in between.
		if (activity.equals(activity2)) {
			return true;
		}

		Date lastCompletionDateActivity1 = getLastCompletionDate(processInstance, activity1);
		Date lastCompletionDateActivity2 = getLastCompletionDate(processInstance, activity2);

		if (lastCompletionDateActivity1 == null) {
			return true;
		}
		if (lastCompletionDateActivity2 == null) {
			return false;
		}

		return lastCompletionDateActivity1.compareTo(lastCompletionDateActivity2) < 0;
	}

}
