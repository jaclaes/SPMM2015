package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public class RenameChangePatternTutorialStep extends AbstractTutorialStep {
	public RenameChangePatternTutorialStep() {
		super("Rename the activity using the \"Rename Activity\" change pattern.");
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/ChangePattern/RenameActivity/RenameActivity.htm";
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RENAME);
	}
}
