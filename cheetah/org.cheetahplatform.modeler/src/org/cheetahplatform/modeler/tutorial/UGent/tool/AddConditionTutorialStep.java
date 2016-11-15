package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class AddConditionTutorialStep extends AbstractTutorialStep {

	public AddConditionTutorialStep() {
		this(Messages.AddConditionTutorialStep_0);
	}

	public AddConditionTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/AddCondition/AddCondition.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (!entry.getEventType().equals(AbstractGraphCommand.RENAME)) {
			return false;
		}

		if (!entry.getAttribute(AbstractGraphCommand.DESCRIPTOR).equals(EditorRegistry.BPMN_SEQUENCE_FLOW)) {
			return false;
		}

		return true;
	}
}
