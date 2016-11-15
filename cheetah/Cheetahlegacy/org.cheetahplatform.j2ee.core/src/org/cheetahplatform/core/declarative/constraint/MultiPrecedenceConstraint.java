package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class MultiPrecedenceConstraint extends MultiActivityConstraint {

	private static final long serialVersionUID = 51249383740400197L;

	public MultiPrecedenceConstraint() {
		super();
	}

	public MultiPrecedenceConstraint(List<DeclarativeActivity> prerequisites, DeclarativeActivity activity) {
		super(prerequisites, activity);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		return true;
	}

	public String getDescription() {
		String activities = concatenateActivities(getStartActivities());
		String rawMessage = "One out of ''{0}'' has to be executed before ''{1}'' can be executed.";
		return MessageFormat.format(rawMessage, activities, getEndActivities().get(0).getName());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!activity.equals(this.getEndActivities().get(0))) {
			return true;
		}

		boolean atLeastOneCompleted = false;
		for (DeclarativeActivity prerequisite : getStartActivities()) {
			if (processInstance.getCompletedNodeInstanceCount(prerequisite) > 0) {
				atLeastOneCompleted = true;
				break;
			}
		}

		return atLeastOneCompleted;
	}

	public void setEndActivity(DeclarativeActivity activity) {
		setEndActivities(Arrays.asList(new DeclarativeActivity[] { activity }));
	}

}
