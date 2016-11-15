package at.component.applicationlauncher;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import at.component.AbstractComponent;

public class Component extends AbstractComponent {

	private ComponentUiController uiController;
	private ServiceRegistration serviceRegistration;

	@Override
	public void addUI(Composite composite) {
		uiController = new ComponentUiController(composite);
	}

	@Override
	public HashMap<String, String> getData() {
		if (uiController != null)
			return uiController.getData();
		return null;
	}

	@Override
	public String getName() {
		if (name == null) {
			Object bundleName = Activator.getBundleContext().getBundle().getHeaders().get(Constants.BUNDLE_NAME);

			if (bundleName != null && !bundleName.toString().trim().equals(""))
				name = bundleName.toString().trim();
			else
				name = Activator.getBundleContext().getBundle().getSymbolicName();
		}

		return name;
	}

	@Override
	public String getNameWithId() {
		return getName() + " (ID = " + getComponentId() + ")";
	}

	@Override
	public void initialize(HashMap<String, String> data) {
		if (uiController != null)
			uiController.initialize(data);
	}

	public void shutDownDescriptors() {
		if (uiController != null)
			uiController.shutDownDescriptors();
	}

	@Override
	public void unregister() {
		if (serviceRegistration != null)
			serviceRegistration.unregister();
		shutDownDescriptors();
		super.unregister();
	}
}
