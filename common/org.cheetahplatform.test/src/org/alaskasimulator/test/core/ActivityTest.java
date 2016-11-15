/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;
import org.alaskasimulator.core.buildtime.constraint.MaxConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.PlanItem.State;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.service.IBookingService;
import org.alaskasimulator.test.fitnesse.ui.FitnesseAgileJourneys;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.agile.policy.AddAgileActionCommand;
import org.junit.Test;


public class ActivityTest {
	private ActivityConfig activityConfig;
	private Game game;
	private Location sonnwendJochLocation;
	private int budget;
	private double availability;
	private int duration;

	private final int lengthOfDay = 720;
	private Location sagzahnLocation;
	private RouteConfig singleTrailRoute;
	private ActivityConfig sagzahnActivity;

	@Test
	public void cancelBooked() {
		budget = 100;
		availability = 1;
		duration = 10;
		createGame();

		ActionConfigProxy activityConfigProxy = game.getConfig().findActionConfigProxy(activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);

		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activity);
		game.startJourney();

		activityConfig.getCancelFee().addFee(10, 0.5);

		assertEquals("Wrong account.", 10.0, game.getPlan().getExpectedExpenses(),0.001);
		activity.cancel();
		assertEquals("Wrong account.", 5.0, game.getPlan().getExpectedExpenses(),0.001);
	}

	@Test
	public void cancelNormal() {
		createExecutableGame();

		Action activity = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		assertEquals(State.NEW, activity.getState());
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);
		assertEquals(State.PLANNED, activity.getState());
		activity.cancel();
		assertEquals(State.CANCELED, activity.getState());
	}

	private void createExecutableGame() {
		budget = 100;
		availability = 1;
		duration = 10;
		createGame();
		game.startJourney();
	}

	private void createGame() {
		GameConfig config = new GameConfig(0, "My game", 0, 10, lengthOfDay);
		sonnwendJochLocation = new Location(config, "Sonnwendjoch");
		sagzahnLocation = new Location(config, "Sagzahn");

		singleTrailRoute = new RouteConfig("Singletrail", 0, 0, new Certainty(), 1, new DurationRange(0), sonnwendJochLocation,
				sagzahnLocation, ActionConfig.NOT_BOOKABLE);
		config.setStartLocation(sonnwendJochLocation);
		activityConfig = new ActivityConfig("My activity", 10, 500, new Certainty(), availability, new DurationRange(duration), 1);
		sagzahnActivity = new ActivityConfig("Sagzahn activity", 0, 1000, new Certainty(), 1.0, new DurationRange(0), 1);

		sonnwendJochLocation.addActionConfig(singleTrailRoute);
		sagzahnLocation.addActionConfig(singleTrailRoute);
		sagzahnLocation.addActionConfig(sagzahnActivity);

		sonnwendJochLocation.addActionConfig(activityConfig);
		config.addConstraint(new MaxConstraint(activityConfig, 1));
		config.addConstraint(new BudgetConstraint(budget));

		game = new Game(config, "John Doe");
	}

	@Test
	public void executeActivityMaxOccurrences() {
		budget = 100;
		availability = 1;
		duration = 10;

		createGame();
		game.startJourney();

		ActionConfigProxy activityConfigProxy = game.getConfig().findActionConfigProxy(activityConfig);
		Action activityVisit1 = activityConfigProxy.createAction();
		Action activityVisit2 = activityConfigProxy.createAction();
		activityVisit1.setStartTime(1, 0);
		game.getPlan().insertPlanItem(activityVisit1);
		activityVisit2.setStartTime(1, activityConfig.getDuration().getMinutes());
		game.getPlan().insertPlanItem(activityVisit2);

		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activityVisit1);

		game.getCurrentTime().increaseDay(1);
		activityVisit1.execute();

		assertFalse(activityVisit2.isExecutable());
	}

	@Test
	public void executeActivityPreBooked() {
		budget = 100;
		availability = 1;
		duration = 10;

		createGame();

		ActionConfigProxy activityConfigProxy = game.getConfig().findActionConfigProxy(activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);

		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activity);

		assertEquals("Wrong account.", 90.0, budget - game.getPlan().getExpectedExpenses(),0.001);
		game.startJourney();

		assertTrue("Should be executable now.", activity.isExecutable());
		assertEquals("Wrong account - activity has been prebooked", 90.0, budget - game.getPlan().getExpectedExpenses(),0.001);
	}

	@Test
	public void executeNotAvailable() {
		Failure failure = new Failure("");
		budget = 100;
		availability = 0;
		duration = 10;
		createGame();
		game.startJourney();

		ActionConfigProxy activityConfigProxy = game.getConfig().findActionConfigProxy(activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);
		assertFalse(activity.isExecutable(failure));
	}

	@Test
	public void executeOk() {
		createExecutableGame();

		ActionConfigProxy activityConfigProxy = game.getConfig().findActionConfigProxy(activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);
		assertTrue("Should be executable now.", activity.isExecutable());
		activity.execute();

		assertEquals("Wrong account.", 10.0, game.getPlan().getExpectedExpenses(),0.001);
		assertEquals("Wrong activity cost", 10.0, activity.getCost(),0.001);

		assertEquals("Wrong amount of history entries.", 1, game.getPlan().getActionsInPlan().size());
		assertEquals("Wrong business value.", 500.0, game.getPlan().getBusinessValue(),0.001);
		assertEquals("Wrong duration.", duration, game.getCurrentTime().getMinute());
	}

	@Test
	public void executeWrongLocation() {
		createExecutableGame();

		ActionConfigProxy activityConfigProxy = game.getConfig().findActionConfigProxy(sagzahnActivity);
		Action action = activityConfigProxy.createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		assertFalse("Wrong location.", action.isExecutable());
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalACost() {
		new ActivityConfig("My name", -0.001, 0, new Certainty(), -0.001, new DurationRange(10), ActionConfig.NOT_BOOKABLE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalAvailability1() {
		new ActivityConfig("My name", 0, 0, new Certainty(), 1.001, new DurationRange(10), ActionConfig.NOT_BOOKABLE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalAvailability2() {
		new ActivityConfig("My name", 0, 0, new Certainty(), -0.001, new DurationRange(10), ActionConfig.NOT_BOOKABLE);
	}

	@Test
	public void isExecutable() throws Exception {
		AgileCalendar calendar = FitnesseAgileJourneys.createSimpleJourneyToAlaska();
		calendar.startJourney();

		Game game = calendar.getGameModel().getGame();
		ActionConfigProxy shortHike = game.getConfig().findActionConfigProxy("Short Hike");
		Action action = shortHike.createAction();
		new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), action, new Time(game, 0, 0)).execute();
		assertTrue("Should be executable.", action.isExecutable());

		action = shortHike.createAction();
		new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), action, new Time(game, 0, 180)).execute();
		action.cancel();
		assertFalse("Should not be executable - final state.", action.isExecutable());

		action = shortHike.createAction();
		new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), action, new Time(game, 0, 180)).execute();
		boolean result = action.isExecutable(Failure.DUMMY, true);
		assertFalse("Should not be executable - plan ordering not obeyed.", result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void oneActionProxyTooMuch() {
		createGame();
		// creates proxies for actions of locations
		game.startJourney();

		activityConfig.createProxy(game.getConfig().findLocation(sonnwendJochLocation));
	}
}
