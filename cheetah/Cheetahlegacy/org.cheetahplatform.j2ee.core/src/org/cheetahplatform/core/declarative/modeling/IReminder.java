package org.cheetahplatform.core.declarative.modeling;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;

/**
 * This interface represents a reminder.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.07.2009
 */
public interface IReminder extends INamed, IIdentifiableObject {

	/**
	 * Returns the reminderText.
	 * 
	 * @return the reminderText
	 */
	String getReminderText();

	/**
	 * Creates a new instance of the {@link Reminder}.
	 * 
	 * @param instance
	 *            the {@link DeclarativeProcessInstance} to which the newly created {@link IReminderInstance} belongs
	 * @return the newly created {@link IReminderInstance}
	 */
	IReminderInstance instantiate(DeclarativeProcessInstance instance);

}
