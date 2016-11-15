package org.cheetahplatform.modeler.engine.configurations;

import org.cheetahplatform.modeler.dialog.ParticipantInformationDialog;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ParticipantInformationWorkflowActivity extends AbstractExperimentsWorkflowActivity {

	private final int durationInMinutes;

	protected ParticipantInformationWorkflowActivity(int durationInMinutes) {
		super("PARTICIPANT_INFORMATION");
		this.durationInMinutes = durationInMinutes;
	}

	@Override
	protected void doExecute() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ParticipantInformationDialog participantInformationDialog = new ParticipantInformationDialog(shell, durationInMinutes);
		if (participantInformationDialog.open() != Window.OK) {
			PlatformUI.getWorkbench().close();
			System.exit(0);
		}
	}

	@Override
	public Object getName() {
		return "Participant Information";
	}
}
