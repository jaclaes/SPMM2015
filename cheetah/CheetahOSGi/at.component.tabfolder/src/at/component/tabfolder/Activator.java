package at.component.tabfolder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.log.IComponentLogService;

public class Activator implements BundleActivator {

	private ServiceRegistration componentStarterServiceRegistration;
	private static ServiceTracker componentServiceTracker;
	private static ServiceTracker componentLogServiceTracker;
	private static ServiceTracker componentWireAmdinServiceTracker;
	private static BundleContext bundleContext;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentLogService getComponentLogService() {
		return (IComponentLogService) componentLogServiceTracker.getService();
	}

	public static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	public static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAmdinServiceTracker.getService();
	}

	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		componentStarterServiceRegistration = context.registerService(IComponentStarterService.class.getName(),
				new ComponentStarterService(), null);

		componentWireAmdinServiceTracker = new ServiceTracker(bundleContext, IComponentWireAdmin.class.getName(), null);
		componentWireAmdinServiceTracker.open();

		componentLogServiceTracker = new ServiceTracker(bundleContext, IComponentLogService.class.getName(), null);
		componentLogServiceTracker.open();

		componentServiceTracker = new ServiceTracker(bundleContext, IComponentService.class.getName(), null);
		componentServiceTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		componentServiceTracker.close();
		componentLogServiceTracker.close();
		componentWireAmdinServiceTracker.close();
		componentStarterServiceRegistration.unregister();
	}
}
