package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.modeler.bpmn.EndEventDescriptor;
import org.cheetahplatform.modeler.tutorial.AbstractCreateNodeTutorialStep;

public class AddEndNodeTutorialStep extends AbstractCreateNodeTutorialStep {
	public AddEndNodeTutorialStep() {
		this(Messages.AddEndNodeTutorialStep_0);
	}

	public AddEndNodeTutorialStep(String description) {
		super(description, new EndEventDescriptor().getId());
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/AddEndEvent/AddEndEvent.htm"; //$NON-NLS-1$
	}

}
