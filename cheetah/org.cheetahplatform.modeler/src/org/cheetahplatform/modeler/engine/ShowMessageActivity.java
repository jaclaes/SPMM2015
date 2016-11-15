package org.cheetahplatform.modeler.engine;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ShowMessageActivity extends AbstractExperimentsWorkflowActivity {

	private String title;
	private String message;

	public ShowMessageActivity(String title, String message) {
		super("SHOW_MESSAGE");

		this.title = title;
		this.message = message;
	}

	@Override
	protected void doExecute() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}

		Shell shell = window.getShell();
		MessageDialog.openInformation(shell, title, message);
	}

	@Override
	public Object getName() {
		return "Display Message";
	}

}
