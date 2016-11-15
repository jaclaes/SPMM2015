package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class AlternatePrecedenceConstraint extends AbstractDeclarativeExecutionConstraintWithTwoActivities {

	private static final long serialVersionUID = 8017529543030580141L;

	protected AlternatePrecedenceConstraint() {
		super(); // hibernate
	}

	public AlternatePrecedenceConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public String getDescription() {
		String message = "Every execution of ''{0}'' must be preceded by an execution of ''{1}''. Between two executions of ''{1}'', ''{0}'' has to be executed at least once.";
		return MessageFormat.format(message, activity2.getName(), activity1.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!activity.equals(activity2)) {
			return true;
		}

		Date lastCompletionActivity1 = getLastCompletionDate(processInstance, activity1);
		if (lastCompletionActivity1 == null) {
			return false;
		}

		Date lastCompletionActivity2 = getLastCompletionDate(processInstance, activity2);
		return lastCompletionActivity2 == null || lastCompletionActivity1.compareTo(lastCompletionActivity2) > 0;
	}

}
