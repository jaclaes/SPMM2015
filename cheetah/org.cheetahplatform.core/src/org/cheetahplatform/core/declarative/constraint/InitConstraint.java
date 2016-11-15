package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class InitConstraint extends AbstractDeclarativeExecutionConstraintWithOneActivity {

	private static final long serialVersionUID = -5961882977161255312L;

	protected InitConstraint() {
		super(null); // hibernate
	}

	public InitConstraint(DeclarativeActivity activity) {
		super(activity);
	}

	public String getDescription() {
		return MessageFormat.format("''{0}'' must be the first activity executed.", activity.getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (processInstance.getCompletedActivities().isEmpty()) {
			return this.activity.equals(activity);
		}

		return true;
	}

}
