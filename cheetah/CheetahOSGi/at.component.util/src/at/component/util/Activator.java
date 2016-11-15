package at.component.util;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	private static ServiceTracker componentWireAdminServiceTracker;
	private static ServiceTracker componentServiceTracker;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	public static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAdminServiceTracker.getService();
	}

	private ServiceRegistration eventServiceRegistration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		componentServiceTracker = new ServiceTracker(bundleContext, IComponentService.class.getName(), null);
		componentServiceTracker.open();

		componentWireAdminServiceTracker = new ServiceTracker(bundleContext, IComponentWireAdmin.class.getName(), null);
		componentWireAdminServiceTracker.open();

		eventServiceRegistration = context.registerService(EventService.class.getName(), new EventService(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		eventServiceRegistration.unregister();
		componentWireAdminServiceTracker.close();
		componentServiceTracker.close();
	}
}
