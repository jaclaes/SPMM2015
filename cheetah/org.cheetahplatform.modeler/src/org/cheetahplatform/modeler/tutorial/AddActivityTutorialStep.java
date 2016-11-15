package org.cheetahplatform.modeler.tutorial;

import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         06.10.2009
 */
public class AddActivityTutorialStep extends AbstractCreateNodeTutorialStep {
	public AddActivityTutorialStep() {
		this(Messages.AddActivityTutorialStep_0);
	}

	public AddActivityTutorialStep(String description) {
		super(description, new ActivityDescriptor().getId());
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/AddActivity/AddActivity.htm"; //$NON-NLS-1$
	}

}
