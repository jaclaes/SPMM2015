package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class EarliestStartTimeConstraint extends AbstractDeclarativeExecutionConstraintWithOneActivity {

	private static final long serialVersionUID = 7036029591060079565L;
	private Duration durationSinceStart;

	protected EarliestStartTimeConstraint() {
		super(null);
	}

	public EarliestStartTimeConstraint(DeclarativeActivity activity, Duration startTime) {
		super(activity);

		Assert.isNotNull(activity);
		Assert.isNotNull(startTime);
		this.durationSinceStart = startTime;
	}

	public String getDescription() {
		return "Activity '" + activity.getName() + "' can not be executed before " + durationSinceStart.toString();
	}

	/**
	 * Return the durationSinceStart.
	 * 
	 * @return the durationSinceStart
	 */
	public Duration getDurationSinceStart() {
		return durationSinceStart;
	}

	public boolean isExecutable(DeclarativeActivity activityToCheck, DeclarativeProcessInstance processInstance) {
		if (!activity.equals(activityToCheck))
			return true;

		DateTime startTime = new DateTime(processInstance.getStartTime());
		startTime.plus(durationSinceStart);

		return startTime.compareTo(DateTimeProvider.getDateTimeSource().getCurrentTime(true)) <= 0;
	}
}
