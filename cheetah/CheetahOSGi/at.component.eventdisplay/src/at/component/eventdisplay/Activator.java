package at.component.eventdisplay;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import at.component.IComponentStarterService;

public class Activator implements BundleActivator {

	private ServiceRegistration componentStarterServiceRegistration;
	private static BundleContext bundleContext;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		componentStarterServiceRegistration = context.registerService(IComponentStarterService.class.getName(), new ComponentStarterService(), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		componentStarterServiceRegistration.unregister();
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

}
