package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;
import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class MultiSuccessionConstraint extends MultiActivityConstraint {

	private static final long serialVersionUID = -1527599391678640426L;

	public MultiSuccessionConstraint() {
		super();
	}

	public MultiSuccessionConstraint(List<DeclarativeActivity> startActivities, List<DeclarativeActivity> endActivities) {
		super(startActivities, endActivities);
	}

	public StringBuilder assembleOrListing(List<DeclarativeActivity> activities) {
		StringBuilder listing = new StringBuilder();
		boolean first = true;

		for (DeclarativeActivity activity : activities) {
			if (!first) {
				listing.append(" or ");
			}

			first = false;
			listing.append("'");
			listing.append(activity.getName());
			listing.append("'");
		}
		return listing;
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		Date lastStartDate = getLastCompletionDate(processInstance, getStartActivities());
		Date lastEndDate = getLastCompletionDate(processInstance, getEndActivities());
		if (lastStartDate == null) {
			return true;
		}

		return lastEndDate != null && lastStartDate.compareTo(lastEndDate) < 0;
	}

	public String getDescription() {
		StringBuilder startListing = assembleOrListing(getStartActivities());
		StringBuilder endListing = assembleOrListing(getEndActivities());

		String description = "One of {0} must be executed before {1} can be executed. In addition, after one of {0} has been executed, the process instance cannot be terminated before one of {1} has been executed afterwards.";
		return MessageFormat.format(description, startListing, endListing);
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		List<DeclarativeActivity> startActivities = getStartActivities();
		if (startActivities.contains(activity) || !getEndActivities().contains(activity)) {
			return true;
		}

		return getLastCompletionDate(processInstance, startActivities) != null;
	}

}
