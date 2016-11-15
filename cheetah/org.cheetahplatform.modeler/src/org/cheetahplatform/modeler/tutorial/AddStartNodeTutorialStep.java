package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.bpmn.StartEventDescriptor;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public class AddStartNodeTutorialStep extends AbstractCreateNodeTutorialStep {
	public AddStartNodeTutorialStep() {
		this(Messages.AddStartNodeTutorialStep_0);
	}

	public AddStartNodeTutorialStep(String description) {
		super(description, new StartEventDescriptor().getId());
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/AddStartEvent/AddStartEvent.htm"; //$NON-NLS-1$
	}
}
