package at.floxx.scrumify.amazonWebService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;

public class Activator implements BundleActivator {

	private ServiceRegistration componentStarterServiceRegistration;
	private static ServiceTracker preferencesServiceTracker;
	private static ServiceTracker componentWireAmdinServiceTracker;
	private static BundleContext bundleContext;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAmdinServiceTracker.getService();
	}

	public static PreferencesService getPreferencesService() {
		return (PreferencesService) preferencesServiceTracker.getService();
	}
	
	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		componentStarterServiceRegistration = context.registerService(IComponentStarterService.class.getName(), new ComponentStarterService(), null);
		
		componentWireAmdinServiceTracker = new ServiceTracker(bundleContext, IComponentWireAdmin.class.getName(), null);
		componentWireAmdinServiceTracker.open();

		preferencesServiceTracker = new ServiceTracker(bundleContext, PreferencesService.class.getName(), null);
		preferencesServiceTracker.open();
		
	}

	public void stop(BundleContext context) throws Exception {
		componentWireAmdinServiceTracker.close();
		preferencesServiceTracker.close();
		
		componentStarterServiceRegistration.unregister();
	}
}
