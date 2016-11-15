package at.component.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import at.component.IComponent;
import at.component.event.IEventInformation;

public class EventService {

	public void showEventDialog(IComponent component) {
		try {
			IEventInformation eventInformation = Activator.getComponentService().getEventInformation(component);
			Shell activeShell = Display.getCurrent().getActiveShell();

			if (eventInformation != null) {
				EventInformationDialog dialog = new EventInformationDialog(activeShell, "EventInformation for component: "
						+ component.getNameWithId(), eventInformation);
				dialog.open();
			} else {
				MessageDialog.openInformation(activeShell, "No Events", "The component \"" + component.getNameWithId()
						+ "\" does not send any events");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
