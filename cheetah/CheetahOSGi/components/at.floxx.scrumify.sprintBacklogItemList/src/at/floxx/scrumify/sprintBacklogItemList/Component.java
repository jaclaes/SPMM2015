package at.floxx.scrumify.sprintBacklogItemList;

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
import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;

/**The Component Class, containing WireAdmin and Event Filter Properties.
 * @author Mathias Breuss
 *
 */
public class Component extends AbstractComponent {

	private ComponentUiController uiController;
	private ServiceRegistration serviceRegistration;

	@Override
	public void addUI(Composite composite) {
		uiController = new ComponentUiController(composite, this);
		
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, Activator.getComponentWireAdmin().getPersistentIdOfComponent(this));
		properties.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS, new Class[] { SprintBacklogItem.class });
		properties.put(WireConstants.WIREADMIN_CONSUMER_FLAVORS, new Class[] { ProductBacklogItem.class, SprintBacklogItem.class });
		properties.put(EventConstants.EVENT_TOPIC, new String[] {"at/floxx/scrumify/sprintBacklogItemEditForm/*", "at/floxx/scrumify/coreObjectProvider/*"});
		
		serviceRegistration = Activator.getBundleContext()
			.registerService(new String[] { Consumer.class.getName(), Producer.class.getName(), EventHandler.class.getName() },
				uiController, properties);
	}

	@Override
	public String getName() {
		Object bundleName = Activator.getBundleContext().getBundle().getHeaders().get(Constants.BUNDLE_NAME);
		
		if (bundleName != null && !bundleName.toString().trim().equals(""))
			return bundleName.toString().trim();
		
		return Activator.getBundleContext().getBundle().getSymbolicName();
	}

	@Override
	public String getNameWithId() {
		return getName() + " (ID = " + getComponentId() + ")";
	}

	@Override
	public HashMap<String, String> getData() {
		if (uiController != null)
			return uiController.getData();
		
		return null;
	}

	@Override
	public void initialize(HashMap<String, String> data) {
		if (uiController != null)
			uiController.initialize(data);
	}

	@Override
	public void unregister() {
		if (serviceRegistration != null)
			serviceRegistration.unregister();
		super.unregister();
	}
}
