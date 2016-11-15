package at.component.applicationlauncher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.framework.services.componentservice.IComponentService;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	private static ServiceTracker componentServiceTracker;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		bundleContext.registerService(IComponentStarterService.class.getName(), new ComponentStarterService(), null);

		componentServiceTracker = new ServiceTracker(bundleContext, IComponentService.class.getName(), null);
		componentServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		componentServiceTracker.close();
	}
}
