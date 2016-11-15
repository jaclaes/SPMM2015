package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class ReconnectEdgeTutorialStep extends AbstractTutorialStep {

	public ReconnectEdgeTutorialStep() {
		this(Messages.ReconnectEdgeTutorialStep_0);
	}

	/**
	 * @param description
	 */
	public ReconnectEdgeTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/ReconnectEdge/ReconnectEdge.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RECONNECT_EDGE);
	}

}
