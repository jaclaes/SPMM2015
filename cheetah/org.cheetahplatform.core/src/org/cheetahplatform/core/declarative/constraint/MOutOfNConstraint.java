package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class MOutOfNConstraint extends AbstractDeclarativeConstraint {

	private static final long serialVersionUID = 5450445482508768699L;

	private List<DeclarativeActivity> activities;
	private int minimum;

	protected MOutOfNConstraint() {
		this(new ArrayList<DeclarativeActivity>(), 0); // hibernate
	}

	public MOutOfNConstraint(List<DeclarativeActivity> activities, int minimum) {
		this.minimum = minimum;
		this.activities = activities;
	}

	@Override
	protected void addActivities(List<DeclarativeActivity> activities) {
		activities.addAll(this.activities);
	}

	@Override
	protected void addEndActivities(List<DeclarativeActivity> activities) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void addStartActivities(List<DeclarativeActivity> activities) {
		throw new UnsupportedOperationException();
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		int count = 0;
		for (DeclarativeActivity activity : activities) {
			if (processInstance.getCompletedNodeInstanceCount(activity) > 0) {
				count++;
			}
		}

		return count >= minimum;
	}

	public String getDescription() {
		String message = "''{0}'' activities from ''{1}'' have to be executed.";
		String listing = concatenateActivities(activities);
		return MessageFormat.format(message, minimum, listing.toString());
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		return true;
	}

}
