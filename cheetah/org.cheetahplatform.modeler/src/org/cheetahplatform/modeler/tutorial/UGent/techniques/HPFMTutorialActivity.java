package org.cheetahplatform.modeler.tutorial.UGent.techniques;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.ui.PlatformUI;

public class HPFMTutorialActivity extends AbstractExperimentsWorkflowActivity {

	public HPFMTutorialActivity() {
		super("HPFMTUTORIAL"); //$NON-NLS-1$
	}

	@Override
	protected void doExecute() {
		HPFMTutorialPreDialog preDialog = new HPFMTutorialPreDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		preDialog.open();
		HPFMTutorialDialog dialog = new HPFMTutorialDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
		HPFMTutorialPostDialog postDialog = new HPFMTutorialPostDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		postDialog.open();
	}

	@Override
	public Object getName() {
		return Messages.HPFMTutorialActivity_2;
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
