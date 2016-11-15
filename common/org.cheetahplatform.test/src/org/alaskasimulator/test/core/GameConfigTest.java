/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.IWeatherSeed;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RandomWeatherSeed;
import org.alaskasimulator.core.buildtime.constraint.Constraint;
import org.alaskasimulator.core.buildtime.constraint.JourneyEndLocationConstraint;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class GameConfigTest {
	@Test(expected = IllegalArgumentException.class)
	public void constructorIllegalInvalidBookingDuration() {
		new GameConfig(0, "Soso", -1, 10, 123);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorIllegalInvalidJourneyDuration() {
		new GameConfig(0, "My config", 1, 0, 456);
	}

	@Test
	public void getDefaultWeatherSeed() {
		GameConfig gameConfig = new GameConfig(0, "Soso", 1, 10, 123);
		IWeatherSeed weatherSeed = gameConfig.getWeatherSeed();
		assertTrue(weatherSeed instanceof RandomWeatherSeed);
	}

	@Test
	public void getEndLocationNoConstraint() {
		TestGame testGame = new TestGame();

		testGame.createGameConfig();
		testGame.createGame();

		Set<Location> result = testGame.getGame().getConfig().getEndlocations();
		assertEquals("Should get two endlocations.", 2, result.size());
		assertTrue("Should get this endlocation.", result.contains(testGame.getLocation()));
		assertTrue("Should get this endlocation.", result.contains(testGame.getLocation2()));
	}

	@Test
	public void getEndLocations() {
		TestGame testGame = new TestGame();

		testGame.createGameConfig();
		Set<Location> locations = new HashSet<Location>();
		locations.add(testGame.getLocation());
		locations.add(testGame.getLocation2());
		Constraint constraint = new JourneyEndLocationConstraint(locations);
		testGame.getConfig().addConstraint(constraint);
		testGame.createGame();

		Set<Location> result = testGame.getGame().getConfig().getEndlocations();
		assertEquals("Should get two endlocations.", 2, result.size());
		assertTrue("Should get this endlocation.", result.contains(testGame.getLocation()));
		assertTrue("Should get this endlocation.", result.contains(testGame.getLocation2()));
	}

	@Test
	public void validateLegal() {
		GameConfig config = new GameConfig();
		Location location = new Location(config, "My location");
		ActivityConfig activity1 = new ActivityConfig("Name 1", 0, 0, new Certainty(), 0, new DurationRange(0), ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(activity1);

		config.validate();
	}

	@Test(expected = AssertionFailedException.class)
	public void validateUsedNameTwice() {
		GameConfig config = new GameConfig();
		Location location = new Location(config, "My location");
		ActivityConfig activity1 = new ActivityConfig("Name 1", 0, 0, new Certainty(), 0, new DurationRange(0), ActionConfig.NOT_BOOKABLE);
		ActivityConfig activity2 = new ActivityConfig("Name 1", 0, 0, new Certainty(), 0, new DurationRange(0), ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(activity1);
		location.addActionConfig(activity2);

		config.validate();
	}
}
