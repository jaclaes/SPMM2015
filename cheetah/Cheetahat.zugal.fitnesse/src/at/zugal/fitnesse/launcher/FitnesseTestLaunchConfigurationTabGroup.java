package at.zugal.fitnesse.launcher;

import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class FitnesseTestLaunchConfigurationTabGroup extends org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[1];
		tabs[0] = new FitnesseTestLaunchConfigurationTab();
		setTabs(tabs);
	}

}
