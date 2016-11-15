/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.service.IService;
import org.alaskasimulator.core.runtime.service.IWeatherService;
import org.alaskasimulator.core.runtime.service.ServiceProvider;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class ServiceProviderTest {
	@Test
	public void getService() throws Exception {
		ServiceProvider provider = createService();

		IService service = provider.getService(IWeatherService.class);
		assertTrue("Wrong service.", IWeatherService.class.isAssignableFrom(service.getClass()));
	}

	private ServiceProvider createService() {
		TestGame testGame = new TestGame();
		testGame.createGameConfig();
		Game game = testGame.createGame();
		ServiceProvider provider = game.getServiceProvider();
		return provider;
	}

	@Test(expected = AssertionFailedException.class)
	public void registerServiceUnregisteredService() throws Exception {
		ServiceProvider provider = createService();
		provider.getService(IDummyService.class);
	}

	@Test(expected = AssertionFailedException.class)
	public void registerServiceRegisterServiceTwice() throws Exception {
		ServiceProvider provider = createService();

		provider.registerService(new DummyService());
		provider.registerService(new DummyService());
	}

	@Test
	public void registerService() throws Exception {
		ServiceProvider provider = createService();

		provider.registerService(new DummyService());
		IService service = provider.getService(IDummyService.class);

		assertTrue("Wrong service.", IDummyService.class.isAssignableFrom(service.getClass()));
	}

	private static interface IDummyService extends IService {
		// dummy interface
	}

	static class DummyService implements IDummyService {
		// dummy class
	}
}
