package at.component.calculation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.calculation.eventdefinition.EventInformation;
import at.component.event.IEventInformation;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.log.IComponentLogService;

public class Activator implements BundleActivator {

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAdminServiceTracker.getService();
	}

	private ServiceRegistration eventInformationRegistration;

	private static BundleContext bundleContext;

	private ServiceRegistration componentStarterServiceRegistration;

	private static ServiceTracker componentLogServiceTracker;

	private static ServiceTracker eventAdminServiceTracker;

	private static ServiceTracker componentWireAdminServiceTracker;

	public static EventAdmin getEventAdmin() {
		return (EventAdmin) eventAdminServiceTracker.getService();
	}

	public static IComponentLogService getLogService() {
		return (IComponentLogService) componentLogServiceTracker.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		eventInformationRegistration = context.registerService(IEventInformation.class.getName(), new EventInformation(), null);

		componentStarterServiceRegistration = context.registerService(IComponentStarterService.class.getName(),
				new ComponentStarterService(), null);

		componentWireAdminServiceTracker = new ServiceTracker(context, IComponentWireAdmin.class.getName(), null);
		componentWireAdminServiceTracker.open();

		eventAdminServiceTracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
		eventAdminServiceTracker.open();

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
		eventAdminServiceTracker.close();
		componentWireAdminServiceTracker.close();
		componentStarterServiceRegistration.unregister();
		eventInformationRegistration.unregister();
	}
}
