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
		return "screencasts/ReconnectEdge/ReconnectEdge.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		return entry.getEventType().equals(AbstractGraphCommand.RECONNECT_EDGE);
	}

}
