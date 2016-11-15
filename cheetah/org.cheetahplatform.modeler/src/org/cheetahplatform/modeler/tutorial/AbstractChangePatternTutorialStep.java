package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.tutorial.AbstractTutorialStep;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         07.07.2010
 */
public abstract class AbstractChangePatternTutorialStep extends AbstractTutorialStep {

	private final String changePatternType;

	/**
	 * @param description
	 */
	public AbstractChangePatternTutorialStep(String description, String type) {
		super(description);
		Assert.isNotNull(type);
		this.changePatternType = type;
	}

	@Override
	public boolean isStepExecuted(AuditTrailEntry entry) {
		if (PromLogger.GROUP_EVENT_START.equals(entry.getEventType())) {
			String type = entry.getAttributeSafely(AbstractGraphCommand.CHANGE_PATTERN_TYPE);
			return this.changePatternType.equals(type);
		}
		return false;
	}

}