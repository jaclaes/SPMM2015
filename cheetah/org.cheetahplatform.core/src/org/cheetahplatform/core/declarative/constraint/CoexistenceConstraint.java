package org.cheetahplatform.core.declarative.constraint;

import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class CoexistenceConstraint extends AbstractDeclarativeTerminationConstraintWithTwoActivities {

	private static final long serialVersionUID = 2591461958203014998L;

	public CoexistenceConstraint(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		super(activity1, activity2);
	}

	@Override
	protected void addActivities(List<DeclarativeActivity> activities) {
		activities.add(activity1);
		activities.add(activity2);
	}

	@Override
	protected void addEndActivities(List<DeclarativeActivity> activities) {
		activities.add(activity1);
	}

	@Override
	protected void addStartActivities(List<DeclarativeActivity> activities) {
		activities.add(activity2);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		boolean isActivity1Completed = processInstance.getCompletedNodeInstanceCount(activity1) > 0;
		boolean isActivity2Completed = processInstance.getCompletedNodeInstanceCount(activity2) > 0;

		return !(isActivity1Completed ^ isActivity2Completed);
	}

	public String getDescription() {
		return "Whenever '" + activity1.getName() + "' is executed '" + activity2.getName() + "' has to be executed and vice-versa.";
	}

	@Override
	public String toString() {
		return getDescription();
	}

}
