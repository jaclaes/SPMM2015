/**
 * 
 */
package org.cheetahplatform.core.declarative.runtime;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.IReminder;
import org.cheetahplatform.core.declarative.modeling.MilestoneActivityReminder;

/**
 * A reminder for an activity being relative to a milestone in a {@link DeclarativeProcessInstance}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.07.2009
 */
public class MilestoneActivityReminderInstance extends IdentifiableObject implements IReminderInstance {
	private static final long serialVersionUID = 36725043173509302L;
	private boolean dismissed;
	private final MilestoneActivityReminder reminder;
	private final DeclarativeProcessInstance processInstance;

	/**
	 * @param milestoneActivityReminder
	 */
	public MilestoneActivityReminderInstance(DeclarativeProcessInstance processInstance, MilestoneActivityReminder reminder) {
		Assert.isNotNull(reminder);
		Assert.isNotNull(processInstance);
		this.processInstance = processInstance;
		this.reminder = reminder;
		this.dismissed = false;
	}

	public IReminder getReminder() {
		return reminder;
	}

	public DateTime getTime() {
		DateTime startTime = new DateTime(processInstance.getStartTime());
		MilestoneInstance milestoneInstance = processInstance.findMilestoneInstance(reminder.getMilestone());
		Duration durationSinceStart = milestoneInstance.getActualDurationSinceStart();
		startTime.plus(durationSinceStart);
		startTime.minus(reminder.getDuration());
		return startTime;
	}

	public boolean isActive(DeclarativeProcessInstance processInstance) {
		if (dismissed) {
			return false;
		}

		DateTime currentTime = DateTimeProvider.getDateTimeSource().getCurrentTime(true);
		return currentTime.compareTo(getTime()) >= 0;
	}

	public boolean isDismissed() {
		return dismissed;
	}

	public void setDismissed(boolean dismissed) {
		this.dismissed = dismissed;
	}
}
