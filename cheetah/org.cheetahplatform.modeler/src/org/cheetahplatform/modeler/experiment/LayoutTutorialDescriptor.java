package org.cheetahplatform.modeler.experiment;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.engine.LayoutTutorialActivity;

public class LayoutTutorialDescriptor implements IExperimentActivityDescriptor {

	@Override
	public String getName() {
		return "Layout Tutorial";
	}

	@Override
	public AbstractExperimentsWorkflowActivity createActivity() {
		return new LayoutTutorialActivity();
	}

}
