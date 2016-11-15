package at.component.tabfolder;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import at.component.AbstractComponent;

public class Component extends AbstractComponent {

	private ComponentUiController uiController;
	private ServiceRegistration serviceRegistration;

	@Override
	public void addUI(Composite composite) {
		uiController = new ComponentUiController(composite, this);

		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(EventConstants.EVENT_TOPIC, "at/component/event/*");

		serviceRegistration = Activator.getBundleContext().registerService(new String[] { EventHandler.class.getName() }, uiController,
				properties);
	}

	@Override
	public HashMap<String, String> getData() {
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
	}

	@Override
	public void unregister() {
		if (serviceRegistration != null)
			serviceRegistration.unregister();
		super.unregister();
	}
}
