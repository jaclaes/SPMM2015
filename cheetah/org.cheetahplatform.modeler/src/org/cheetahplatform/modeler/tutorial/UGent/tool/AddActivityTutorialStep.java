package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.tutorial.AbstractCreateNodeTutorialStep;

public class AddActivityTutorialStep extends AbstractCreateNodeTutorialStep {
	public AddActivityTutorialStep() {
		this(Messages.AddActivityTutorialStep_0);
	}

	public AddActivityTutorialStep(String description) {
		super(description, new ActivityDescriptor().getId());
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/AddActivity/AddActivity.htm"; //$NON-NLS-1$
	}

}
