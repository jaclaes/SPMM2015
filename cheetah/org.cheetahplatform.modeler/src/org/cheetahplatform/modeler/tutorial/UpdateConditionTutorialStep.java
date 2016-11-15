package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class UpdateConditionTutorialStep extends AbstractTutorialStep {

	/**
	 * @param description
	 * @param type
	 */
	public UpdateConditionTutorialStep() {
		super("An edge's condition can be updated by selecting it and using the \"Update Condition\" change pattern.");
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/UpdateCondition/UpdateCondition.htm";
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RENAME);
	}
}
