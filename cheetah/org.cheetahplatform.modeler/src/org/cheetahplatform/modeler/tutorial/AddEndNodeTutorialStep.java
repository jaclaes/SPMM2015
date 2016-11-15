package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.bpmn.EndEventDescriptor;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public class AddEndNodeTutorialStep extends AbstractCreateNodeTutorialStep {
	public AddEndNodeTutorialStep() {
		this(Messages.AddEndNodeTutorialStep_0);
	}

	/**
	 * @param description
	 * @param descriptorId
	 */
	public AddEndNodeTutorialStep(String description) {
		super(description, new EndEventDescriptor().getId());
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/AddEndEvent/AddEndEvent.htm"; //$NON-NLS-1$
	}

}
