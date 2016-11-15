package at.component.componentDisplay;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;

import at.component.AbstractComponent;
import at.component.IComponent;

public class Component extends AbstractComponent {

	private ComponentUiController uiController;
	private ServiceTracker componentTracker;

	@Override
	public void addUI(Composite composite) {
		uiController = new ComponentUiController(composite, this);
		componentTracker = new ServiceTracker(Activator.getBundleContext(), IComponent.class.getName(), uiController);
		componentTracker.open();
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

	@Override
	public void unregister() {
		if (uiController != null)
			componentTracker.close();
		super.unregister();
	}
}
