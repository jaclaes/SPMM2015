package org.cheetahplatform.modeler.experiment;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.engine.TutorialActivity;

public class BPMNTutorialDescriptor implements IExperimentActivityDescriptor {

	@Override
	public String getName() {
		return "BPMN Tutorial";
	}

	@Override
	public AbstractExperimentsWorkflowActivity createActivity() {
		return new TutorialActivity();
	}

}
