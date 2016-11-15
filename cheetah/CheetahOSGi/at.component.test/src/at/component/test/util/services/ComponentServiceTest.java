package at.component.test.util.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.util.tracker.ServiceTracker;

import at.component.IComponent;
import at.component.event.IEventInformation;
import at.component.framework.services.componentservice.ActiveComponentException;
import at.component.framework.services.componentservice.IComponentService;
import at.component.framework.services.componentservice.IProject;
import at.component.framework.services.componentservice.ProjectConfiguration;
import at.component.framework.services.componentservice.UninstallableBundlesException;
import at.component.test.Activator;

public class ComponentServiceTest extends Assert {
	private static ServiceTracker componentServiceTracker;
	private static IComponentService componentService;

	@AfterClass
	public static void afterClass() throws Exception {
		componentServiceTracker.close();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		componentServiceTracker = new ServiceTracker(Activator.getBundleContext(), IComponentService.class.getName(), null);
		componentServiceTracker.open();

		componentService = getComponentService();
	}

	private static IComponentService getComponentService() {
		return (IComponentService) componentServiceTracker.getService();
	}

	private String adaptLocation(String location) {
		if (location == null)
			return null;

		String osgiInstallArea = Activator.getBundleContext().getProperty("osgi.install.area");

		osgiInstallArea = osgiInstallArea.substring(5, 9);

		String driveName = osgiInstallArea.substring(1, 3);

		if (location.startsWith(driveName))
			return location;

		String newLocation = location;

		if (location.contains("file:"))
			newLocation = location.substring(location.indexOf("file:") + 5);

		if (location.contains(driveName))
			return location.substring(location.indexOf(driveName));

		while (newLocation.startsWith("../"))
			newLocation = newLocation.substring(newLocation.indexOf("/") + 1);
		while (newLocation.startsWith("..\\"))
			newLocation = newLocation.substring(newLocation.indexOf("\\") + 1);
		return driveName + "/" + newLocation;
	}

	@Test
	public void testDeployProject() throws FileNotFoundException, IOException, URISyntaxException {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.deployProject(projectConfiguration);

		assertNotNull(componentService.getDeployedProject(projectConfiguration.getProjectName()));
		assertTrue(componentService.getDeployedProjects().size() == 1);

		componentService.removeDeployedProject(projectConfiguration);
	}

	@Test
	public void testGetComponent() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent = componentService.startComponent(bundle, null, projectName1);

		assertTrue(startedComponent == componentService.getComponent(projectName1, startedComponent.getComponentId()));

		IComponent startedComponent1 = componentService.startComponent(bundle, startedComponent, projectName1);

		assertFalse(startedComponent1 == componentService.getComponent(projectName1, startedComponent.getComponentId()));
		assertTrue(startedComponent1 == componentService.getComponent(projectName1, startedComponent1.getComponentId()));

		String projectName2 = "project2";

		IComponent startedComponent2 = componentService.startComponent(bundle, null, projectName2);

		assertFalse(startedComponent2 == componentService.getComponent(projectName1, startedComponent1.getComponentId()));
		assertTrue(startedComponent2 == componentService.getComponent(projectName2, startedComponent2.getComponentId()));
		assertNull(componentService.getComponent(projectName2, startedComponent1.getComponentId()));

