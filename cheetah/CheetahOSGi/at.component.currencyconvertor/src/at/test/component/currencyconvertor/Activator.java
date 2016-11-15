package at.test.component.currencyconvertor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import at.component.IComponentStarterService;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		bundleContext.registerService(IComponentStarterService.class.getName(), new ComponentStarterService(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
