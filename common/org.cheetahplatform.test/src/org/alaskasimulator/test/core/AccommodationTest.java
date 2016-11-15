/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.AccommodationConfigProxy;
import org.junit.Test;

public class AccommodationTest {
	private AccommodationConfig accommodationConfig;
	private Location location;
	private Game game;
	private int lastMinuteOfDay;

	private void createGame() {
		GameConfig config = new GameConfig(0, "My game", 0, 3, 959);
		config.setBusinessValueConstraintSleepPenalty();
		location = new Location(config, "Brixlegg");
		accommodationConfig = new AccommodationConfig("At home", 0, 100, new Certainty(), 1, ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(accommodationConfig);

		game = new Game(config, "John Doe");
		lastMinuteOfDay = game.getCurrentTime().getDayDurationInMinutes() - 1;
	}

	@Test
	public void executeAndCheckDay() {
		createGame();
		game.startJourney();

		AccommodationConfigProxy accommodationConfigProxy = (AccommodationConfigProxy) game.getConfig().findActionConfigProxy(
				accommodationConfig);

		Action accommodation = accommodationConfigProxy.createAction();
		assertFalse("Should not be executable, is not in the plan.", accommodation.isExecutable());
	}

	@Test
	public void tooLongJourney() {
		createGame();
		game.startJourney();

		AccommodationConfigProxy accommodationConfigProxy = (AccommodationConfigProxy) game.getConfig().findActionConfigProxy(
				accommodationConfig);

		Action action1 = accommodationConfigProxy.createAction();
		Action action2 = accommodationConfigProxy.createAction();
		Action action3 = accommodationConfigProxy.createAction();

		action1.setStartTime(0, lastMinuteOfDay);
		game.getPlan().insertPlanItem(action1);
		action2.setStartTime(1, lastMinuteOfDay);
		game.getPlan().insertPlanItem(action2);
		action3.setStartTime(2, lastMinuteOfDay);
		game.getPlan().insertPlanItem(action3);

		action1.execute();
		action2.execute();
		assertEquals(new Time(game, 2, 0), game.getCurrentTime());

		assertFalse("At the last day of the journey, no accommodation action can be executed", action3.isExecutable());
	}
}
