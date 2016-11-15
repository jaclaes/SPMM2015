/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.event.ChangeActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.Event;
import org.alaskasimulator.core.buildtime.event.NewActionConfigEvent;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.service.AvailabilityService;
import org.alaskasimulator.core.runtime.service.IAvailabilityService;
import org.alaskasimulator.core.runtime.service.IEventService;
import org.alaskasimulator.core.runtime.service.IWeatherService;
import org.alaskasimulator.core.runtime.service.PartialServiceState;
import org.alaskasimulator.core.runtime.service.ServicePersistence;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class AvailabilityServiceTest {
	private Game game;
	private ActionConfigProxy activityAProxy;
	private GameConfig gameConfig;
	private ActivityConfig activityConfigA;
	private Location locationA;

	@Test
	public void actionAvailableDueToExpiredEvent() {
		game = new Game(gameConfig, "Player");
		game.startJourney();
		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		ActivityAction action = TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();
		assertTrue(availabilityService.isActionAvailable(action));

		IEventService eventService = game.getServiceProvider().getEventService();
		eventService.createAndExecuteNewEvents();
		assertFalse(availabilityService.isActionAvailable(action));

		ActivityAction availableAction = TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertTrue(availabilityService.isActionAvailable(availableAction));
	}

	@Test
	public void actionUnavailableDueToEvent() {
		game = new Game(gameConfig, "Player");
		game.startJourney();
		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		ActivityAction action = TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();
		assertTrue(availabilityService.isActionAvailable(action));

		IEventService eventService = game.getServiceProvider().getEventService();
		eventService.createAndExecuteNewEvents();

		assertFalse(availabilityService.isActionAvailable(action));
	}

	@Test
	public void eventPersistence() {
		game = new Game(gameConfig, "Player");
		game.startJourney();
		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		ActivityAction action = TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();
		ServicePersistence servicePersistence = new ServicePersistence();
		availabilityService.setPersistenceContext(servicePersistence.createContext(IAvailabilityService.class));

		// execute some stuff on the first service
		assertTrue(availabilityService.isActionAvailable(action));
		IEventService eventService = game.getServiceProvider().getEventService();
		eventService.createAndExecuteNewEvents();
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int i = 0; i < game.getJourneyDuration(); i++) {
			ActivityAction activityAction = TestHelper.addActivityToPlan(activityAProxy, i, 0);
			actions.add(activityAction);
		}

		// put everything into another service
		AvailabilityService otherService = new AvailabilityService(game);
		for (PartialServiceState state : servicePersistence.getState()) {
			otherService.updateFrom(new PartialServiceState(IWeatherService.class, WeatherServiceTest.unescapeXml(state.getContent()), 0));
		}

		for (Action actionToCheck : actions) {
			assertEquals(availabilityService.isActionAvailable(actionToCheck), otherService.isActionAvailable(actionToCheck));
		}

	}

	@Test
	public void multipleEventsOnOneAction() {
		ActivityConfig activityConfig = new ActivityConfig("activityToTriggerEvent", 10.0, 5, new Certainty(), 1.0, new DurationRange(5),
				-1);
		locationA.addActionConfig(activityConfig);

		Event changeEvent2 = new ChangeActionConfigEvent("changeEvent2", "", "", 1.0, activityConfigA, null, null, null, 1.0,
				new DurationRange(100));
		changeEvent2.addLocationToOccur(activityConfig);
		gameConfig.addEvent(changeEvent2);

		game = new Game(gameConfig, "Player");
		game.startJourney();
		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		ActivityAction action = TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();
		IEventService eventService = game.getServiceProvider().getEventService();
		// force execution order of events
		eventService.createAndExecuteNewEvents();
		eventService.createAndExecuteNewEvents(activityConfig);

		// true because of changeEvent2
		assertTrue(availabilityService.isActionAvailable(action));

		// changeEvent2 expired -> false because of changeEvent(setUp)
		ActivityAction action2 = TestHelper.addActivityToPlan(activityAProxy, 0, 150);
		assertFalse(availabilityService.isActionAvailable(action2));

		// changeEvent expired -> good to go because of actionConfig
		ActivityAction availableAction = TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertTrue(availabilityService.isActionAvailable(availableAction));
	}

	@Test
	public void newActionEventExpires() {
		ActivityConfig activityConfig = new ActivityConfig("newActivity", 10.0, 5, new Certainty(), 1.0, new DurationRange(300), -1);

		NewActionConfigEvent event = new NewActionConfigEvent("event", "info", "", 1.0, activityConfig, locationA, new DurationRange(100));
		event.addLocationToOccur(locationA);
		gameConfig.addEvent(event);

		game = new Game(gameConfig, "Player");
		game.startJourney();
		ActionConfigProxy newlyAvailableAction = game.getConfig().findActionConfigProxy(activityConfig);
		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();
		IEventService eventService = game.getServiceProvider().getEventService();

		eventService.createAndExecuteNewEvents();

		ActivityAction action = TestHelper.addActivityToPlan(newlyAvailableAction, 0, 0);
		ActivityAction action2 = TestHelper.addActivityToPlan(newlyAvailableAction, 0, 600);

		assertTrue(availabilityService.isActionAvailable(action));
		action.execute();
		assertFalse(availabilityService.isActionAvailable(action2));
	}

	@Test
	public void newEventChangesAvailability() {
		ActivityConfig activityConfig = new ActivityConfig("activityToTriggerEvent", 10.0, 5, new Certainty(), 1.0, new DurationRange(5),
				-1);
		locationA.addActionConfig(activityConfig);

		Event changeEvent2 = new ChangeActionConfigEvent("changeEvent2", "", "", 1.0, activityConfigA, null, null, null, 1.0,
				new DurationRange(100));
		changeEvent2.addLocationToOccur(activityConfig);
		gameConfig.addEvent(changeEvent2);

		game = new Game(gameConfig, "Player");
		game.startJourney();
		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();

		IEventService eventService = game.getServiceProvider().getEventService();
		eventService.createAndExecuteNewEvents();

		ActivityAction action = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(availabilityService.isActionAvailable(action));
		eventService.createAndExecuteNewEvents(activityConfig);
		assertTrue(availabilityService.isActionAvailable(action));
	}

	@Test
	public void newEventChangesAvailabilityAndExpires() {
		ActivityConfig activityConfig = new ActivityConfig("newActivity", 10.0, 5, new Certainty(), 1.0, new DurationRange(5), -1);

		NewActionConfigEvent event = new NewActionConfigEvent("event", "info", "", 1.0, activityConfig, locationA, new DurationRange(100));
		event.addLocationToOccur(locationA);
		gameConfig.addEvent(event);

		game = new Game(gameConfig, "Player");
		game.startJourney();
		ActionConfigProxy newlyAvailableAction = game.getConfig().findActionConfigProxy(activityConfig);
		IAvailabilityService availabilityService = game.getServiceProvider().getAvailabilityService();
		IEventService eventService = game.getServiceProvider().getEventService();

		eventService.createAndExecuteNewEvents();

		ActivityAction action = TestHelper.addActivityToPlan(newlyAvailableAction, 0, 0);
		ActivityAction action2 = TestHelper.addActivityToPlan(newlyAvailableAction, 0, 200);

		assertTrue(availabilityService.isActionAvailable(action));
		assertFalse(availabilityService.isActionAvailable(action2));
	}

	@Test
	public void persist() {
		TestGame testGame = new TestGame();
		int days = 10;
		testGame.setJourneyDuration(days);
		testGame.setAvailabilityActivity1(0.5);
		testGame.createGameConfig();
		Game game = testGame.createGame();

		AvailabilityService service = new AvailabilityService(game);
		Action action = game.getConfig().findActionConfigProxy(testGame.getActivityConfig1()).createAction();
		List<Boolean> availabilities = new ArrayList<Boolean>();
		game.getServiceProvider().getEventService().createAndExecuteNewEvents();

		for (int i = 0; i < days; i++) {
			action.setStartTime(new Time(testGame.getConfig(), i, 0));
			boolean availability = service.isActionAvailable(action);
			availabilities.add(availability);
		}

		IAvailabilityService otherService = new AvailabilityService(game);
		List<Boolean> newAvailabilities = new ArrayList<Boolean>();
		for (int i = 0; i < days; i++) {
			action.setStartTime(new Time(testGame.getConfig(), i, 0));
			boolean availability = otherService.isActionAvailable(action);
			newAvailabilities.add(availability);
		}

		boolean atLeastOneDifferent = false;
		for (int i = 0; i < days; i++) {
			boolean expected = availabilities.get(i);
			boolean actual = newAvailabilities.get(i);

			atLeastOneDifferent = expected != actual;

			if (atLeastOneDifferent) {
				break;
			}
		}

		assertTrue("At least one should be different - it is very unlikely that they are the same (1/1024).", atLeastOneDifferent);

		ServicePersistence servicePersistence = new ServicePersistence();
		service.setPersistenceContext(servicePersistence.createContext(IAvailabilityService.class));

		for (PartialServiceState state : servicePersistence.getState()) {
			otherService.updateFrom(new PartialServiceState(IWeatherService.class, WeatherServiceTest.unescapeXml(state.getContent()), 0));
		}

		for (int i = 0; i < days; i++) {
			action.setStartTime(new Time(testGame.getConfig(), i, 0));
			boolean expected = service.isActionAvailable(action);
			boolean actual = otherService.isActionAvailable(action);

			assertEquals("Should be the same - the state is restored now.", expected, actual);
		}
	}

	@Before
	public void setUp() {
		gameConfig = new GameConfig(1, "config", 5, 25, 700);
		locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigA);

		Event changeEvent = new ChangeActionConfigEvent("changeEvent", "", "", 1.0, activityConfigA, null, null, null, 0.0,
				new DurationRange(200));
		gameConfig.addEvent(changeEvent);

	}
}
