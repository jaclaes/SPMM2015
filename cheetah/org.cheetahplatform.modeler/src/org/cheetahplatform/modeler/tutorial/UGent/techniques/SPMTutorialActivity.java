package org.cheetahplatform.modeler.tutorial.UGent.techniques;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.ui.PlatformUI;

public class SPMTutorialActivity extends AbstractExperimentsWorkflowActivity {

	public SPMTutorialActivity() {
		super("SPMTUTORIAL"); //$NON-NLS-1$
	}

	@Override
	protected void doExecute() {
		SPMTutorialPreDialog preDialog = new SPMTutorialPreDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		preDialog.open();
		SPMTutorialDialog dialog = new SPMTutorialDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.open();
		SPMTutorialPostDialog postDialog = new SPMTutorialPostDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		postDialog.open();
	}

	@Override
	public Object getName() {
		return Messages.SPMTutorialActivity_2;
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
