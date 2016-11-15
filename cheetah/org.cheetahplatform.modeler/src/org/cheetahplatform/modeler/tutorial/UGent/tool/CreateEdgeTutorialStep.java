package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class CreateEdgeTutorialStep extends AbstractTutorialStep {

	public CreateEdgeTutorialStep() {
		this(Messages.CreateEdgeTutorialStep_0);
	}

	public CreateEdgeTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/CreateEdge/CreateEdge.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return (entry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE));
	}
}
