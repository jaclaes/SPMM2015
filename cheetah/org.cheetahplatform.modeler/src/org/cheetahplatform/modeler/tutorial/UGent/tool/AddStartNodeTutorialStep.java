package org.cheetahplatform.modeler.tutorial.UGent.tool;

import org.cheetahplatform.modeler.bpmn.StartEventDescriptor;
import org.cheetahplatform.modeler.tutorial.AbstractCreateNodeTutorialStep;

public class AddStartNodeTutorialStep extends AbstractCreateNodeTutorialStep {
	public AddStartNodeTutorialStep() {
		this(Messages.AddStartNodeTutorialStep_0);
	}

	public AddStartNodeTutorialStep(String description) {
		super(description, new StartEventDescriptor().getId());
	}

	@Override
	public String getScreencastPath() {
		return "screencasts/UGentVideo/AddStartEvent/AddStartEvent.htm"; //$NON-NLS-1$
	}
}
