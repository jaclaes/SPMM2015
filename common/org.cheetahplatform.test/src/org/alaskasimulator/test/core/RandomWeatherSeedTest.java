/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Random;

import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RandomWeatherSeed;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.junit.Before;
import org.junit.Test;

public class RandomWeatherSeedTest {

	private RandomWeatherSeed randomWeatherSeed;
	private LocationProxy locationProxyA;
	private LocationProxy locationProxyB;
	private Game game;
	private GameConfig gameConfig;

	@Test
	public void assertNotNullRandom() {
		RandomWeatherSeed randomWeatherSeed = new RandomWeatherSeed();
		assertNotNull(randomWeatherSeed.getWeatherForecastRandom());
		Random random = randomWeatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyA.getLocation());
		assertNotNull(random);
		random = randomWeatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyA.getLocation());
		assertNotNull(random);
	}

	@Test
	public void getDifferentRandomForDifferentLocation() {
		Random randomA = randomWeatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyA.getLocation());
		Random randomB = randomWeatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyB.getLocation());
		assertNotSame(randomA, randomB);
	}

	@Test
	public void getSameForecastRandom() {
		Random random1 = randomWeatherSeed.getWeatherForecastRandom();
		Random random2 = randomWeatherSeed.getWeatherForecastRandom();
		assertSame(random1, random2);
	}

	@Test
	public void getSameRandomForSameLocation() {
		Random random = randomWeatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyA.getLocation());
		Random random2 = randomWeatherSeed.getLocalWeatherCharacteristicRandom(gameConfig, locationProxyA.getLocation());
		assertSame(random, random2);
	}

	@Before
	public void setUp() {
		gameConfig = new GameConfig(0, "bla", 0, 7, 900);
		Location locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics locationAWeatherCharacteristic = new LocalWeatherCharacteristics(0.5, -0.5);
		locationA.setWeatherCharacteristics(locationAWeatherCharacteristic);
		Location locationB = new Location(gameConfig, "Location B", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics locationBWeatherCharacteristic = new LocalWeatherCharacteristics(0.5, -0.5);
		locationB.setWeatherCharacteristics(locationBWeatherCharacteristic);

		game = new Game(gameConfig, "dummyPlayer");

		randomWeatherSeed = new RandomWeatherSeed();

		game.startJourney();
		locationProxyA = game.getConfig().findLocation(locationA);
		locationProxyB = game.getConfig().findLocation(locationB);
	}
}
