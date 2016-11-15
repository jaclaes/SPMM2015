package at.component.display;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.log.IComponentLogService;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	private static ServiceTracker componentWireAdminServiceTracker;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAdminServiceTracker.getService();
	}

	public static IComponentLogService getLogService() {
		return (IComponentLogService) componentLogServiceTracker.getService();
	}

	private ServiceRegistration componentStarterServiceRegistration;

	private static ServiceTracker componentLogServiceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		componentStarterServiceRegistration = bundleContext.registerService(IComponentStarterService.class.getName(),
				new ComponentStarterService(), null);

		componentWireAdminServiceTracker = new ServiceTracker(context, IComponentWireAdmin.class.getName(), null);
		componentWireAdminServiceTracker.open();

		componentLogServiceTracker = new ServiceTracker(context, IComponentLogService.class.getName(), null);
		componentLogServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		componentLogServiceTracker.close();
		componentStarterServiceRegistration.unregister();
	}
}
