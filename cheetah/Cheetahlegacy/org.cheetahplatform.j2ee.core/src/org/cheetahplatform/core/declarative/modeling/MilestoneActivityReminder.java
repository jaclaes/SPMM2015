package org.cheetahplatform.core.declarative.modeling;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.NamedIdentifiableObject;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import org.cheetahplatform.core.declarative.runtime.MilestoneActivityReminderInstance;

public class MilestoneActivityReminder extends NamedIdentifiableObject implements IReminder {
	private static final long serialVersionUID = 5690427230368171824L;
	private DeclarativeActivity activity;
	private Milestone milestone;
	private String reminderText;
	private Duration duration;

	@SuppressWarnings("unused")
	private MilestoneActivityReminder() {
		// hibernate
	}

	/**
	 * @param name
	 * @param activity
	 * @param milestone
	 * @param reminderText
	 * @param duration
	 */
	public MilestoneActivityReminder(DeclarativeActivity activity, Milestone milestone, String name, String reminderText, Duration duration) {
		super(name);
		Assert.isNotNull(activity);
		Assert.isNotNull(milestone);
		Assert.isNotNull(reminderText);
		Assert.isNotNull(duration);

		this.activity = activity;
		this.milestone = milestone;
		this.reminderText = reminderText;
		this.duration = duration;
	}

	/**
	 * Returns the activity.
	 * 
	 * @return the activity
	 */
	public DeclarativeActivity getActivity() {
		return activity;
	}

	/**
	 * Returns the duration.
	 * 
	 * @return the duration
	 */
	public Duration getDuration() {
		return duration;
	}

	/**
	 * Returns the milestone.
	 * 
	 * @return the milestone
	 */
	public Milestone getMilestone() {
		return milestone;
	}

	/**
	 * Returns the reminderText.
	 * 
	 * @return the reminderText
	 */
	public String getReminderText() {
		return reminderText;
	}

	public IReminderInstance instantiate(DeclarativeProcessInstance instance) {
		IReminderInstance reminder = new MilestoneActivityReminderInstance(instance, this);
		instance.addReminder(reminder);
		return reminder;
	}
}
