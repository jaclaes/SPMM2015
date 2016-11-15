package org.cheetahplatform.client;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;

public class UiUtils {
	/**
	 * Logs the error and displays a message to the user.
	 * 
	 * @param message
	 *            the message to display
	 * @param e
	 *            the {@link Exception}
	 */
	public static void showAndLogError(String message, Exception e) {
		UiUtils.showError(message, e);
		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

	public static void showError(String message) {
		ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error", message, new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				message));

	}

	public static void showError(String message, Exception exception) {
		ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error", message, new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				message, exception));
	}
}
