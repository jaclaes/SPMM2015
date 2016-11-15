/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.junit.Test;

public class GameTest {
	@Test
	public void getPlayerName() throws Exception {
		TestGame testGame = new TestGame();
		testGame.createGameConfig();
		Game game = testGame.createGame();

		assertEquals("Wrong player name.", "John Doe", game.getPlayerName());
	}

	@Test
	public void maxDuration() {
		GameConfig gameConfig = new GameConfig(0, "My Game", 0, 2, 700);
		gameConfig.setBusinessValueConstraintSleepPenalty();
		Location location = new Location(gameConfig, "StartOrt");
		AccommodationConfig accommodationConfig = new AccommodationConfig("", 1, 1, new Certainty(), 1, ActionConfig.NOT_BOOKABLE);
		ActionConfig actionConfig = new ActivityConfig("my activity", 10, 10, new Certainty(), 1, new DurationRange(10),
				ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(actionConfig);

		location.addActionConfig(accommodationConfig);

		Game game = new Game(gameConfig, "name");
		game.startJourney();

		ActionConfigProxy actionConfigProxy = game.getConfig().findActionConfigProxy(actionConfig);
		Action action = actionConfigProxy.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		action.execute();

		// should work, we are on the first day
		ActionConfigProxy accommodationProxy = game.getConfig().findActionConfigProxy(accommodationConfig);
		Action accommodation = accommodationProxy.createAction();
		accommodation.setStartTime(0, game.getCurrentTime().getDayDurationInMinutes() - 1);
		game.getPlan().insertPlanItem(accommodation);
		accommodation.execute();

		// action should be also executable on the second day
		action = actionConfigProxy.createAction();
		action.setStartTime(game.getCurrentTime());
		game.getPlan().insertPlanItem(action);
		action.execute();

		accommodation = accommodationProxy.createAction();
		accommodation.setStartTime(1, game.getCurrentTime().getDayDurationInMinutes() - 1);
		game.getPlan().insertPlanItem(accommodation);

		try {
			accommodation.execute();
			fail("Should not be executable, we are on the last day.");
		} catch (RuntimeException e) {
			// should happen, end of journey reached
		}

	}

	@Test
	public void start() {
		GameConfig gameConfig = new GameConfig();
		new Location(gameConfig, "StartOrt");
		Game game = new Game(gameConfig, "John");
		assertEquals("Should be in upfront -planning phase.", Time.UPFRONT_PLANNING_DAY, game.getCurrentTime().getDay());
		game.startJourney();
		assertEquals("Should be in day 0.", 0, game.getCurrentTime().getDay());
	}

	@Test(expected = IllegalStateException.class)
	public void startTwice() {
		GameConfig gameConfig = new GameConfig();
		new Location(gameConfig, "StartOrt");
		Game game = new Game(gameConfig, "John");
		game.startJourney();
		game.startJourney();
	}
}
