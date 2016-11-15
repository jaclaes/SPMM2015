/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.TemporalActionConfig;
import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;
import org.alaskasimulator.core.buildtime.event.ChangeActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.ChangeTemporalActionConfigEvent;
import org.alaskasimulator.core.buildtime.event.Event;
import org.alaskasimulator.core.buildtime.event.NewActionConfigEvent;
import org.alaskasimulator.core.runtime.Duration;
import org.alaskasimulator.core.runtime.ExecutedEvent;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.PlanItem.State;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.TemporalActionConfigProxy;
import org.alaskasimulator.core.runtime.service.IEventService;
import org.junit.Test;

public class EventTest {
	private static final TemporalActionConfig config1 = new ActivityConfig(
			"A1", 0, 0, new Certainty(), 0, new DurationRange(0),
			ActionConfig.NOT_BOOKABLE);

	private GameConfig gameConfig;

	private Location location;

	private ActivityConfig actionConfig1;

	@Test
	public void changeActionConfigEventOccurs() {
		createConfig();

		// execute game without event
		Game game1 = new Game(gameConfig, "Bomberman");
		game1.startJourney();
		Action action1 = game1.getConfig().findActionConfigProxy(actionConfig1)
				.createAction();
		action1.setStartTime(0, 0);
		game1.getPlan().insertPlanItem(action1);
		IEventService eventService1 = game1.getServiceProvider()
				.getEventService();
		eventService1.createAndExecuteNewEvents();
		action1.execute();
		assertEquals(5.0, game1.getPlan().getExpenses(), 0.001);
		assertEquals(24.0, game1.getPlan().getActionsInPlan().get(0)
				.getBusinessValue(), 0.001);
		assertEquals(new Duration(60), game1.getPlan().getActionsInPlan()
				.get(0).getDuration());

		// now execute another game with an event
		Event changeEvent = new ChangeActionConfigEvent("changeEvent", "", 1.0,
				actionConfig1, 17.0, 27.0, new Certainty(), 1.0);
		gameConfig.addEvent(changeEvent);
		Game game2 = new Game(gameConfig, "Bomberman");
		game2.startJourney();
		action1 = game2.getConfig().findActionConfigProxy(actionConfig1)
				.createAction();
		action1.setStartTime(0, 0);
		game2.getPlan().insertPlanItem(action1);
		IEventService eventService2 = game2.getServiceProvider()
				.getEventService();
		eventService2.createAndExecuteNewEvents();
		action1.execute();
		assertEquals(17.0, game2.getPlan().getExpenses(), 0.001);
		assertEquals(27.0, game2.getPlan().getActionsInPlan().get(0)
				.getBusinessValue(), 0.001);
	}

	@Test
	public void ChangeTemporalActionConfigEvent() {
		createConfig();
		ChangeTemporalActionConfigEvent changeTemporalActionConfigEvent = new ChangeTemporalActionConfigEvent(
				"event", "short info", "description", 1.0, actionConfig1, 5.0,
				24.0, new Certainty(), 1.0, new DurationRange(30),
				new DurationRange(30, 50));
		gameConfig.addEvent(changeTemporalActionConfigEvent);

		Game game = new Game(gameConfig, "Bomberman");
		game.startJourney();

		TemporalActionConfigProxy actionConfigProxy = (TemporalActionConfigProxy) game
				.getConfig().findActionConfigProxy(actionConfig1);

		IEventService eventService = game.getServiceProvider()
				.getEventService();
		DurationRange durationRange = actionConfigProxy.getDurationRange();
		assertEquals("wrong duration", actionConfig1.getDurationRange()
				.getMinMinutes(), durationRange.getMinMinutes());
		assertEquals("wrong duration", actionConfig1.getDurationRange()
				.getMaxMinutes(), durationRange.getMaxMinutes());

		assertEquals("one event should be executed", 1, eventService
				.createAndExecuteNewEvents().size());

		durationRange = actionConfigProxy.getDurationRange();
		assertEquals("wrong duration", 30, durationRange.getMinMinutes());
		assertEquals("wrong duration", 50, durationRange.getMaxMinutes());

		game.getCurrentTime().increaseMinutes(60);

		// remove expired events
		assertTrue("no new events", eventService.createAndExecuteNewEvents()
				.isEmpty());

		durationRange = actionConfigProxy.getDurationRange();
		assertEquals("wrong duration", actionConfig1.getDurationRange()
				.getMinMinutes(), durationRange.getMinMinutes());
		assertEquals("wrong duration", actionConfig1.getDurationRange()
				.getMaxMinutes(), durationRange.getMaxMinutes());

	}

