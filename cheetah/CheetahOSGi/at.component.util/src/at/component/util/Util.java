package at.component.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public class Util {
	public static void centerShell(Shell shell) {
		Rectangle rec = shell.getDisplay().getPrimaryMonitor().getClientArea();
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation((rec.width - shellBounds.width) / 2, (rec.height - shellBounds.height) / 2);
	}
}
