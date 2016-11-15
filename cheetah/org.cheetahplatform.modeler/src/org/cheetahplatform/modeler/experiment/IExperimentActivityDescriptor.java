package org.cheetahplatform.modeler.experiment;

import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;

public interface IExperimentActivityDescriptor {
	public String getName();
	public AbstractExperimentsWorkflowActivity createActivity();
}
