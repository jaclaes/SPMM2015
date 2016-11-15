package org.cheetahplatform.modeler.engine;

public class ShowInformationDialogActivity extends AbstractExperimentsWorkflowActivity {

	private final String message;

	public ShowInformationDialogActivity(String message) {
		super("SHOW_INFORMATION");

		this.message = message;
	}

	@Override
	protected void doExecute() {
		InformationDialog.showMessage(message);
	}

	@Override
	public Object getName() {
		return "Show Information Dialog";
	}

}
