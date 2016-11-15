package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.IBMLayouter;

public class LayoutTutorialStep extends AbstractTutorialStep {

	public LayoutTutorialStep() {
		super("Lay out the process model using the Layout dialog.");
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/Layout/Layout.htm";
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (entry.getEventType().equals(PromLogger.GROUP_EVENT_START)) {
			String attribute = entry.getAttributeSafely(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME);
			return IBMLayouter.LAYOUT.equals(attribute);
		}

		return false;
	}

}
