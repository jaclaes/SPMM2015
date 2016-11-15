/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.buildtime.constraint.JourneyCompletionDayConstraint;
import org.alaskasimulator.core.buildtime.constraint.JourneyEndLocationConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.junit.Test;

public class CompleteJourneyTest {

	private GameConfig gameConfig;
	private ActivityConfig actA1;
	private ActivityConfig actA2;
	private AccommodationConfig accA1;
	private Location locA;
	private int journeyDuration = 1000;

	private void createGameConfig() {
		gameConfig = new GameConfig(0, "gameConfig", 0, journeyDuration, 800);
		gameConfig.setBusinessValueConstraintSleepPenalty();

		locA = new Location(gameConfig, "A");

		actA1 = new ActivityConfig("ActA1", 5, 20, new Certainty(), 1.0, new DurationRange(60), ActionConfig.NOT_BOOKABLE);
		locA.addActionConfig(actA1);
		actA2 = new ActivityConfig("ActA2", 10, 20, new Certainty(), 1.0, new DurationRange(180), ActionConfig.NOT_BOOKABLE);
		locA.addActionConfig(actA2);
		accA1 = new AccommodationConfig("AccA1", 30, 100, new Certainty(), 1.0, ActionConfig.NOT_BOOKABLE);
		locA.addActionConfig(accA1);
	}

	@Test
	public void smallBoundedLocationJourney() {
		createGameConfig();
		Location locB = new Location(gameConfig, "B");
		RouteConfig routeAB = new RouteConfig("RouteAB", 2, 4, new Certainty(), 1.0, new DurationRange(50), locA, locB,
				ActionConfig.NOT_BOOKABLE);
		locA.addActionConfig(routeAB);
		locB.addActionConfig(routeAB);

		Set<Location> endLocations = new HashSet<Location>();
		endLocations.add(locB);
		gameConfig.addConstraint(new JourneyEndLocationConstraint(endLocations));
		Game game = new Game(gameConfig, "");

		game.startJourney();

		Action accomA1 = game.getConfig().findActionConfigProxy(accA1).createAction();
		Action route = game.getConfig().findActionConfigProxy(routeAB).createAction();
		accomA1.setStartTime(0, game.getDayDuration() - 1);
		game.getPlan().insertPlanItem(accomA1);
		route.setStartTime(1, 0);
		game.getPlan().insertPlanItem(route);

		assertFalse(game.isFinishable());
		accomA1.execute();
		assertFalse(game.isFinishable());
		route.execute();
		assertTrue(game.isFinishable());
		game.finishJourney();
		assertFalse(game.isFinishable());
	}

	@Test
	public void smallBoundedTimeJourney() {
		journeyDuration = 2;
		createGameConfig();
		gameConfig.addConstraint(new JourneyCompletionDayConstraint());
		Game game = new Game(gameConfig, "");

		game.startJourney();

		Action activA1 = game.getConfig().findActionConfigProxy(actA1).createAction();
		Action accomA1 = game.getConfig().findActionConfigProxy(accA1).createAction();
		activA1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activA1);
		accomA1.setStartTime(0, game.getDayDuration() - 1);
		game.getPlan().insertPlanItem(accomA1);

		assertFalse(game.isFinishable());
		activA1.execute();
		assertFalse(game.isFinishable());
		accomA1.execute();
		assertTrue(game.isFinishable());
		game.finishJourney();
		assertFalse(game.isFinishable());
	}

	@Test
	public void smallJourney() {
		createGameConfig();
		Game game = new Game(gameConfig, "");

		game.startJourney();
		assertTrue(game.isFinishable());
		game.finishJourney();
		assertFalse(game.isFinishable());
	}
}
