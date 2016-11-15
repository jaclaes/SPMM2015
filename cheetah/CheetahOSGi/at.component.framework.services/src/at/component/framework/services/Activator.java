package at.component.framework.services;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponentStarterService;
import at.component.event.IEventInformation;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentservice.impl.ComponentService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.componentwireadmin.impl.ComponentWireAdmin;
import at.component.framework.services.idservice.IIdService;
import at.component.framework.services.idservice.impl.IdService;
import at.component.framework.services.log.IComponentLogService;
import at.component.framework.services.log.impl.ComponentLogService;
import at.component.framework.services.preferences.IComponentPreferencesService;
import at.component.framework.services.preferences.impl.ComponentPreferencesService;

public class Activator implements BundleActivator {

	private static ServiceTracker preferencesServiceTracker;
	private ServiceRegistration idServiceRegistration;
	private static ComponentService componentService;
	private ServiceRegistration componentServiceRegistration;
	private ServiceRegistration componentWireAdminServiceRegistration;
	private static ComponentLogService componentLogService;
	private ServiceRegistration loggerServiceRegistration;
	private ServiceRegistration componentPreferencesServiceRegistration;
	private static ComponentWireAdmin componentWireAdmin;
	private static ServiceTracker eventInformationTracker;
	private static ServiceTracker componentStarterServiceTracker;
	private static BundleContext bundleContext;
	private static ServiceTracker osgiEventAdminServiceTracker;
	private static IdService idService;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static ComponentLogService getComponentLogService() {
		return componentLogService;
	}

	public static IComponentService getComponentService() {
		return componentService;
	}

	public static ServiceReference[] getComponentStarterServices() {
		return componentStarterServiceTracker.getServiceReferences();
	}

	public static IComponentWireAdmin getComponentWireAdmin() {
		return componentWireAdmin;
	}

	public static ServiceReference[] getEventInformationServices() {
		return eventInformationTracker.getServiceReferences();
	}

	public static IIdService getIdService() {
		return idService;
	}

	public static EventAdmin getOsgiEventAdmin() {
		return (EventAdmin) osgiEventAdminServiceTracker.getService();
	}

	public static PreferencesService getPreferencesService() {
		return (PreferencesService) preferencesServiceTracker.getService();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		idService = new IdService();
		idServiceRegistration = context.registerService(IIdService.class.getName(), idService, null);

		osgiEventAdminServiceTracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
		osgiEventAdminServiceTracker.open();

		componentStarterServiceTracker = new ServiceTracker(context, IComponentStarterService.class.getName(), null);
		componentStarterServiceTracker.open();

		eventInformationTracker = new ServiceTracker(context, IEventInformation.class.getName(), null);
		eventInformationTracker.open();

		preferencesServiceTracker = new ServiceTracker(context, PreferencesService.class.getName(), null);
		preferencesServiceTracker.open();

		componentLogService = new ComponentLogService();
		loggerServiceRegistration = bundleContext.registerService(IComponentLogService.class.getName(), componentLogService, null);

		componentService = new ComponentService(context);
		componentService.open();

		componentWireAdmin = new ComponentWireAdmin();

		componentService.initialize();

		componentWireAdminServiceRegistration = bundleContext
				.registerService(IComponentWireAdmin.class.getName(), componentWireAdmin, null);

		componentServiceRegistration = bundleContext.registerService(IComponentService.class.getName(), componentService, null);
		componentPreferencesServiceRegistration = bundleContext.registerService(IComponentPreferencesService.class.getName(), new ComponentPreferencesService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		componentPreferencesServiceRegistration.unregister();
		componentServiceRegistration.unregister();
		componentWireAdminServiceRegistration.unregister();
		componentService.close();
		loggerServiceRegistration.unregister();
		componentLogService.deactivateLogger();
		preferencesServiceTracker.close();
		eventInformationTracker.close();
		componentStarterServiceTracker.close();
		osgiEventAdminServiceTracker.close();
		idServiceRegistration.unregister();
	}
}
