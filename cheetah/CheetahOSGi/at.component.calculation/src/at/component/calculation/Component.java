package at.component.calculation;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.WireConstants;

import at.component.AbstractComponent;

public class Component extends AbstractComponent {

	private ComponentUiController uiController;
	private ServiceRegistration producerServiceRegistration;

	@Override
	public void addUI(Composite composite) {
		uiController = new ComponentUiController(composite);

		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, Activator.getComponentWireAdmin().getPersistentIdOfComponent(this));
		properties.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS, new Class[] { Object.class });
		properties.put(EventConstants.EVENT_TOPIC, "at/component/event/calculation/calculationresultevent");

		producerServiceRegistration = Activator.getBundleContext()
				.registerService(new String[] { Consumer.class.getName(), Producer.class.getName(), EventHandler.class.getName() },
						uiController, properties);
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
		if (producerServiceRegistration != null)
			producerServiceRegistration.unregister();
		super.unregister();
	}
}
