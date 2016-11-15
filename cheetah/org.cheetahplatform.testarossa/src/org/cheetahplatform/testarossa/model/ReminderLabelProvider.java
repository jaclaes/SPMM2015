package org.cheetahplatform.testarossa.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cheetahplatform.common.IDeferredObjectProvider;
import org.cheetahplatform.core.declarative.modeling.MilestoneActivityReminder;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.testarossa.Activator;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ReminderLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	private final IDeferredObjectProvider<DeclarativeProcessInstance> provider;

	public ReminderLabelProvider(IDeferredObjectProvider<DeclarativeProcessInstance> provider) {
		this.provider = provider;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		IReminderInstance reminderInstance = ((IReminderInstance) element);
		if (columnIndex == 0) {
			if (reminderInstance.isDismissed())
				return ResourceManager.getPluginImage(Activator.getDefault(), "img/reminder_disabled.png");

			return ResourceManager.getPluginImage(Activator.getDefault(), "img/reminder.png");
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		IReminderInstance reminderInstance = ((IReminderInstance) element);

		switch (columnIndex) {
		case 0:
			return reminderInstance.getReminder().getName();
		case 1:
			if (reminderInstance.getReminder() instanceof MilestoneActivityReminder) {
				MilestoneActivityReminder casted = (MilestoneActivityReminder) reminderInstance.getReminder();
				Role role = RoleLookup.getInstance().getRole(provider.get(), casted.getActivity());
				if (role != null) {
					return role.getName();
				}
			}

			return "";
		case 2:
			Date javaUtilDate = reminderInstance.getTime().toJavaUtilDate();
			return DATE_FORMAT.format(javaUtilDate);
		default:
			throw new IllegalArgumentException("Illegal column: " + columnIndex);
		}
	}
}