		componentService.stopComponent(startedComponent);
		componentService.stopComponent(startedComponent2);
	}

	@Test
	public void testGetComponents() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);

		assertNull(componentService.getComponents(projectName1));

		IComponent startedComponent = componentService.startComponent(bundle, null, projectName1);

		assertTrue(componentService.getComponents(projectName1).size() == 1);
		assertTrue(startedComponent == componentService.getComponents(projectName1).get(0));

		IComponent startedComponent1 = componentService.startComponent(bundle, startedComponent, projectName1);

		assertTrue(componentService.getComponents(projectName1).size() == 2);
		assertTrue(startedComponent1 == componentService.getComponents(projectName1).get(1));

		String projectName2 = "project2";

		IComponent startedComponent2 = componentService.startComponent(bundle, null, projectName2);

		assertTrue(componentService.getComponents(projectName2).size() == 1);
		assertTrue(startedComponent2 == componentService.getComponents(projectName2).get(0));

		componentService.stopComponent(startedComponent);
		componentService.stopComponent(startedComponent2);
	}

	@Test
	public void testGetDeployedProject() throws FileNotFoundException, IOException {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.deployProject(projectConfiguration);

		assertNotNull(componentService.getDeployedProject(projectConfiguration.getProjectName()));

		componentService.removeDeployedProject(projectConfiguration);
	}

	@Test
	public void testGetDeployedProjects() throws FileNotFoundException, IOException {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.deployProject(projectConfiguration);

		assertNotNull(componentService.getDeployedProjects().size() == 1);

		assertTrue(componentService.getDeployedProjects().get(0) instanceof ProjectConfiguration);

		componentService.removeDeployedProject(projectConfiguration);

		assertNotNull(componentService.getDeployedProjects().size() == 0);
	}

	@Test
	public void testGetEventInformation() throws BundleException {
		List<Bundle> installedComponentBundles = componentService.getInstalledComponentBundles();

		Bundle bundleWithEventInformation = null;
		Bundle bundleWithOutEventInformation = null;

		for (Bundle bundle : installedComponentBundles) {
			if (bundle.getLocation().contains("calculation"))
				bundleWithEventInformation = bundle;
			if (bundle.getLocation().contains("applicationlauncher"))
				bundleWithOutEventInformation = bundle;
		}

		if (bundleWithEventInformation != null) {
			IComponent startedComponent1 = componentService.startComponent(bundleWithEventInformation, null, "projectName");
			IEventInformation eventInformation = componentService.getEventInformation(startedComponent1);

			assertNotNull(eventInformation);

			componentService.stopComponent(startedComponent1);
		} else
			fail("The Calculation-Component should have an EventInformation");

		if (bundleWithOutEventInformation != null) {
			IComponent startedComponent1 = componentService.startComponent(bundleWithOutEventInformation, null, "projectName");
			IEventInformation eventInformation = componentService.getEventInformation(startedComponent1);

			assertNull(eventInformation);

			componentService.stopComponent(startedComponent1);
		} else
			fail("The Applicationlauncher-Component should have no EventInformation");
	}

	@Test
	public void testGetInstalledComponentBundles() {
		assertTrue(componentService.getInstalledComponentBundles().size() == 8);
	}

	@Test
	public void testGetProject() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);

		assertNull(componentService.getProject(projectName1));

		IComponent startedComponent1 = componentService.startComponent(bundle, null, projectName1);

		assertNotNull(componentService.getProject(projectName1));

		String projectName2 = "project2";

		assertNull(componentService.getProject(projectName2));

		IComponent startedComponent2 = componentService.startComponent(bundle, null, projectName2);

		assertNotNull(componentService.getProject(projectName2));

		componentService.stopComponent(startedComponent1);
		componentService.stopComponent(startedComponent2);
	}

	@Test
	public void testGetProjectComponentBundles() {
		assertTrue(componentService.getInstalledProjectComponentBundles().size() == 1);
	}

	@Test
	public void testGetProjectName() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);

		IComponent startedComponent1 = componentService.startComponent(bundle, null, projectName1);

		assertTrue(componentService.getProjectName(startedComponent1) == projectName1);

		String projectName2 = "project2";

		IComponent startedComponent2 = componentService.startComponent(bundle, null, projectName2);

		assertTrue(componentService.getProjectName(startedComponent2) == projectName2);

		componentService.stopComponent(startedComponent1);
		componentService.stopComponent(startedComponent2);
	}

	@Test
	public void testInstallBundle() throws ActiveComponentException, BundleException {
		Bundle bundle = componentService.getInstalledComponentBundles().get(0);
		int numberOfInstalledComponentBundles = componentService.getInstalledComponentBundles().size();
		String location = adaptLocation(bundle.getLocation());

		componentService.uninstallBundle(bundle);

		componentService.installBundle(location);

		List<Bundle> installedComponentBundles = componentService.getInstalledComponentBundles();

		Bundle newlyInstalledComponentBundle = null;

		for (Bundle installedComponentBundle : installedComponentBundles) {
			if (adaptLocation(installedComponentBundle.getLocation()).equals(location)) {
				newlyInstalledComponentBundle = installedComponentBundle;
			}
		}

		assertNotNull(newlyInstalledComponentBundle);
		assertTrue(adaptLocation(newlyInstalledComponentBundle.getLocation()).equals(location));
		assertTrue(numberOfInstalledComponentBundles == componentService.getInstalledComponentBundles().size());
	}

	@Test
	public void testIsProjectNameInUse() throws BundleException {
		String projectName1 = "project1";

		assertFalse(componentService.isProjectNameInUse(projectName1));

		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);

		IComponent startedComponent1 = componentService.startComponent(bundle, null, projectName1);

		assertTrue(componentService.isProjectNameInUse(projectName1));

		String projectName2 = "project2";

		assertFalse(componentService.isProjectNameInUse(projectName2));

		IComponent startedComponent2 = componentService.startComponent(bundle, null, projectName2);

		assertTrue(componentService.isProjectNameInUse(projectName2));

		componentService.stopComponent(startedComponent1);
		componentService.stopComponent(startedComponent2);
	}

	@Test
	public void testLoadProject() throws Exception {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.loadProject(projectConfiguration);

		assertNotNull(componentService.getProject(projectConfiguration.getProjectName()));

		assertTrue(componentService.getComponents(projectConfiguration.getProjectName()).size() == 5);

		assertNotNull(componentService.getComponent(projectConfiguration.getProjectName(), String.valueOf(1)));
		assertNotNull(componentService.getComponent(projectConfiguration.getProjectName(), String.valueOf(2)));
		assertNotNull(componentService.getComponent(projectConfiguration.getProjectName(), String.valueOf(3)));
		assertNotNull(componentService.getComponent(projectConfiguration.getProjectName(), String.valueOf(4)));

		assertTrue(componentService.getComponents(projectConfiguration.getProjectName()).get(0).getComponentId().equals("1"));

		componentService.stopComponent(componentService.getProject(projectConfiguration.getProjectName()).getProjectComponent());
	}

	@Test
	public void testLoadProjectConfiguration() throws FileNotFoundException, IOException {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		assertNotNull(projectConfiguration);
		assertTrue(projectConfiguration.getProjectName().equals("test"));
	}

	@Test
	public void testRemoveDeployedProject() throws FileNotFoundException, IOException {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.deployProject(projectConfiguration);

		componentService.removeDeployedProject(projectConfiguration);

		assertNull(componentService.getDeployedProject(projectConfiguration.getProjectName()));
		assertTrue(componentService.getDeployedProjects().size() == 0);
	}

	@Test
	public void testSaveAndDeployProject() throws Exception {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.loadProject(projectConfiguration);

		IProject project = componentService.getProject(projectConfiguration.getProjectName());

		componentService.saveAndDeployProject(project, adaptLocation(location + "/files/projectconfiguration1.coco"));

		File file = new File(adaptLocation(location + "/files/projectconfiguration1.coco"));

		assertTrue(file.exists());
		assertTrue(file.canRead());

		assertTrue(componentService.getDeployedProjects().size() == 1);
		assertNotNull(componentService.getDeployedProject(projectConfiguration.getProjectName()));

		// clean up
		componentService.removeDeployedProject(projectConfiguration);
		file.delete();
		componentService.stopComponent(componentService.getProject(projectConfiguration.getProjectName()).getProjectComponent());
	}

	@Test
	public void testSaveProject() throws Exception {
		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.loadProject(projectConfiguration);

		IProject project = componentService.getProject(projectConfiguration.getProjectName());

		componentService.saveProject(project, adaptLocation(location + "/files/projectconfiguration1.coco"));

		File file = new File(adaptLocation(location + "/files/projectconfiguration1.coco"));

		assertTrue(file.exists());
		assertTrue(file.canRead());

		// clean up
		file.delete();
		componentService.stopComponent(componentService.getProject(projectConfiguration.getProjectName()).getProjectComponent());
	}

	@Test(expected = BundleException.class)
	public void testStartComponentBundleException1() throws BundleException {
		componentService.startComponent(null, null, null);
	}

	@Test(expected = BundleException.class)
	public void testStartComponentBundleException2() throws BundleException {
		String projectName1 = "project1";
		componentService.startComponent(null, null, projectName1);
	}

	@Test(expected = BundleException.class)
	public void testStartComponentBundleException3() throws BundleException {
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		componentService.startComponent(bundle1, null, null);
	}

	@Test(expected = BundleException.class)
	public void testStartComponentBundleException4() throws BundleException {
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		componentService.startComponent(bundle1, null, "");
	}

	@Test(expected = BundleException.class)
	public void testStartComponentBundleException5() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);

		IComponent startedComponent1 = componentService.startComponent(bundle1, null, projectName1);

		try {
			String projectName2 = "project2";
			componentService.startComponent(bundle1, startedComponent1, projectName2);
		} catch (BundleException e) {
			componentService.stopComponent(startedComponent1);
			throw e;
		}
	}

	@Test
	public void testStartDeployedProject() throws FileNotFoundException, IOException, BundleException, UninstallableBundlesException {
		Bundle bundle = componentService.getInstalledComponentBundles().get(0);

		String projectName = "project";

		IComponent projectComponent = componentService.createProject(bundle, projectName);
		IComponent childComponent1 = componentService.startComponent(bundle, projectComponent, projectName);

		String bundleLocation = Activator.getBundleContext().getBundle().getLocation();
		String location = bundleLocation.substring(bundleLocation.indexOf("file:") + 5);
		ProjectConfiguration projectConfiguration = componentService.loadProjectConfiguration(new File(adaptLocation(location
				+ "/files/projectconfiguration.coco")));

		componentService.startDeployedProject(childComponent1, projectConfiguration);

		assertTrue(childComponent1.getChildComponents().size() == 1);
		assertTrue(childComponent1.getChildComponents().get(0).getChildComponents().size() == 4);
		assertTrue(componentService.getComponents(projectName).size() == 7);

		componentService.stopComponent(projectComponent);
	}

	@Test
	public void testStartMoreComponents() throws BundleException {
		// first component
		String projectName1 = "project1";
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent1 = componentService.startComponent(bundle1, null, projectName1);

		// A project for the given projectName was created
		assertNotNull(componentService.getProject(projectName1));

		// The started and registered component was added to the project
		assertTrue(componentService.getComponents(projectName1).size() == 1);

		// The component was registered by the right bundle
		assertTrue(startedComponent1.getComponentReference().getBundle() == bundle1);

		// The idService added the right id
		assertTrue(startedComponent1.getComponentId().equals("1"));

		// Should have no parent
		assertNull(startedComponent1.getParent());

		assertTrue(startedComponent1.getChildComponents().size() == 0);

		// ----------------------------------------------------------------------------
		// second component - same bundle - same project

		IComponent startedComponent2 = componentService.startComponent(bundle1, startedComponent1, projectName1);

		// A project for the given projectName was created
		assertNotNull(componentService.getProject(projectName1));

		// The started and registered component was added to the project
		assertTrue(componentService.getComponents(projectName1).size() == 2);

		// The component was registered by the right bundle
		assertTrue(startedComponent2.getComponentReference().getBundle() == bundle1);

		// The idService added the right id
		assertTrue(startedComponent2.getComponentId().equals("2"));

		// The first component should be the parent
		assertTrue(startedComponent2.getParent() == startedComponent1);

		// Should have no childcomponents
		assertTrue(startedComponent2.getChildComponents().size() == 0);

		// startedComponent1 should have exactly one childcomponent
		assertTrue(startedComponent1.getChildComponents().size() == 1);

		// The only childComponent should be startedComponent2
		assertTrue(startedComponent1.getChildComponents().get(0) == startedComponent2);

		// ----------------------------------------------------------------------------
		// third component - same bundle - second project

		String projectName2 = "porject2";

		IComponent startedComponent3 = componentService.startComponent(bundle1, null, projectName2);

		// A project for the given projectName was created
		assertNotNull(componentService.getProject(projectName1));
		assertNotNull(componentService.getProject(projectName2));

		// The started and registered component was added to the project
		assertTrue(componentService.getComponents(projectName1).size() == 2);
		assertTrue(componentService.getComponents(projectName2).size() == 1);

		// The component was registered by the right bundle
		assertTrue(startedComponent3.getComponentReference().getBundle() == bundle1);

		// The idService added the right id
		assertTrue(startedComponent3.getComponentId().equals("1"));

		// ----------------------------------------------------------------------------
		// forth component - same bundle - first project

		IComponent startedComponent4 = componentService.startComponent(bundle1, startedComponent2, projectName1);

		// A project for the given projectName was created
		assertNotNull(componentService.getProject(projectName1));
		assertNotNull(componentService.getProject(projectName2));

		// The started and registered component was added to the project
		assertTrue(componentService.getComponents(projectName1).size() == 3);
		assertTrue(componentService.getComponents(projectName2).size() == 1);

		// startedComponent2 has one child
		assertTrue(startedComponent2.getChildComponents().size() == 1);

		// startedComponent2 is parent of startedComponent4
		assertTrue(startedComponent4.getParent() == startedComponent2);

		assertTrue(startedComponent4.getChildComponents().size() == 0);

		// The component was registered by the right bundle
		assertTrue(startedComponent4.getComponentReference().getBundle() == bundle1);

		// The idService added the right id
		assertTrue(startedComponent4.getComponentId().equals("3"));

		// second bundle
		Bundle bundle2 = componentService.getInstalledComponentBundles().get(1);

		IComponent startedComponent5 = componentService.startComponent(bundle2, startedComponent3, projectName2);

		// A project for the given projectName was created
		assertNotNull(componentService.getProject(projectName1));
		assertNotNull(componentService.getProject(projectName2));

		// The started and registered component was added to the project
		assertTrue(componentService.getComponents(projectName1).size() == 3);
		assertTrue(componentService.getComponents(projectName2).size() == 2);

		// startedComponent2 has one child
		assertTrue(startedComponent3.getChildComponents().size() == 1);

		// startedComponent2 is parent of startedComponent4
		assertTrue(startedComponent5.getParent() == startedComponent3);

		assertTrue(startedComponent5.getChildComponents().size() == 0);

		// The component was registered by the right bundle
		assertTrue(startedComponent5.getComponentReference().getBundle() == bundle2);

		// The idService added the right id
		assertTrue(startedComponent5.getComponentId().equals("2"));

		componentService.stopComponent(startedComponent1);
		componentService.stopComponent(startedComponent3);
	}

	@Test
	public void testStartOneComponent() throws BundleException {
		String projectName = "project1";
		Bundle bundle = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent = componentService.startComponent(bundle, null, projectName);

		// A project for the given projectName was created
		assertNotNull(componentService.getProject(projectName));

		// The started and registered component was added to the project
		assertTrue(componentService.getComponents(projectName).size() == 1);

		// The component was registered by the right bundle
		assertTrue(startedComponent.getComponentReference().getBundle() == bundle);

		// The idService added the right id
		assertTrue(startedComponent.getComponentId().equals("1"));

		// unregister the component
		componentService.stopComponent(startedComponent);
	}

	@Test
	public void testStopComponent() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent1 = componentService.startComponent(bundle1, null, projectName1);
		componentService.stopComponent(startedComponent1);

		// The projectComponent was stopped - therefore the project was deleted
		assertNull(componentService.getProject(projectName1));

		startedComponent1 = componentService.startComponent(bundle1, null, projectName1);

		IComponent startedComponent2 = componentService.startComponent(bundle1, startedComponent1, projectName1);

		componentService.stopComponent(startedComponent2);

		assertTrue(componentService.getComponents(projectName1).size() == 1);
		assertFalse(componentService.getComponents(projectName1).contains(startedComponent2));

		// The childComponent was deleted but not the project
		assertNotNull(componentService.getProject(projectName1));

		startedComponent2 = componentService.startComponent(bundle1, startedComponent1, projectName1);

		IComponent startedComponent3 = componentService.startComponent(bundle1, startedComponent2, projectName1);

		componentService.stopComponent(startedComponent2);

		assertTrue(componentService.getComponents(projectName1).size() == 1);
		assertFalse(componentService.getComponents(projectName1).contains(startedComponent3));
		assertFalse(componentService.getComponents(projectName1).contains(startedComponent2));

		// The childComponent was deleted but not the project
		assertNotNull(componentService.getProject(projectName1));

		componentService.stopComponent(startedComponent1);
	}

	@Test(expected = IllegalStateException.class)
	public void testStopComponentIllegalStateException1() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent1 = componentService.startComponent(bundle1, null, projectName1);
		IComponent startedComponent2 = componentService.startComponent(bundle1, startedComponent1, projectName1);

		componentService.stopComponent(startedComponent1);

		startedComponent2.getComponentReference();
	}

	@Test(expected = IllegalStateException.class)
	public void testStopComponentIllegalStateException2() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent1 = componentService.startComponent(bundle1, null, projectName1);
		@SuppressWarnings("unused")
		IComponent startedComponent2 = componentService.startComponent(bundle1, startedComponent1, projectName1);

		componentService.stopComponent(startedComponent1);

		startedComponent1.getComponentReference();
	}

	@Test(expected = IllegalStateException.class)
	public void testStopComponentIllegalStateException3() throws BundleException {
		String projectName1 = "project1";
		Bundle bundle1 = componentService.getInstalledProjectComponentBundles().get(0);
		IComponent startedComponent1 = componentService.startComponent(bundle1, null, projectName1);
		IComponent startedComponent2 = componentService.startComponent(bundle1, startedComponent1, projectName1);
		IComponent startedComponent3 = componentService.startComponent(bundle1, startedComponent2, projectName1);

		componentService.stopComponent(startedComponent2);

		try {
			startedComponent3.getComponentReference();
		} catch (Exception e) {
			componentService.stopComponent(startedComponent1);
			throw ((IllegalStateException) e);
		}
	}

	@Test
	public void testUninstallBundle() throws ActiveComponentException, BundleException {
		Bundle bundle = componentService.getInstalledComponentBundles().get(0);
		int numberOfInstalledComponentBundles = componentService.getInstalledComponentBundles().size();
		String location = bundle.getLocation();

		componentService.uninstallBundle(bundle);

		assertTrue(numberOfInstalledComponentBundles == componentService.getInstalledComponentBundles().size() + 1);

		componentService.installBundle(location);
	}
}
