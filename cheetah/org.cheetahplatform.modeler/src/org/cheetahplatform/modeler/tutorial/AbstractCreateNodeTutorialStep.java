package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.eclipse.core.runtime.Assert;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public abstract class AbstractCreateNodeTutorialStep extends AbstractTutorialStep {

	private final String descriptorId;

	public AbstractCreateNodeTutorialStep(String description, String descriptorId) {
		super(description);
		Assert.isNotNull(descriptorId);
		this.descriptorId = descriptorId;
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (entry.getEventType().equals(AbstractGraphCommand.CREATE_NODE)) {
			if (entry.getAttribute(AbstractGraphCommand.DESCRIPTOR).equals(descriptorId))
				return true;
		}
		return false;
	}

}
