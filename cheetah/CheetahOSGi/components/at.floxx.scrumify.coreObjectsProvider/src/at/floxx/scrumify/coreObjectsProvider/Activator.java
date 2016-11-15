package at.floxx.scrumify.coreObjectsProvider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.floxx.scrumify.coreObjectsProvider.service.ObjectProviderService;

/**
 * The OSGI Activator Class.
 * @author Mathias Breuss
 *
 */
public class Activator implements BundleActivator {

	private ServiceRegistration componentStarterServiceRegistration;
	private static ServiceTracker eventAdminServiceTracker;
	private static ObjectProviderService objectProviderService;
	private static ServiceTracker componentWireAmdinServiceTracker;
	private static BundleContext bundleContext;
	private static ServiceRegistration obsRegistration;

	/**
	 * @return eventAdminServiceTracker
	 */
	public static EventAdmin getEventAdmin() {
		return (EventAdmin) eventAdminServiceTracker.getService();
	}
	
	/**
	 * @return objectProviderService
	 */
	public static ObjectProviderService getObjectProviderService() {
		return objectProviderService;
	}

	/**
	 * @return bundleContext
	 */
	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * @return componentWireAmdinServiceTracker
	 */
	public static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAmdinServiceTracker.getService();
	}

	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		componentStarterServiceRegistration = context.registerService(IComponentStarterService.class.getName(), new ComponentStarterService(), null);
		
		componentWireAmdinServiceTracker = new ServiceTracker(bundleContext, IComponentWireAdmin.class.getName(), null);
		componentWireAmdinServiceTracker.open();
		
		eventAdminServiceTracker = new ServiceTracker(bundleContext, EventAdmin.class.getName(), null);
		eventAdminServiceTracker.open();
		
		reregisterObjectProvider();
		
		//Get The ObjectProvider Service
		ServiceReference serviceReference = bundleContext.getServiceReference(ObjectProviderService.class.getName());
		if(serviceReference != null) {
			objectProviderService = (ObjectProviderService) bundleContext.getService(serviceReference);
		}
		else
			System.out.println("Well Service Provider is not there, at least its service reference");
		
	}

	public void stop(BundleContext context) throws Exception {
		componentWireAmdinServiceTracker.close();
		componentStarterServiceRegistration.unregister();
	}

	/**
	 * Registers the ObjectProviderService as OSGI Service.
	 */
	public static void reregisterObjectProvider() {
		if(obsRegistration != null)
			obsRegistration.unregister();
		obsRegistration = getBundleContext().registerService(ObjectProviderService.class.getName(), ObjectProviderService.getInstance(), null);
	}
}
