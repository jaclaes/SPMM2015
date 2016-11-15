package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class RenameNodeTutorialStep extends AbstractTutorialStep {

	public RenameNodeTutorialStep() {
		this(Messages.RenameNodeTutorialStep_0);
	}

	public RenameNodeTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/RenameNode/RenameNode.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RENAME);
	}
}
