package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public class RenameNodeTutorialStep extends AbstractTutorialStep {

	public RenameNodeTutorialStep() {
		this(Messages.RenameNodeTutorialStep_0);
	}

	public RenameNodeTutorialStep(String description) {
		super(description);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/RenameNode/RenameNode.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RENAME);
	}
}
