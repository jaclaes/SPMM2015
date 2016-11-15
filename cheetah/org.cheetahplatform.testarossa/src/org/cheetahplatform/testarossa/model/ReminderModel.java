/**
 * 
 */
package org.cheetahplatform.testarossa.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.IDeferredObjectProvider;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         02.07.2009
 */
public class ReminderModel {

	/**
	 * Returns an {@link IContentProvider} for the reminders.
	 * 
	 * @return an {@link IContentProvider} for the reminders
	 */
	public IContentProvider createContentProvider() {
		return new ArrayContentProvider();
	}

	/**
	 * @return
	 */
	public IBaseLabelProvider createLabelProvider(IDeferredObjectProvider<DeclarativeProcessInstance> provider) {
		return new ReminderLabelProvider(provider);
	}

	public DeclarativeProcessInstance getProcessInstance() {
		return TestaRossaModel.getInstance().getCurrentInstance();
	}

	/**
	 * Returns all reminders of the {@link DeclarativeProcessInstance}.
	 * 
	 * @return all reminders of the {@link DeclarativeProcessInstance}
	 */
	public List<IReminderInstance> getReminders() {
		DeclarativeProcessInstance currentInstance = getProcessInstance();
		if (currentInstance == null)
			return Collections.emptyList();
		List<IReminderInstance> remindersToDisplay = new ArrayList<IReminderInstance>();
		List<IReminderInstance> reminders = currentInstance.getReminders();
		for (IReminderInstance reminder : reminders) {
			if (reminder.getTime().compareTo(DateTimeProvider.getDateTimeSource().getCurrentTime(false)) < 0) {
				remindersToDisplay.add(reminder);
			}
		}

		return remindersToDisplay;
	}

}
