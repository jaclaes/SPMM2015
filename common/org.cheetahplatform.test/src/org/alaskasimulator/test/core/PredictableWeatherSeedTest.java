/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Random;

import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.PredictableWeatherSeed;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.junit.Before;
import org.junit.Test;

public class PredictableWeatherSeedTest {

	private static final int SEED = 12345;
	private PredictableWeatherSeed weatherSeed;
	private LocationProxy locationProxyA;
	private LocationProxy locationProxyB;
	private LocationProxy locationProxyC;
	private Game game;
	private GameConfig gameConfig;
	private Location locationA;

	@Test
	public void addAddtionalLocationAfterInvokingRandomSeed() {
		gameConfig = new GameConfig(0, "bla", 0, 7, 900);
		new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));

		Location locationB = new Location(gameConfig, "Location B", new UtmCoordinate(1, 266000, 0));
		new Location(gameConfig, "Location C", new UtmCoordinate(1, 266000, 0));

		weatherSeed = new PredictableWeatherSeed(SEED);
		weatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationB);

		Location locationD = new Location(gameConfig, "Location D");

		game = new Game(gameConfig, "dummyPlayer");

		Random localWeatherCharacteristicRandom = weatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationD);
		assertNotNull(localWeatherCharacteristicRandom);
	}

	@Test
	public void getDifferentRandomForDifferentLocation() {
		Random randomA = weatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyA.getLocation());
		Random randomB = weatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyB.getLocation());
		assertNotSame(randomA, randomB);
	}

	@Test
	public void getEqualForecastRandomForDifferentGames() {
		Random forecastRandom1 = game.getConfig().getConfig().getWeatherSeed().getWeatherForecastRandom();
		Game game2 = new Game(gameConfig, "player");
		Random forecastRandom2 = game2.getConfig().getConfig().getWeatherSeed().getWeatherForecastRandom();
		assertNotSame(forecastRandom1, forecastRandom2);
		for (int i = 0; i < 10; i++) {
			assertEquals(forecastRandom1.nextInt(), forecastRandom2.nextInt());
		}
	}

	@Test
	public void getEqualForecastRandomForDifferentWeatherSeeds() {
		PredictableWeatherSeed weatherSeed2 = new PredictableWeatherSeed(12345);
		Random weatherForecastRandom = weatherSeed.getWeatherForecastRandom();
		Random weatherForecastRandom2 = weatherSeed2.getWeatherForecastRandom();
		for (int i = 0; i < 10; i++) {
			assertEquals(weatherForecastRandom.nextInt(), weatherForecastRandom2.nextInt());
		}
	}

	@Test
	public void getEqualLocationRandomForDifferentWeatherSeeds() {
		PredictableWeatherSeed weatherSeed2 = new PredictableWeatherSeed(SEED);
		Random locationRandom = weatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyC.getLocation());
		Random locationRandom2 = weatherSeed2.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyC.getLocation());
		for (int i = 0; i < 10; i++) {
			assertEquals(locationRandom.nextInt(), locationRandom2.nextInt());
		}
	}

	@Test
	public void getEqualLocationRandomsForDifferentGames() {
		Random locationRandom1 = game.getConfig().getConfig().getWeatherSeed().getLocalWeatherCharacteristicRandom(gameConfig,
				locationProxyA.getLocation());
		Game game2 = new Game(gameConfig, "player");
		LocationProxy locationProxyA = game2.getConfig().findLocation(locationA);
		Random locationRandom2 = game2.getConfig().getConfig().getWeatherSeed().getLocalWeatherCharacteristicRandom(gameConfig,
				locationProxyA.getLocation());
		assertNotSame(locationRandom1, locationRandom2);
		for (int i = 0; i < 10; i++) {
			assertEquals(locationRandom1.nextInt(), locationRandom2.nextInt());
		}
	}

	@Before
	public void setUp() {
		gameConfig = new GameConfig(0, "bla", 0, 7, 900);
		locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics locationAWeatherCharacteristic = new LocalWeatherCharacteristics(0.5, -0.5);
		locationA.setWeatherCharacteristics(locationAWeatherCharacteristic);
		Location locationB = new Location(gameConfig, "Location B", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics locationBWeatherCharacteristic = new LocalWeatherCharacteristics(0.5, -0.5);
		locationB.setWeatherCharacteristics(locationBWeatherCharacteristic);
		Location locationC = new Location(gameConfig, "Location B", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics locationCWeahterCharacteristic = new LocalWeatherCharacteristics(0.5, -0.5);
		locationC.setWeatherCharacteristics(locationCWeahterCharacteristic);

		game = new Game(gameConfig, "dummyPlayer");

		weatherSeed = new PredictableWeatherSeed(SEED);
		gameConfig.setWeatherSeed(weatherSeed);

		game.startJourney();
		locationProxyA = game.getConfig().findLocation(locationA);
		locationProxyB = game.getConfig().findLocation(locationB);
		locationProxyC = game.getConfig().findLocation(locationC);
	}
}
