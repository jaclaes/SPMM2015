/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.PredictableWeatherSeed;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.alaskasimulator.core.runtime.service.IWeatherService;
import org.junit.Before;
import org.junit.Test;

public class WeatherTest {

	private GameConfig gameConfig;
	private Game game;
	private Location locationA;
	private Location locationB;

	@Test
	public void sameForecastForDifferentQueryOrder() {
		game = new Game(gameConfig, "dummyPlayer");
		game.startJourney();
		LocationProxy locationProxyAGame1 = game.getConfig().findLocation(locationA);
		IWeatherService weatherServiceGame1 = game.getServiceProvider().getWeatherService();

		LocationProxy locationProxyBGame1 = game.getConfig().findLocation(locationB);

		Game game2 = new Game(gameConfig, "ce player");
		game2.startJourney();
		IWeatherService weatherServiceGame2 = game2.getServiceProvider().getWeatherService();
		assertNotSame(weatherServiceGame1, weatherServiceGame2);
		LocationProxy locationProxyAGame2 = game2.getConfig().findLocation(locationA);
		assertNotSame(locationProxyAGame1, locationProxyAGame2);
		LocationProxy locationProxyBGame2 = game2.getConfig().findLocation(locationB);
		assertNotSame(locationProxyBGame1, locationProxyBGame2);

		for (int i = 0; i < gameConfig.getJourneyDuration(); i++) {
			double weatherAGame1 = weatherServiceGame1.getWeather(locationProxyAGame1, new Time(game, i, 0));
			double weatherBGame1 = weatherServiceGame1.getWeather(locationProxyBGame1, new Time(game, i, 0));
			double weatherBGame2 = weatherServiceGame2.getWeather(locationProxyBGame2, new Time(game2, i, 0));
			double weatherAGame2 = weatherServiceGame2.getWeather(locationProxyAGame2, new Time(game2, i, 0));
			assertEquals(Double.doubleToLongBits(weatherAGame1), Double.doubleToLongBits(weatherAGame2));
			assertEquals(Double.doubleToLongBits(weatherBGame1), Double.doubleToLongBits(weatherBGame2));
		}
	}

	@Test
	public void sameWeatherForecastForFixedConfiguration() {
		// those values were calculated and should always be the same
		Map<Integer, Double> results = new HashMap<Integer, Double>();
		results.put(0, 0.24148939775893452);
		results.put(1, 0.2890318751705644);
		results.put(2, 0.3313695178892392);
		results.put(3, 0.18216253240536848);
		results.put(4, 0.08505745831934769);
		results.put(5, 0.05092948256398795);

		GameConfig gameConfig = new GameConfig(0, "bla", 0, 7, 900);
		gameConfig.setWeatherSeed(new PredictableWeatherSeed(565541));
		Location location = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.5, -0.5);
		location.setWeatherCharacteristics(weatherCharacteristics);

		Game game = new Game(gameConfig, "dummyPlayer");

		IWeatherService weatherService = game.getServiceProvider().getWeatherService();

		game.startJourney();
		LocationProxy locProxy = game.getConfig().findLocation(location);

		for (int day = 0; day < game.getJourneyDuration() - 1; day++) {
			double weather = weatherService.getWeather(locProxy, new Time(game, day, 0));
			assertTrue("Invalid weather value: " + weather, 0 <= weather && weather <= 1.0);
			assertEquals(Double.doubleToLongBits(results.get(day)), Double.doubleToLongBits(weather));
		}
	}

	@Test
	public void sameWeatherForFixedConfiguration() {
		// those values were calculated and should always be the same
		Map<Integer, Double> results = new HashMap<Integer, Double>();
		results.put(0, 0.24148939775893452);
		results.put(1, 0.32754025636400463);
		results.put(2, 0.28312538645374064);
		results.put(3, 0.2664088989456138);
		results.put(4, 0.10172803055467278);
		results.put(5, 0.11660801615993782);

		GameConfig gameConfig = new GameConfig(0, "bla", 0, 7, 900);
		gameConfig.setWeatherSeed(new PredictableWeatherSeed(565541));
		Location location = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.5, -0.5);
		location.setWeatherCharacteristics(weatherCharacteristics);

		Game game = new Game(gameConfig, "dummyPlayer");

		IWeatherService weatherService = game.getServiceProvider().getWeatherService();

		game.startJourney();
		LocationProxy locProxy = game.getConfig().findLocation(location);

		for (int day = 0; day < game.getJourneyDuration() - 1; day++) {
			double weather = weatherService.getWeather(locProxy, new Time(game, day, 0));
			assertTrue("Invalid weather value: " + weather, 0 <= weather && weather <= 1.0);
			assertEquals(Double.doubleToLongBits(results.get(day)), Double.doubleToLongBits(weather));
			game.getCurrentTime().increaseDay(1);
		}
	}

	@Before
	public void setUp() {
		gameConfig = new GameConfig(0, "bla", 0, 7, 900);
		gameConfig.setWeatherSeed(new PredictableWeatherSeed(123456));
		locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		locationB = new Location(gameConfig, "Location B", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristicsB = new LocalWeatherCharacteristics(0.4, -0.6);
		locationB.setWeatherCharacteristics(weatherCharacteristicsB);

		game = new Game(gameConfig, "dummyPlayer");
	}

	@Test
	public void testExactWeatherBounds() {
		game.startJourney();
		LocationProxy locProxy = game.getConfig().findLocation(locationA);

		IWeatherService weatherService = game.getServiceProvider().getWeatherService();

		for (int day = 0; day < game.getJourneyDuration() - 1; day++) {
			double weather = weatherService.getWeather(locProxy, new Time(game, day, 0));
			assertTrue("Invalid weather value: " + weather, 0 <= weather && weather <= 1.0);
			game.getCurrentTime().increaseDay(1);
		}
	}

	@Test
	public void testForecastBounds() {
		game.startJourney();
		LocationProxy locProxy = game.getConfig().findLocation(locationA);

		IWeatherService weatherService = game.getServiceProvider().getWeatherService();

		for (int day = 0; day < game.getJourneyDuration(); day++) {
			double weather = weatherService.getWeather(locProxy, new Time(game, day, 0));
			assertTrue("Invalid weather value: " + weather, 0 <= weather && weather <= 1.0);
		}
	}
}
