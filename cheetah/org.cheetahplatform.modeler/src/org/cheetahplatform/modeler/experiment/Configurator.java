package org.cheetahplatform.modeler.experiment;

import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.configuration.IConfigurator;

public class Configurator implements IConfigurator {

	@Override
	public void configure(IConfiguration configuration) {
		configuration.enableModelerMode();
		configuration.set(IConfiguration.EXPERIMENT, new Experiment());
	}

}
