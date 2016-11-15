package org.cheetahplatform.modeler.experiment;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;

public class ChangePatternTutorialDescriptor implements IExperimentActivityDescriptor {

	@Override
	public AbstractExperimentsWorkflowActivity createActivity() {
		return null; // new ChangePatternTutorialActivity();
	}

	@Override
	public String getName() {
		return "Change Pattern Tutorial";
	}

}
