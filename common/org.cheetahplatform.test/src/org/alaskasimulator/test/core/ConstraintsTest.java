/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.RouteConfig;
import org.alaskasimulator.core.buildtime.constraint.BudgetConstraint;
import org.alaskasimulator.core.buildtime.constraint.CoExistenceConstraint;
import org.alaskasimulator.core.buildtime.constraint.Constraint;
import org.alaskasimulator.core.buildtime.constraint.JourneyEndLocationConstraint;
import org.alaskasimulator.core.buildtime.constraint.MaxConstraint;
import org.alaskasimulator.core.buildtime.constraint.MinConstraint;
import org.alaskasimulator.core.buildtime.constraint.MutualExclusionConstraint;
import org.alaskasimulator.core.buildtime.constraint.PrecedenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.junit.Test;

public class ConstraintsTest {
	private ActivityConfig activityConfig1;
	private final int durationOfActivity = 60;
	private Game game;
	private Location location;
	private double budget = 1000;
	private ActivityConfig activityConfig2;
	private GameConfig gameConfig;
	private AccommodationConfig accommodation;
	private Location location2;
	private RouteConfig routeConfig;

	@Test
	public void budgetConstraint() {
		budget = 100;
		createGameConfig();
		createGame();
		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();

		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);

		action2.setStartTime(0, activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);

