package at.component.test.util.services;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import at.component.framework.services.idservice.IIdService;
import at.component.test.Activator;

public class IdServiceTest extends Assert {
	private static ServiceTracker idServiceTracker;
	private static IIdService idService;

	@AfterClass
	public static void afterClass() throws Exception {
		idServiceTracker.close();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		idServiceTracker = new ServiceTracker(Activator.getBundleContext(), IIdService.class.getName(), null);
		idServiceTracker.open();

		idService = getIdService();
	}

	private static IIdService getIdService() {
		return (IIdService) idServiceTracker.getService();
	}

	private String projectName1 = "project1";

	private String projectName2 = "project2";

	@Test
	public void testGetId() {
		assertTrue(idService.getId(projectName1).equals("1"));
		assertTrue(idService.getId(projectName1).equals("2"));
		assertTrue(idService.getId(projectName1).equals("3"));

		assertTrue(idService.getId(projectName2).equals("1"));
		assertTrue(idService.getId(projectName2).equals("2"));

		idService.setId(projectName1, -1);
		idService.setId(projectName2, -1);
	}

	@Test
	public void testSetId() {
		assertTrue(idService.getId(projectName1).equals("1"));
		assertTrue(idService.getId(projectName1).equals("2"));
		assertTrue(idService.getId(projectName1).equals("3"));

		idService.setId(projectName1, 6);
		assertTrue(idService.getId(projectName1).equals("6"));

		idService.setId(projectName1, -1);
		assertTrue(idService.getId(projectName1).equals("1"));

		idService.setId(projectName1, -1);
	}
}
