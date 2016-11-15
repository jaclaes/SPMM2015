package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;
import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class MultiResponseConstraint extends MultiActivityConstraint {

	private static final long serialVersionUID = -6130335302585276533L;

	public MultiResponseConstraint() {
		super();
	}

	public MultiResponseConstraint(DeclarativeActivity startActivity, List<DeclarativeActivity> endActivities) {
		super(startActivity, endActivities);
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		DeclarativeActivity start = getStartActivities().get(0);
		if (processInstance.getCompletedActivities(start).isEmpty()) {
			return true;
		}

		Date lastStartCompletion = getLastCompletionDate(processInstance, start);
		Date lastEndCompletion = getLastCompletionDate(processInstance, getEndActivities());
		return lastEndCompletion != null && lastStartCompletion.compareTo(lastEndCompletion) < 0;
	}

	public String getDescription() {
		String message = "After ''{0}'' has been executed, one of {1} has to be executed afterwards.";
		return MessageFormat.format(message, getStartActivities().get(0).getName(), concatenateActivities(getEndActivities()));
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		return true;// constraint affects termination only
	}

}
