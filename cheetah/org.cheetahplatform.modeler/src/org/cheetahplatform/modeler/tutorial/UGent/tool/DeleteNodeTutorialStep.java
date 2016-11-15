package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class DeleteNodeTutorialStep extends AbstractTutorialStep {

	public DeleteNodeTutorialStep() {
		this(Messages.DeleteNodeTutorialStep_0);
	}

	public DeleteNodeTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/DeleteNode/DeleteNode.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.DELETE_NODE);
	}

}
