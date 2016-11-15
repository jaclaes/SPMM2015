package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class MoveNodeTutorialStep extends AbstractTutorialStep {
	public MoveNodeTutorialStep() {
		this(Messages.MoveNodeTutorialStep_0);
	}

	public MoveNodeTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/MoveNode/MoveNode.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.MOVE_NODE);
	}
}
