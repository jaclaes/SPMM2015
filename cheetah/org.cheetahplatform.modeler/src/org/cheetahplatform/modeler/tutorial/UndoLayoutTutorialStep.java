package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;

public class UndoLayoutTutorialStep extends AbstractTutorialStep {

	public UndoLayoutTutorialStep() {
		super("Undo the model's layout using the Layout dialog.");
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UndoLayout/UndoLayout.htm";
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (entry.getEventType().equals(PromLogger.GROUP_EVENT_START)) {
			if (entry.isAttributeDefined(CommonConstants.ATTRIBUTE_UNDO_EVENT)) {
				return entry.getBooleanAttribute(CommonConstants.ATTRIBUTE_UNDO_EVENT);
			}
		}

		return false;
	}

}
