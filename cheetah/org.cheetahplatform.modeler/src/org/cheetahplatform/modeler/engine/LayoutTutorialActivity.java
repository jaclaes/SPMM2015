package org.cheetahplatform.modeler.engine;

import java.util.ArrayList;

import org.cheetahplatform.common.tutorial.ITutorialStep;
import org.cheetahplatform.modeler.tutorial.LayoutTutorialDialog;
import org.cheetahplatform.modeler.tutorial.LayoutTutorialStep;
import org.cheetahplatform.modeler.tutorial.UndoLayoutTutorialStep;
import org.eclipse.swt.widgets.Display;

public class LayoutTutorialActivity extends AbstractExperimentsWorkflowActivity {

	public LayoutTutorialActivity() {
		super("SHOW_LAYOUT_TUTORIAL");
	}

	@Override
	protected void doExecute() {
		ArrayList<ITutorialStep> steps = new ArrayList<ITutorialStep>();
		steps.add(new LayoutTutorialStep());
		steps.add(new UndoLayoutTutorialStep());

		LayoutTutorialDialog dialog = new LayoutTutorialDialog(Display.getDefault().getActiveShell(), steps);
		dialog.open();
	}

	@Override
	public Object getName() {
		return "Show Layout Tutorial";
	}

}
