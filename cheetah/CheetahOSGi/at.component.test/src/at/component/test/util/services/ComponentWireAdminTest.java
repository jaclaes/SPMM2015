package at.component.test.util.services;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponent;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentwireadmin.IComponentWireAdmin;
import at.component.test.Activator;

public class ComponentWireAdminTest extends Assert {
	private static ServiceTracker componentWireAdminServiceTracker;
	private static IComponentWireAdmin componentWireAdmin;
	private static ServiceTracker componentServiceTracker;
	private static IComponentService componentService;

	@AfterClass
	public static void afterClass() throws Exception {
		componentWireAdminServiceTracker.close();
		componentServiceTracker.close();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		componentServiceTracker = new ServiceTracker(Activator.getBundleContext(), IComponentService.class.getName(), null);
		componentServiceTracker.open();

		componentService = getComponentService();

		componentWireAdminServiceTracker = new ServiceTracker(Activator.getBundleContext(), IComponentWireAdmin.class.getName(), null);
		componentWireAdminServiceTracker.open();

		componentWireAdmin = getComponentWireAdmin();
	}

	private static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	private static IComponentWireAdmin getComponentWireAdmin() {
		return (IComponentWireAdmin) componentWireAdminServiceTracker.getService();
	}

	@Test
	public void testGetComponentPid() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent = componentService.createProject(bundle, projectName1);
		IComponent startedComponent1 = componentService.startComponent(bundle, startedComponent, projectName1);

		assertEquals(componentWireAdmin.getPersistentIdOfComponent(startedComponent), projectName1 + "?"
				+ startedComponent.getComponentId());
		assertEquals(componentWireAdmin.getPersistentIdOfComponent(startedComponent1), projectName1 + "?"
				+ startedComponent1.getComponentId());

		String projectName2 = "project2";

		IComponent startedComponent2 = componentService.startComponent(bundle, null, projectName2);

		assertEquals(componentWireAdmin.getPersistentIdOfComponent(startedComponent2), projectName2 + "?"
				+ startedComponent2.getComponentId());

		componentService.stopComponent(startedComponent);
		componentService.stopComponent(startedComponent2);
	}
}
