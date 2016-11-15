package org.cheetahplatform.demoreplayer;

import org.cheetahplatform.modeler.admin.basic.SingleModelingTaskConfiguration;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.configuration.IConfigurator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;

public class DemoReplayerConfiguration implements IConfigurator {

	@Override
	public void configure(IConfiguration configuration) {
		configuration.set(IConfiguration.EXPERIMENT,
				new SingleModelingTaskConfiguration());
		configuration.set(IConfiguration.SHOW_MENU_BAR, true);
		configuration.set(IConfiguration.INITIAL_ACTIVITIY_SIZE, new Point(120,
				50));
		configuration.set(IConfiguration.INITIAL_GATEWAY_SIZE,
				new Point(30, 30));
		configuration.set(IConfiguration.INITIAL_EVENT_SIZE, new Point(24, 24));
		configuration.set(IConfiguration.SHELL_STYLE, SWT.SHELL_TRIM);
		configuration.set(IConfiguration.SHOW_TOOL_BAR, false);
		configuration.set(IConfiguration.DEMO_REPLAY, true);
	}
}
