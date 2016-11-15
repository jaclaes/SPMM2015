package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         27.10.2009
 */
public class BendPointTutorialStep extends AbstractTutorialStep {

	public BendPointTutorialStep() {
		super(Messages.BendPointTutorialStep_0);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/AddBendPoint/AddBendPoint.htm"; //$NON-NLS-1$
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
