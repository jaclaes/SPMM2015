package at.component.framework.manager;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		ManagerUIController controller = new ManagerUIController();
		
		Shell shell = controller.getUi().getShell();
		Display display = shell.getDisplay();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		return EXIT_OK;
	}

	@Override
	public void stop() {
	}
}
