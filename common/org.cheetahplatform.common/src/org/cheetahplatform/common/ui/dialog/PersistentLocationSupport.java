package org.cheetahplatform.common.ui.dialog;

import org.cheetahplatform.common.Activator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple storage for storing the location of a shell.
 * 
 * @author Stefan Zugal
 * 
 */
public class PersistentLocationSupport {

	private final String xLocationKey;
	private final String yLocationKey;

	public PersistentLocationSupport(String xLocationKey, String yLocationKey) {
		this.xLocationKey = xLocationKey;
		this.yLocationKey = yLocationKey;
	}

	public void restoreLocation(Shell shell) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		int defaultValue = -1;
		preferenceStore.setDefault(xLocationKey, defaultValue);
		int x = preferenceStore.getInt(xLocationKey);
		int y = preferenceStore.getInt(yLocationKey);

		Rectangle bounds = shell.getDisplay().getBounds();

		boolean isOnScreen = bounds.contains(x, y);
		boolean isNonDefaultValue = x != defaultValue;
		if (isNonDefaultValue && isOnScreen) {
			shell.setLocation(new Point(x, y));
		} else {
			Rectangle screenBounds = shell.getDisplay().getPrimaryMonitor().getBounds();
			Rectangle shellBounds = shell.getBounds();
			shell.setLocation((screenBounds.width - shellBounds.width) / 2, (screenBounds.height - shellBounds.height) / 2);
		}
	}

	public void storeLocation(Shell shell) {
		if (shell.isDisposed()) {
			return;
		}

		Point location = shell.getLocation();
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setValue(xLocationKey, location.x);
		preferenceStore.setValue(yLocationKey, location.y);
	}
}
