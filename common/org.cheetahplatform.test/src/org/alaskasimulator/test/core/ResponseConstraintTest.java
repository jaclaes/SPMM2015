/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.ResponseConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.ActivityConfigProxy;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class ResponseConstraintTest {

	private ActionConfigProxy activityAProxy;
	private Game game;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActivityConfig activityConfigC;
	private ResponseConstraint constraint;

	@Test
	public void affects() {
		assertTrue(constraint.affects(activityConfigA));
		assertFalse(constraint.affects(activityConfigB));
		assertFalse(constraint.affects(activityConfigC));
	}

	@Test
	public void complexExample() {
		// B,A,A,A,C,B should be accepted
		ActivityAction activityB1Action = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityB1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityB1Action);

		ActivityAction activityA1Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA1Action);
		ActivityAction activityA2Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA2Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA2Action);
		ActivityAction activityA3Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA3Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA3Action);

		ActivityAction activityCAction = new ActivityAction((ActivityConfigProxy) activityCProxy);
		activityCAction.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityCAction);

		ActivityAction activityB2Action = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityB2Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityB2Action);

		assertTrue(constraint.allowsCompletion(game));

	}

	@Test
	public void complexRejection() {
		// A,B,A should be rejected

		ActivityAction activityA1Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA1Action);

		ActivityAction activityB1Action = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityB1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityB1Action);

		ActivityAction activityA2Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA2Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA2Action);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void directResponse() {
		ActivityAction activityAAction = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityAAction.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityAAction);

		ActivityAction activityBAction = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityBAction.setStartTime(0, 400);
		game.getPlan().insertPlanItem(activityBAction);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void getFailures() {
		ActivityAction activityB1Action = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityB1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityB1Action);

		ActivityAction activityA1Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA1Action);
		ActivityAction activityA2Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA2Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA2Action);
		ActivityAction activityA3Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA3Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA3Action);

		ActivityAction activityCAction = new ActivityAction((ActivityConfigProxy) activityCProxy);
		activityCAction.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityCAction);

		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());

	}

	@Test
	public void ignoreCancelledActions() {
		ActivityAction activityAAction = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityAAction.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityAAction);

		ActivityAction activityBAction = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityBAction.setStartTime(0, 400);
		game.getPlan().insertPlanItem(activityBAction);

		assertTrue(constraint.allowsCompletion(game));
		activityBAction.cancel();
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalFirstAction() {
		ActivityConfig activityConfigB = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(10), 4);
		new ResponseConstraint(null, activityConfigB);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalSecondAction() {
		ActivityConfig activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(10), 4);
		new ResponseConstraint(activityConfigA, null);
	}

	@Test
	public void indirectResponse() {
		ActivityAction activityAAction = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityAAction.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityAAction);

		ActivityAction activityCAction = new ActivityAction((ActivityConfigProxy) activityCProxy);
		activityCAction.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityCAction);

		ActivityAction activityBAction = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityBAction.setStartTime(0, 400);
		game.getPlan().insertPlanItem(activityBAction);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void multipleActivityA() {
		ActivityAction activityA1Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA1Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA1Action);
		ActivityAction activityA2Action = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityA2Action.setStartTime(0, 0);
		game.getPlan().insertPlanItem(activityA2Action);

		ActivityAction activityBAction = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityBAction.setStartTime(0, 400);
		game.getPlan().insertPlanItem(activityBAction);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void onlyActivityB() {
		ActivityAction activityBAction = new ActivityAction((ActivityConfigProxy) activityBProxy);
		activityBAction.setStartTime(0, 400);
		game.getPlan().insertPlanItem(activityBAction);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void preventOnlyActivityA() {
		ActivityAction activityAAction = new ActivityAction((ActivityConfigProxy) activityAProxy);
		activityAAction.setStartTime(0, 400);
		game.getPlan().insertPlanItem(activityAAction);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test(expected = IllegalArgumentException.class)
	public void sameAction() {
		ActivityConfig activityConfig = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(10), 4);
		new ResponseConstraint(activityConfig, activityConfig);
	}

	@Before
	public void setUp() {
		GameConfig gameConfig = new GameConfig(1, "ResponseTestConfig", 5, 5, 700);
		Location locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigA);
		activityConfigB = new ActivityConfig("Activity B", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigB);
		activityConfigC = new ActivityConfig("Activity C", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigC);
		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(activityConfigC);

		constraint = new ResponseConstraint(activityConfigA, activityConfigB);
	}
}
