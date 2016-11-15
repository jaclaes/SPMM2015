/*******************************************************************************
* This file is provided under the terms of the Eclipse Public License 1.0. 
* Any use, reproduction or distribution of the program
* constitutes recipient's acceptance of this agreement.
*******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.BoxConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LateModelingBoxConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.PlanItemConfig;
import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;
import org.alaskasimulator.core.runtime.Duration;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Plan;
import org.alaskasimulator.core.runtime.PlanItem;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.box.Box;
import org.alaskasimulator.core.runtime.box.LateModelingBox;
import org.alaskasimulator.core.runtime.proxy.ActivityConfigProxy;
import org.alaskasimulator.core.runtime.service.IBookingService;
import org.junit.Test;

public class PlanTest
{
	private Game game;
	private ActivityConfig activityConfig;
	private int budget = 1000;
	private int deadLine = 1;
	private AccommodationConfig accommodationConfig;
	private BoxConfig boxConfig;

	@Test
	public void doNotNeedToPayForBookedActions()
	{
		createGame();

		Plan plan = game.getPlan();
		ActivityConfigProxy activityConfigProxy = (ActivityConfigProxy) game.getConfig().findActionConfigProxy(
				activityConfig);
		Action activityVisit1 = activityConfigProxy.createAction();
		Action activityVisit2 = activityConfigProxy.createAction();
		activityVisit1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityVisit1);
		activityVisit2.setStartTime(0, activityConfigProxy.getDurationRange().getMaxMinutes());
		game.getPlan().insertPlanItem(activityVisit2);
		
		assertTrue(activityVisit1.isBookable());
		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activityVisit1);
		game.startJourney();
		assertEquals("Wrong account.", 10.0, plan.getExpenses(),0.001);
		assertTrue(activityVisit1.isExecutable());
		activityVisit1.execute();
		assertFalse(activityVisit1.isExecutable());
		assertEquals("Wrong account.", 10.0, plan.getExpenses(),0.001);
		assertTrue(activityVisit2.isExecutable());
		activityVisit2.execute();
		assertFalse(activityVisit2.isExecutable());
		assertEquals("Wrong account.", 20.0, plan.getExpenses(),0.001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void decreaseIllegal()
	{
		budget = 1;
		createGame();
		game.startJourney();

		ActivityConfigProxy activityConfigProxy = (ActivityConfigProxy) game.getConfig().findActionConfigProxy(
				activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);
		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activity);
	}

	@Test(expected = IllegalArgumentException.class)
	public void bookTooLate()
	{
		deadLine = 1;
		createGame();
		game.startJourney();

		ActivityConfigProxy activityConfigProxy = (ActivityConfigProxy) game.getConfig().findActionConfigProxy(
				activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);
		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activity);
	}

	@Test(expected = IllegalArgumentException.class)
	public void bookNotBookable()
	{
		deadLine = ActionConfig.NOT_BOOKABLE;
		createGame();

		ActivityConfigProxy activityConfigProxy = (ActivityConfigProxy) game.getConfig().findActionConfigProxy(
				activityConfig);
		Action activity = activityConfigProxy.createAction();
		activity.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity);
		IBookingService bookingService = game.getServiceProvider().getBookingService();
		bookingService.createBooking(activity);
	}

	@Test
	public void obeyOrdering()
	{
		createGame();
		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		action2.setStartTime(0, activityConfig.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);

		assertFalse("Wrong ordering", action2.isExecutable());
		action1.execute();
		action2.execute();
	}

	@Test
	public void movePlanItem()
	{
		createGame();
		game.startJourney();

		Action activity1 = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		Action activity2 = game.getConfig().findActionConfigProxy(activityConfig).createAction();

		activity1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity1);
		activity2.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activity2);

		List<PlanItem> planItems = game.getPlan().getPlanItems();
		assertSame("Wrong ordering.", activity1, planItems.get(0));
		assertSame("Wrong ordering.", activity2, planItems.get(1));

		activity1.setStartTime(0,3);
		game.getPlan().movePlanItem(activity1);

		planItems = game.getPlan().getPlanItems();
		assertSame("Wrong ordering.", activity1, planItems.get(1));
		assertSame("Wrong ordering.", activity2, planItems.get(0));
		
		game.getPlan().movePlanItem(activity2);

		planItems = game.getPlan().getPlanItems();
		assertSame("Wrong ordering.", activity1, planItems.get(1));
		assertSame("Wrong ordering.", activity2, planItems.get(0));
	}

	@Test
	public void removeBoxAroundContent()
	{
		createGame();
		
		PlanItem action1 = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		
		Box box = (Box)game.getConfig().findBoxConfig("LateModelingBox").create(game);
		box.setStartTime(0, 0);
		game.getPlan().insertPlanItem(box);
		
		PlanItem action2 = game.getConfig().findActionConfigProxy(activityConfig).createAction();
		action2.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action2);
		
		List<PlanItemConfig> boxContent = new ArrayList<PlanItemConfig>();
		boxContent.add(accommodationConfig);
		boxContent.add(accommodationConfig);
		((LateModelingBox)box).select(boxContent);
		
		assertEquals(3, game.getPlan().getPlanItems().size());
		
		game.getPlan().removeBoxAroundContent(box);
		
		assertEquals(4, game.getPlan().getPlanItems().size());
		assertEquals("Checking Plan-Order", action1, game.getPlan().getPlanItems().get(0));
		assertEquals("Checking Plan-Order", box.getSequence().getPlanItems().get(0), game.getPlan().getPlanItems().get(1));
		assertEquals("Checking Plan-Order", box.getSequence().getPlanItems().get(1), game.getPlan().getPlanItems().get(2));
		assertEquals("Checking Plan-Order", action2, game.getPlan().getPlanItems().get(3));
	}

	private void createGame()
	{
		GameConfig config = new GameConfig();
		config.addConstraint(new BudgetConstraint(budget));
		Location location = new Location(config, "Sonnwendjoch");
		config.setStartLocation(location);
		activityConfig = new ActivityConfig("My activity", 10, 500, new Certainty(), 1, new DurationRange(10), deadLine);
		accommodationConfig = new AccommodationConfig("Hotel", 0, 0, new Certainty(), 1, ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(activityConfig);
		location.addActionConfig(accommodationConfig);
		
		HashSet<PlanItemConfig> set = new HashSet<PlanItemConfig>();
		set.add(accommodationConfig);
		boxConfig = new LateModelingBoxConfig("LateModelingBox", config, 0, new Duration(config.getDayDuration()*3), location, location, set);
		config.addBoxConfig(boxConfig);

		Set<PlanItemConfig> modelingItems = new HashSet<PlanItemConfig>();
		modelingItems.add(activityConfig);
		boxConfig = new LateModelingBoxConfig("lmbc", config, 0, new Duration(120), location, location, modelingItems);
		config.addBoxConfig(boxConfig);

		game = new Game(config, "John Doe");
	}
}
