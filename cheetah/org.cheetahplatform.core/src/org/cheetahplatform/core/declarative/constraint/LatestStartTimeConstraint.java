package org.cheetahplatform.core.declarative.constraint;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class LatestStartTimeConstraint extends AbstractDeclarativeExecutionConstraintWithOneActivity {

	private static final long serialVersionUID = -6895456530752470952L;
	private Duration durationSinceStart;

	protected LatestStartTimeConstraint() {
		super(null);
	}

	/**
	 * @param node
	 * @param dateTime
	 */
	public LatestStartTimeConstraint(DeclarativeActivity activity, Duration durationSinceStart) {
		super(activity);

		Assert.isNotNull(activity);
		Assert.isNotNull(durationSinceStart);
		this.durationSinceStart = durationSinceStart;
	}

	public String getDescription() {
		return "Activity " + activity.getName() + " can not be executed after " + durationSinceStart.toString();
	}

	public boolean isExecutable(DeclarativeActivity activityToCheck, DeclarativeProcessInstance processInstance) {
		if (!activity.equals(activityToCheck))
			return true;

		DateTime startTime = new DateTime(processInstance.getStartTime());
		startTime.plus(durationSinceStart);

		return startTime.compareTo(DateTimeProvider.getDateTimeSource().getCurrentTime(true)) > 0;
	}
}
