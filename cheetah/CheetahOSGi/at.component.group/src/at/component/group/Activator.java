package at.component.group;

import org.eclipse.swt.graphics.Point;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponent;
import at.component.IComponentStarterService;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.framework.services.log.IComponentLogService;
import at.component.group.ui.ComponentUiRunnable;
import at.component.util.EventService;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;
	private static ServiceTracker componentServiceTracker;
	private ServiceRegistration componentStarterServiceRegistration;
	private static ServiceTracker eventServiceTracker;
	private static ServiceTracker componentWireAdminTracker;
	private static ServiceTracker eventAdminTracker;
	private static ServiceTracker componentStarterServiceTracker;
	public static int dragRightClickButton;
	public static IComponent dragRightClickTargetComponent;
	public static IComponent dragRightClickSourceComponent;
	public static Point dragRightClickComponentUILocation;
	public static ComponentUiRunnable dragRightClickComponentUiRunnable;
	private static ServiceTracker componentLogServiceTracker;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static EventService getComponentEventService() {
		return (EventService) eventServiceTracker.getService();
	}

	public static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	public static ServiceReference[] getComponentStarterServices() {
		return componentStarterServiceTracker.getServiceReferences();
	}

	public static EventAdmin getEventAdmin() {
		return (EventAdmin) eventAdminTracker.getService();
	}

	public static void resetDrag() {
		dragRightClickButton = -1;
		dragRightClickComponentUILocation = null;
		dragRightClickComponentUiRunnable = null;
		dragRightClickSourceComponent = null;
		dragRightClickTargetComponent = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;

		eventAdminTracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
		eventAdminTracker.open();

		componentStarterServiceRegistration = bundleContext.registerService(IComponentStarterService.class.getName(),
				new ComponentStarterService(), null);

		componentServiceTracker = new ServiceTracker(context, IComponentService.class.getName(), null);
		componentServiceTracker.open();

		componentStarterServiceTracker = new ServiceTracker(context, IComponentStarterService.class.getName(), null);
		componentStarterServiceTracker.open();

		componentWireAdminTracker = new ServiceTracker(context, IComponentWireAdmin.class.getName(), null);
		componentWireAdminTracker.open();

		eventServiceTracker = new ServiceTracker(context, EventService.class.getName(), null);
		eventServiceTracker.open();
		
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
		eventServiceTracker.close();
		componentWireAdminTracker.close();
		componentServiceTracker.close();
		componentStarterServiceRegistration.unregister();
		eventAdminTracker.close();
	}

	public static IComponentLogService getComponentLogService() {
		return (IComponentLogService) componentLogServiceTracker.getService();
	}
}
