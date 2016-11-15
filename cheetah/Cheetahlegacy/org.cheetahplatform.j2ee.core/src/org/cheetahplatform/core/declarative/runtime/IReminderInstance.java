package org.cheetahplatform.core.declarative.runtime;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.IReminder;

/**
 * This class represents an instance of a {@link Reminder}.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.07.2009
 */
public interface IReminderInstance {
	/**
	 * Returns the reminder.
	 * 
	 * @return the reminder
	 */
	IReminder getReminder();

	/**
	 * Returns the {@link DateTime} when the {@link IReminderInstance} should be activated.
	 * 
	 * @return the {@link DateTime} when the {@link IReminderInstance} should be activated
	 */
	DateTime getTime();

	/**
	 * Checks if the {@link IReminderInstance} is currently activated.
	 * 
	 * @param processInstance
	 *            the {@link DeclarativeProcessInstance}
	 * @return <code>true</code> if the {@link IReminderInstance} is currently activated, <code>false</code> otherwise
	 */
	boolean isActive(DeclarativeProcessInstance processInstance);

	/**
	 * Returns <code>true</code> if the {@link IReminderInstance} has been dismissed by the user,<code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the {@link IReminderInstance} has been dismissed by the user,<code>false</code> otherwise
	 */
	boolean isDismissed();

	/**
	 * Sets the dismissed state of the {@link IReminderInstance}.
	 * 
	 * @param dismissed
	 *            the dismissed state
	 */
	void setDismissed(boolean dismissed);
}
