package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class MutualExclusionConstraint extends AbstractDeclarativeExecutionConstraintWithTwoActivities {

	private static final long serialVersionUID = -4728076135171990342L;

	protected MutualExclusionConstraint() {
		super(); // required for hibernate
	}

	public MutualExclusionConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	public String getDescription() {
		return "Either '" + activity1.getName() + "' or '" + activity2.getName() + "' can be executed, but not both.";
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		DeclarativeActivity toCheck = null;

		if (activity1.equals(activity)) {
			toCheck = activity2;
		} else if (activity2.equals(activity)) {
			toCheck = activity1;
		} else {
			return true; // neither activity1 nor activity2 --> constraint does not influence the execution of the activitiy
		}

		return !processInstance.hasUncanceledInstanceOf(toCheck);
	}

}
