package org.cheetahplatform.modeler.engine.configurations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;

public abstract class AbstractExperimentHolder extends AbstractExperimentalConfiguration {
	@Override
	public boolean allowsRecovering() {
		return true;
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		List<WorkflowConfiguration> configurations = new ArrayList<WorkflowConfiguration>();
		return configurations;
	}

	@Override
	public Map<Process, String> getInitialModelMapping() {
		return Collections.emptyMap();
	}
}
