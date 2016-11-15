package org.cheetahplatform.modeler.admin.basic;

import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.configuration.IConfigurator;

public class AdminConfigurator implements IConfigurator {

	@Override
	public void configure(IConfiguration configuration) {
		configuration.enableFullAdminMode();
		configuration.set(IConfiguration.DELETE_EDGES_WHEN_DELETING_NODE, true);
		configuration.set(IConfiguration.EXPERIMENT, new SingleModelingTaskConfiguration());
	}

}
