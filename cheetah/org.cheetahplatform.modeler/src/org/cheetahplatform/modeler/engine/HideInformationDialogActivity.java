package org.cheetahplatform.modeler.engine;

public class HideInformationDialogActivity extends AbstractExperimentsWorkflowActivity {

	public HideInformationDialogActivity() {
		super("HIDE_INFORMATION");
	}

	@Override
	protected void doExecute() {
		InformationDialog.hide();
	}

	@Override
	public Object getName() {
		return "Hide Information Dialog";
	}

}
