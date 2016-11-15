package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.bpmn.AndGatewayDescriptor;
import org.cheetahplatform.modeler.bpmn.XorGatewayDescriptor;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.10.2009
 */
public class AddGatewayTutorialStep extends AbstractTutorialStep {
	public AddGatewayTutorialStep() {
		super(Messages.AddGatewayTutorialStep_0);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/CreateXor/CreateXor.htm"; //$NON-NLS-1$
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (!entry.getEventType().equals(AbstractGraphCommand.CREATE_NODE)) {
			return false;
		}

		if (entry.getAttribute(AbstractGraphCommand.DESCRIPTOR).equals(new AndGatewayDescriptor().getId())) {
			return true;
		}

		if (entry.getAttribute(AbstractGraphCommand.DESCRIPTOR).equals(new XorGatewayDescriptor().getId())) {
			return true;
		}

		return false;
	}
}
