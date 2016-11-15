package at.component.framework.manager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;

import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.idservice.IIdService;
import at.component.framework.services.log.IComponentLogService;

public class Activator implements BundleActivator {

	private static ServiceTracker componentServiceTracker;
	private static ServiceTracker idServiceTracker;
	private static BundleContext bundleContext;
	private static ServiceTracker loggerServiceTracker;
	private static ServiceTracker preferencesServiceTracker;
	private static ServiceTracker eventAdminServiceTracker;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static IComponentLogService getComponentLogService() {
		return (IComponentLogService) loggerServiceTracker.getService();
	}

	public static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	public static IIdService getIdService() {
		return (IIdService) idServiceTracker.getService();
	}

	public static PreferencesService getPreferencesService() {
		return (PreferencesService) preferencesServiceTracker.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		idServiceTracker = new ServiceTracker(context, IIdService.class.getName(), null);
		idServiceTracker.open();

		componentServiceTracker = new ServiceTracker(context, IComponentService.class.getName(), null);
		componentServiceTracker.open();

		preferencesServiceTracker = new ServiceTracker(context, PreferencesService.class.getName(), null);
		preferencesServiceTracker.open();

		loggerServiceTracker = new ServiceTracker(context, IComponentLogService.class.getName(), null);
		loggerServiceTracker.open();
		
		eventAdminServiceTracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
		eventAdminServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		eventAdminServiceTracker.close();
		loggerServiceTracker.close();
		preferencesServiceTracker.close();
		componentServiceTracker.close();
		idServiceTracker.close();
	}

	public static EventAdmin getEventAdmin() {
		return (EventAdmin) eventAdminServiceTracker.getService();
	}
}
