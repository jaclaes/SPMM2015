package at.zugal.fitnesse.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import at.zugal.fitnesse.Activator;
import at.zugal.fitnesse.ResourceManager;

public class FitnesseTestLaunchConfigurationTab extends AbstractLaunchConfigurationTab implements ILaunchConfigurationTab {

	public static final String FITNESSE_URL = "FITNESSE_URL";
	public static final String FITNESSE_DEFAULT_URL = "http://localhost:8080";

	private FitnesseTestLaunchConfigurationTabComposite composite;

	@Override
	public void createControl(Composite parent) {
		composite = new FitnesseTestLaunchConfigurationTabComposite(parent, SWT.NONE);
		setControl(composite);
	}

	@Override
	public Image getImage() {
		return ResourceManager.getPluginImage(Activator.getDefault(), "img/FitNesseLogo.gif");
	}

	@Override
	public String getName() {
		return "Fitnesse Test";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			composite.getUrlText().setText(configuration.getAttribute(FITNESSE_URL, FITNESSE_DEFAULT_URL));
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could initialize from properties.");
			Activator.getDefault().getLog().log(status);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(FITNESSE_URL, composite.getUrlText().getText());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(FITNESSE_URL, FITNESSE_DEFAULT_URL);
	}

}
