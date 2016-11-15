package org.cheetahplatform.modeler.screencast;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.swt.widgets.Display;

public class ShowScreencastActivity extends AbstractExperimentsWorkflowActivity {

	private String path;

	public ShowScreencastActivity(String path) {
		super("SHOW_SCREENCAST");

		this.path = path;
	}

	@Override
	protected void doExecute() {
		ShowScreencastDialog dialog = new ShowScreencastDialog(Display.getDefault().getActiveShell(), path);
		dialog.open();
	}

	@Override
	public Object getName() {
		return "Show Screencast";
	}

}
