package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.bpmn.AndGatewayDescriptor;
import org.cheetahplatform.modeler.bpmn.XorGatewayDescriptor;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

public class AddGatewayTutorialStep extends AbstractTutorialStep {
	public AddGatewayTutorialStep() {
		super(Messages.AddGatewayTutorialStep_0);
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/CreateXor/CreateXor.htm"; //$NON-NLS-1$
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
