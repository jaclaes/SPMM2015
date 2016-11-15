package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class NegationResponseConstraint extends AbstractDeclarativeExecutionConstraintWithTwoActivities {

	private static final long serialVersionUID = 6309378805029723690L;

	protected NegationResponseConstraint() {
		super(); // hibernate
	}

	public NegationResponseConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public String getDescription() {
		String description = "After activity ''{0}'' has been executed, activity ''{1}'' cannot be executed anymore.";
		return MessageFormat.format(description, activity1.getName(), activity2.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!activity2.equals(activity)) {
			return true;
		}

		return !processInstance.hasUncanceledInstanceOf(activity1);
	}

}
