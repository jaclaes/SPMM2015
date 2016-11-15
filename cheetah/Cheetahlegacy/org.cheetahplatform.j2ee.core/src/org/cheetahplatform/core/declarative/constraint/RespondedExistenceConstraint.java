package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class RespondedExistenceConstraint extends AbstractDeclarativeTerminationConstraintWithTwoActivities {

	private static final long serialVersionUID = -1126401323607682524L;

	public RespondedExistenceConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		boolean activity1Instantiated = processInstance.hasUncanceledInstanceOf(activity1);
		if (!activity1Instantiated) {
			return true;
		}

		return processInstance.getCompletedNodeInstanceCount(activity2) > 0;
	}

	public String getDescription() {
		return "If '" + activity1.getName() + "' is executed '" + activity2.getName() + "' has to be executed too.";
	}

}
