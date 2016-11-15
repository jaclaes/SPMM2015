package at.floxx.scrumify.sprintBacklogItemList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;

/**
 * The OSGI Activator Class.
 * @author Mathias Breuss
 *
 */
public class Activator implements BundleActivator {

	private ServiceRegistration componentStarterServiceRegistration;
	private static ServiceTracker componentWireAmdinServiceTracker;
	private static BundleContext bundleContext;

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
	}

	public void stop(BundleContext context) throws Exception {
		componentWireAmdinServiceTracker.close();
		componentStarterServiceRegistration.unregister();
	}
}
