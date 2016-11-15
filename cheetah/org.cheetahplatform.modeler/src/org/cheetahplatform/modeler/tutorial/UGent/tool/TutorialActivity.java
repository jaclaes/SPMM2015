package org.cheetahplatform.modeler.tutorial.UGent.tool;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.tutorial.ITutorialStep;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.eclipse.ui.PlatformUI;

public class TutorialActivity extends AbstractExperimentsWorkflowActivity {

	public TutorialActivity() {
		super("TUTORIAL"); //$NON-NLS-1$
	}

	@Override
	protected void doExecute() {
		List<ITutorialStep> tutorialSteps = new ArrayList<ITutorialStep>();

		tutorialSteps.add(new AddStartNodeTutorialStep());
		tutorialSteps.add(new AddActivityTutorialStep());
		tutorialSteps.add(new AddActivityTutorialStep(Messages.TutorialActivity_0));
		tutorialSteps.add(new MoveNodeTutorialStep());
		tutorialSteps.add(new DeleteNodeTutorialStep());
		tutorialSteps.add(new RenameNodeTutorialStep());
		tutorialSteps.add(new CreateEdgeTutorialStep());
		tutorialSteps.add(new AddEndNodeTutorialStep());
		tutorialSteps.add(new ReconnectEdgeTutorialStep());
		tutorialSteps.add(new AddGatewayTutorialStep());
		tutorialSteps.add(new AddConditionTutorialStep());
		tutorialSteps.add(new BendPointTutorialStep());

		TutorialDialog dialog = new TutorialDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), tutorialSteps);
		dialog.open();
	}

	@Override
	public Object getName() {
		return Messages.TutorialActivity_2;
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