	private void createConfig() {
		gameConfig = new GameConfig(0, "Testgame", 15, 3, 1440);
		gameConfig.addConstraint(new BudgetConstraint(20));
		location = new Location(gameConfig, "Testort");
		actionConfig1 = new ActivityConfig("ActivityConfig1", 5, 24,
				new Certainty(), 1.0, new DurationRange(60),
				ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(actionConfig1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventInvalidAvailability1() {
		new ChangeActionConfigEvent("My name", "", 0.5, config1, 0.0, 1.0,
				new Certainty(), -0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventInvalidAvailability2() {
		new ChangeActionConfigEvent("My name", "", 0.5, config1, 0.0, 1.0,
				new Certainty(), 1.001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventInvalidBusinessValue() {
		new ChangeActionConfigEvent("My name", "", 0.5, config1, 0.0, -1.0,
				new Certainty(), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventInvalidCost() {
		new ChangeActionConfigEvent("My name", "", 0.5, config1, -1.0, 10.0,
				new Certainty(), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventInvalidProbability1() {
		new ChangeActionConfigEvent("My event", "", -0.1, config1, 10.0, 10.0,
				new Certainty(), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventInvalidProbability2() {
		new ChangeActionConfigEvent("My event", "", 1.1, config1, 10.0, 10.0,
				new Certainty(), 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventNullConfig() {
		new ChangeActionConfigEvent("My name", "", 0.5, null, 10.0, 10.0,
				new Certainty(), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEventNullName() {
		new ChangeActionConfigEvent(null, "", 0.5, null, 10.0, 10.0,
				new Certainty(), 0.01);
	}

	@Test
	public void eventMakesActionNonavailable() {
		createConfig();

		Event event = new ChangeActionConfigEvent("FailActionEvent", "", 1.0,
				actionConfig1, 5.0, 24.0, new Certainty(), 0.0);
		gameConfig.addEvent(event);

		Game game = new Game(gameConfig, "Bomberman");
		game.startJourney();

		Action action = game.getConfig().findActionConfigProxy(actionConfig1)
				.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);

		assertTrue("Action is available and should therefore be executable",
				action.isExecutable());
		IEventService eventService = game.getServiceProvider()
				.getEventService();
		assertTrue("One event should have occured", eventService
				.createAndExecuteNewEvents().size() == 1);

		assertFalse(
				"Event modifies availability and makes this action unavailable",
				action.isExecutable());

	}

	@Test
	public void expiredChangeConfigEvent() {
		createConfig();
		ChangeActionConfigEvent event = new ChangeActionConfigEvent(
				"FailActionEvent", "", "", 1.0, actionConfig1, 7.0, 10.0,
				new Certainty(), 0.0, new DurationRange(10));
		gameConfig.addEvent(event);

		Game game = new Game(gameConfig, "Bomberman");
		game.startJourney();

		ActionConfigProxy actionConfigProxy = game.getConfig()
				.findActionConfigProxy(actionConfig1);
		Action action = actionConfigProxy.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);

		assertTrue("Action is available and should therefore be executable",
				action.isExecutable());
		IEventService eventService = game.getServiceProvider()
				.getEventService();

		assertEquals("Should be equal", actionConfig1.getCost(),
				actionConfigProxy.getCost(), 0.001);
		assertEquals("Should be equal", actionConfig1.getMaxBusinessValue(),
				actionConfigProxy.getExpectedBusinessValue(), 0.001);
		assertEquals("Should be equal", actionConfig1.getAvailability(),
				actionConfigProxy.getAvailability(), 0.001);

		assertTrue("One event should have occured", eventService
				.createAndExecuteNewEvents().size() == 1);

		assertEquals("Should be equal", event.getCost(), actionConfigProxy
				.getCost(), 0.001);
		assertEquals("Should be equal", event.getBusinessValue(),
				actionConfigProxy.getExpectedBusinessValue(), 0.001);
		assertEquals("Should be equal", event.getAvailability(),
				actionConfigProxy.getAvailability(), 0.001);

		// increase time -> event expires
		game.getCurrentTime().increaseMinutes(20);

		// removes expired events
		assertTrue("no new events", eventService.createAndExecuteNewEvents()
				.isEmpty());

		assertEquals("Should be equal", actionConfig1.getCost(),
				actionConfigProxy.getCost(), 0.001);
		assertEquals("Should be equal", actionConfig1.getMaxBusinessValue(),
				actionConfigProxy.getExpectedBusinessValue(), 0.001);
		assertEquals("Should be equal", actionConfig1.getAvailability(),
				actionConfigProxy.getAvailability(), 0.001);
	}

	@Test
	public void expiredNewActivityEvent() {
		createConfig();
		ActivityConfig newActivityConfig = new ActivityConfig("new activity",
				5, 24, new Certainty(), 1.0, new DurationRange(60),
				ActionConfig.NOT_BOOKABLE);

		NewActionConfigEvent event = new NewActionConfigEvent("event",
				"short info", "desc", 1.0, newActivityConfig, gameConfig
						.getStartLocation(), new DurationRange(20));
		gameConfig.addEvent(event);

		Game game = new Game(gameConfig, "Bomberman");
		game.startJourney();

		IEventService eventService = game.getServiceProvider()
				.getEventService();
		assertTrue("One event should have occured", eventService
				.createAndExecuteNewEvents().size() == 1);
		ActionConfigProxy newActionConfigProxy = game.getConfig()
				.findActionConfigProxy(newActivityConfig);
		Action action = newActionConfigProxy.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		Action action2 = newActionConfigProxy.createAction();
		action2.setStartTime(0, 30);
		game.getPlan().insertPlanItem(action2);

		assertTrue("Action is available and should therefore be executable",
				action.isExecutable());

		action.execute();

		assertEquals("availability should be set to 1", Double
				.doubleToLongBits(1.0), Double
				.doubleToLongBits(newActionConfigProxy.getAvailability()));

		// remove expired events
		assertTrue("no new events", eventService.createAndExecuteNewEvents()
				.isEmpty());

		assertEquals("availability should be set to 0", Double
				.doubleToLongBits(0.0), Double
				.doubleToLongBits(newActionConfigProxy.getAvailability()));

		assertFalse(
				"Action is not available and should therefore be executable",
				action2.isExecutable());
	}

	@Test
	public void newActionConfigEventOccurs() {
		createConfig();
		ActivityConfig actionConfig2 = new ActivityConfig("ActivityConfig2", 7,
				1, new Certainty(), 1.0, new DurationRange(60),
				ActionConfig.NOT_BOOKABLE);
		NewActionConfigEvent newEvent = new NewActionConfigEvent(
				"NewActionEvent", "", 1.0, actionConfig2, location);
		gameConfig.addEvent(newEvent);

		Game game = new Game(gameConfig, "Bomberman");
		game.startJourney();
		assertTrue(game.isInTravelPhase());

		Action action1 = game.getConfig().findActionConfigProxy(actionConfig1)
				.createAction();
		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);

		// execution of action1, then occurrence of event which creates action2
		assertEquals(State.PLANNED, action1.getState());

		IEventService eventService = game.getServiceProvider()
				.getEventService();
		assertTrue("The event should be among them", eventService
				.createAndExecuteNewEvents().contains(newEvent));

		assertNotNull("ActionConfig2 should now be available", game.getConfig()
				.findActionConfigProxy(actionConfig2));

		assertTrue("No more event should occur", eventService
				.createAndExecuteNewEvents().isEmpty());
		assertTrue(eventService.getOccuredEvents().size() == 1);
		ExecutedEvent eEvent = eventService.getOccuredEvents().get(0);
		assertTrue("The correct event should be stored in the eventservice",
				eEvent.getEvent().equals(newEvent));

		action1.execute();
		Action action2 = game.getConfig().findActionConfigProxy(actionConfig2)
				.createAction();
		action2.setStartTime(0, action1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);
		action2.execute();
		assertEquals(120, game.getCurrentTime().getMinute());
		assertEquals(12.0, game.getPlan().getExpectedExpenses(), 0.001);
	}
}
