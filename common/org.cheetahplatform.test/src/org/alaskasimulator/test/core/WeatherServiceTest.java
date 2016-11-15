/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.alaskasimulator.core.runtime.service.IWeatherService;
import org.alaskasimulator.core.runtime.service.PartialServiceState;
import org.alaskasimulator.core.runtime.service.ServicePersistence;
import org.alaskasimulator.core.runtime.service.WeatherService;
import org.junit.Test;

public class WeatherServiceTest {

	public static String unescapeXml(String escaped) {
		return escaped.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	}

	@Test
	public void persist() {
		TestGame testGame = new TestGame();
		int days = 10;
		testGame.setJourneyDuration(days);
		testGame.createGameConfig();
		Game game = testGame.createGame();
		game.startJourney();
		Location location = testGame.getLocation();
		LocationProxy locationProxy = game.getConfig().findLocation(location);

		IWeatherService service = new WeatherService(game);
		List<Double> weather = new ArrayList<Double>();

		for (int i = 0; i < days; i++) {
			double weatherForDay = service.getWeather(locationProxy, new Time(testGame.getConfig(), i, 0));
			weather.add(weatherForDay);
		}

		IWeatherService otherService = new WeatherService(game);
		List<Double> otherWeather = new ArrayList<Double>();
		boolean atLeastOneDifferent = false;

		for (int i = 0; i < days; i++) {
			double weatherForDay = otherService.getWeather(locationProxy, new Time(testGame.getConfig(), i, 0));
			otherWeather.add(weatherForDay);

			if (weatherForDay != weather.get(i)) {
				atLeastOneDifferent = true;
			}

		}

		assertTrue("Should have at least one different value (very unlikely that this is not the case).", atLeastOneDifferent);

		ServicePersistence persistence = new ServicePersistence();
		service.setPersistenceContext(persistence.createContext(IWeatherService.class));

		for (PartialServiceState state : persistence.getState()) {
			otherService.updateFrom(new PartialServiceState(IWeatherService.class, unescapeXml(state.getContent()), 0));
		}

		for (int i = 0; i < days; i++) {
			double expected = service.getWeather(locationProxy, new Time(testGame.getConfig(), i, 0));
			double actual = otherService.getWeather(locationProxy, new Time(testGame.getConfig(), i, 0));

			assertEquals("Should be the same now - restored the state.", expected, actual, 0.0);
		}

	}
}
