package org.cheetahplatform.literatemodeling;

import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.modeler.engine.configurations.IExperimentConfiguration;

public interface ILiterateExperimentConfiguration extends IExperimentConfiguration {
	public Map<Process, LiterateInitialValues> getInitialLiterateModelMapping();
}
