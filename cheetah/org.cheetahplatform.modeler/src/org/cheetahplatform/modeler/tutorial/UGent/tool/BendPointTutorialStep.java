package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class BendPointTutorialStep extends AbstractTutorialStep {

	public BendPointTutorialStep() {
		super(Messages.BendPointTutorialStep_0);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/AddBendPoint/AddBendPoint.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (entry.getEventType().equals(AbstractGraphCommand.CREATE_EDGE_BENDPOINT)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.MOVE_EDGE_BENDPOINT)) {
			return true;
		}
		if (entry.getEventType().equals(AbstractGraphCommand.DELETE_EDGE_BENDPOINT)) {
			return true;
		}
		return false;
	}

}