		action1.execute();
		assertFalse("Not enough money left for this action!", action2.isExecutable());
	}

	@Test
	public void corequisiteConstraint() {
		createGameConfig();
		CoExistenceConstraint constraint = new CoExistenceConstraint(activityConfig1, activityConfig2);
		gameConfig.addConstraint(constraint);
		createGame();
		game.startJourney();

		assertTrue("Completion should be allowed.", game.isFinishable());

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig2).createAction();

		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		action1.execute();

		assertFalse("Action1 requires action2 to be executed.", game.isFinishable());

		action2.setStartTime(0, activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);
		action2.execute();

		assertTrue("Completion should be allowed.", game.isFinishable());
	}

	private void createGame() {
		game = new Game(gameConfig, "John Doe");
	}

	private void createGameConfig() {
		gameConfig = new GameConfig(System.currentTimeMillis(), "testConfig", 0, 10, 1440);
		gameConfig.setBusinessValueConstraintSleepPenalty();
		location = new Location(gameConfig, "Sonnwendjoch");
		location2 = new Location(gameConfig, "Saulueg");
		routeConfig = new RouteConfig("Strange way", 0, 100, new Certainty(), 1, new DurationRange(1), location, location2,
				ActionConfig.NOT_BOOKABLE);
		location.addActionConfig(routeConfig);
		location2.addActionConfig(routeConfig);

		gameConfig.setStartLocation(location);
		activityConfig1 = new ActivityConfig("My activity 1", 80, 10, new Certainty(), 1.0, new DurationRange(durationOfActivity), 1);
		activityConfig2 = new ActivityConfig("My activity 2", 80, 10, new Certainty(), 1.0, new DurationRange(durationOfActivity), 1);
		accommodation = new AccommodationConfig("Home sweet home", 0, 100, new Certainty(), 1.0, -1);
		location.addActionConfig(activityConfig1);
		location.addActionConfig(activityConfig2);
		location.addActionConfig(accommodation);
		gameConfig.addConstraint(new MaxConstraint(activityConfig2, 1));
		gameConfig.addConstraint(new BudgetConstraint(budget));
	}

	@Test
	public void journeyDurationConstraint() {
		gameConfig = new GameConfig(0, "My game", 0, 2, 700);
		gameConfig.setBusinessValueConstraintSleepPenalty();
		location = new Location(gameConfig, "Sonnwendjoch");
		gameConfig.setStartLocation(location);
		accommodation = new AccommodationConfig("Home sweet home", 0, 100, new Certainty(), 1.0, -1);
		location.addActionConfig(accommodation);

		createGame();
		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(accommodation).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(accommodation).createAction();
		action1.setStartTime(0, gameConfig.getDayDuration() - 1);
		game.getPlan().insertPlanItem(action1);

		action2.setStartTime(1, gameConfig.getDayDuration() - 1);
		game.getPlan().insertPlanItem(action2);

		action1.execute();
		assertFalse("Should not be schedulable, last day of journey.", action2.isExecutable());
	}

	@Test
	public void journeyEndConstraint() {
		createGameConfig();
		Constraint constraint = new JourneyEndLocationConstraint(location2);
		gameConfig.addConstraint(constraint);
		createGame();

		game.startJourney();

		assertFalse("We are not in a location where the journey can be finished.", game.isFinishable());
		Action action = game.getConfig().findActionConfigProxy(routeConfig).createAction();
		action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action);
		action.execute();

		assertTrue("Should be able to finish the game.", game.isFinishable());
	}

	@Test
	public void maxConstraint() {
		createGameConfig();
		Constraint constraint = new MaxConstraint(activityConfig1, 1);
		gameConfig.addConstraint(constraint);
		createGame();

		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		action2.setStartTime(0, activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);

		action1.execute();
		assertFalse("MaxConstraint should forbid execution of this constraint", action2.isExecutable());
	}

	@Test
	public void minConstraint() {
		createGameConfig();
		MinConstraint constraint = new MinConstraint(activityConfig1, 2);
		gameConfig.addConstraint(constraint);
		createGame();
		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action3 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();

		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		action2.setStartTime(0, activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);
		action3.setStartTime(0, 2 * activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action3);

		assertFalse("Should not be able to complete the game, did not execute activityConfig1 two times.", game.isFinishable());

		action1.execute();

		assertFalse("Should not be able to complete the game, did not execute activityConfig1 two times.", game.isFinishable());

		action2.execute();

		assertTrue("Should be allowed to complete the game, executed activityConfig1 two times.", game.isFinishable());

		action3.execute();

		assertTrue("Should still be allowed to complete the game, executed activityConfig1 three time.", game.isFinishable());
	}

	@Test
	public void mutualExlusionConstraint() {
		createGameConfig();
		MutualExclusionConstraint constraint = new MutualExclusionConstraint(activityConfig1, activityConfig2);
		gameConfig.addConstraint(constraint);
		createGame();
		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig2).createAction();
		Action action11 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();

		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		action2.setStartTime(0, activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);

		assertTrue("Should be executable.", action1.isExecutable());
		assertFalse("Should not be executable - comes after action1!", action2.isExecutable());

		action1.execute();

		assertFalse("Already executed.", action1.isExecutable());
		assertFalse("Mutually excluded.", action2.isExecutable());

		action11.setStartTime(0, 2 * activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action11);
		assertFalse("Should not be executable - comes after action2!", action11.isExecutable());
		action2.cancel();
		assertTrue("Should now be executable - comes after canceled action2!", action11.isExecutable());

		// other execution order
		createGame();
		game.startJourney();

		action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		action2 = game.getConfig().findActionConfigProxy(activityConfig2).createAction();
		action2.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action2);
		action1.setStartTime(0, activityConfig2.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action1);

		assertTrue("Should be executable.", action2.isExecutable());
		assertFalse("Should not be executable - comes after action1!", action1.isExecutable());

		action2.execute();

		assertFalse("Already executed.", action2.isExecutable());
		assertFalse("Mutually excluded.", action1.isExecutable());
	}

	@Test
	public void prerequisiteConstraint() {
		createGameConfig();
		PrecedenceConstraint constraint = new PrecedenceConstraint(activityConfig1, activityConfig2);
		gameConfig.addConstraint(constraint);
		createGame();
		game.startJourney();

		Action action1 = game.getConfig().findActionConfigProxy(activityConfig1).createAction();
		Action action2 = game.getConfig().findActionConfigProxy(activityConfig2).createAction();

		action1.setStartTime(0, 0);
		game.getPlan().insertPlanItem(action1);
		action2.setStartTime(0, activityConfig1.getDuration().getMinutes());
		game.getPlan().insertPlanItem(action2);

		assertFalse("Should not yet be executable, did not execute action1 yet.", action2.isExecutable());
		action1.execute();
		assertTrue("Should be executable now, execute action1.", action2.isExecutable());
		action2.execute();
	}
}
