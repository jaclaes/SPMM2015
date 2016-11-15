package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class LastConstraint extends AbstractDeclarativeExecutionConstraintWithOneActivity {

	private static final long serialVersionUID = -77899072331282809L;

	public LastConstraint(DeclarativeActivity activity) {
		super(activity);
	}

	public String getDescription() {
		String message = "After ''{0}'' has been executed, no other activity can be executed anymore.";
		return MessageFormat.format(message, activity.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!processInstance.getCompletedActivities(this.activity).isEmpty()) {
			return this.activity.equals(activity);
		}

		return true;
	}

}
