package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class MoveNodeChangePatternTutorialStep extends MoveNodeTutorialStep {
	private boolean endOfChangePatternReached;

	public MoveNodeChangePatternTutorialStep() {
		super("Nodes can be moved by dragging them around as shown in the video below.");
		endOfChangePatternReached = false;
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/MoveNode/MoveNode.htm";
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (PromLogger.GROUP_EVENT_END.equals(entry.getEventType())) {
			endOfChangePatternReached = true;
			return false;
		}

		if (endOfChangePatternReached) {
			return super.isStepExecuted(entry);
		}

		return false;
	}
}
