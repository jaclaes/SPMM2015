/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import java.util.List;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.event.ChangeActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.ChangeTemporalActionConfigEvent;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.ActivityConfigProxy;
import org.alaskasimulator.ui.model.ChangedValuesInformationProvider;
import org.cheetahplatform.test.TestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangeValuesInformationProviderTest {
	// Added as internationalization introduces different texts
	private static void checkResult(String actual, String... possibleResults) {
		for (String string : possibleResults) {
			if (string.equals(actual)) {
				return;
			}
		}

		Assert.fail("Unexpected result: " + actual);
	}

	private ActivityConfig activityConfigA;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActivityAction action;
	private GameConfig gameConfig;
	private Location locationA;

	private ActivityConfig activityConfigB;

	private ActivityAction addToPlan(ActionConfigProxy proxy, int day, int minutes) {
		ActivityAction action = new ActivityAction((ActivityConfigProxy) proxy);
		action.setStartTime(day, minutes);
		game.getPlan().insertPlanItem(action);
		return action;
	}

	@Test
	public void changedAvailability() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigB, null, null, null,
				1.0, null, null);
		event.addLocationToOccur(activityConfigB);
		gameConfig.addEvent(event);

		game = new Game(gameConfig, "player");
		game.startJourney();

		ActionConfigProxy activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		action = addToPlan(activityBProxy, 0, 0);
		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();

		checkResult(information.get(0), "Due to an event the availability of the action was changed.",
				"Aufgrund eines Events wurde(n) die Verfügbarkeit der Aktion verändert.");
	}

	@Test
	public void changedBusinessValue() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigA, null, 1000.0,
				null, null, new DurationRange(50), null);
		event.addLocationToOccur(activityConfigA);

		gameConfig.addEvent(event);

		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		action = addToPlan(activityAProxy, 0, 0);

		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();
		checkResult(information.get(0), "Aufgrund eines Events wurde(n) die Punkte der Aktion verändert.",
				"Due to an event the business value of the action was changed.");
	}

	@Test
	public void changedCertainty() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigA, null, null,
				new Certainty(0.75), null, null, null);
		event.addLocationToOccur(activityConfigA);
		gameConfig.addEvent(event);
		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		action = addToPlan(activityAProxy, 0, 0);
		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();
		checkResult(information.get(0), "Aufgrund eines Events wurde(n) die Verlässlichkeit der Aktion verändert.",
				"Due to an event the certainty of the action was changed.");
	}

	@Test
	public void changedCost() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigA, 1100.0, null,
				null, null, null, null);
		event.addLocationToOccur(activityConfigA);

		gameConfig.addEvent(event);

		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		action = addToPlan(activityAProxy, 0, 0);
		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();
		checkResult(information.get(0), "Aufgrund eines Events wurde(n) die Kosten der Aktion verändert.",
				"Due to an event the cost of the action was changed.");
	}

	@Test
	public void changedDuration() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigA, null, null, null,
				null, null, new DurationRange(200));
		event.addLocationToOccur(activityConfigA);

		gameConfig.addEvent(event);

		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		action = addToPlan(activityAProxy, 0, 0);
		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();
		checkResult(information.get(0), "Aufgrund eines Events wurde(n) die Dauer der Aktion verändert.",
				"Due to an event the duration of the action was changed.");
	}

	@Test
	public void changedThreeValues() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigA, 1000.0, 1000.0,
				null, null, null, new DurationRange(300));
		event.addLocationToOccur(activityConfigA);

		gameConfig.addEvent(event);

		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		action = addToPlan(activityAProxy, 0, 0);
		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();
		checkResult(information.get(0), "Aufgrund eines Events wurde(n) die Punkte, die Kosten und die Dauer der Aktion verändert.",
				"Due to an event the duration, cost and business value of the action were changed.");
	}

	@Test
	public void changedTwoValues() {
		ChangeActionConfigEvent event = new ChangeTemporalActionConfigEvent("name", "info", "desc", 1.0, activityConfigA, 1000.0, 1000.0,
				null, null, null, null);
		event.addLocationToOccur(activityConfigA);

		gameConfig.addEvent(event);

		game = new Game(gameConfig, "player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		action = addToPlan(activityAProxy, 0, 0);
		action.execute();

		ChangedValuesInformationProvider changedValuesInformationProvider = new ChangedValuesInformationProvider(game, action);
		List<String> information = changedValuesInformationProvider.getInformation();
		checkResult(information.get(0), "Aufgrund eines Events wurde(n) die Punkte und die Kosten der Aktion verändert.",
				"Due to an event the cost and business value of the action were changed.");
	}

	@Before
	public void setUp() {
		TestHelper.setLocaleToEnglish();
		gameConfig = new GameConfig(1, "ResponseTestConfig", 5, 5, 700);
		locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigA);
		activityConfigB = new ActivityConfig("Activity B", 10.0, 100, new Certainty(1.0), 0.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigB);
	}
}
