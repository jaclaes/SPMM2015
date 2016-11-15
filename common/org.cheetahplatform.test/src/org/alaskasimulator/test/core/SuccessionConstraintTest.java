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
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.SuccessionConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class SuccessionConstraintTest {
	private ActivityConfig actionConfigA;
	private ActivityConfig actionConfigB;
	private ActivityConfig actionConfigC;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;

	@Test
	public void affects() {
		SuccessionConstraint constraint = new SuccessionConstraint(actionConfigA, actionConfigB);
		assertTrue(constraint.affects(actionConfigA));
		assertTrue(constraint.affects(actionConfigB));
		assertFalse(constraint.affects(actionConfigC));
	}

	@Test
	public void allowsCompletionABBA() {
		SuccessionConstraint constraint = new SuccessionConstraint(actionConfigA, actionConfigB);

		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsExecutionABB() {
		SuccessionConstraint constraint = new SuccessionConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionB2));
	}

	@Test
	public void allowsExecutionABBA() {
		SuccessionConstraint constraint = new SuccessionConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionB2));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA2, failure));
		assertEquals(failure.getType(), Failure.TYPE_SUCCESSION);
		assertTrue(failure.getOrigins().contains(actionA2));
	}

	@Test
	public void allowsExecutionACABB() {
		SuccessionConstraint constraint = new SuccessionConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionB2));
	}

	@Test
	public void allowsExecutionBCBCACACB() {
		SuccessionConstraint constraint = new SuccessionConstraint(actionConfigA, actionConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionC2 = TestHelper.addActivityToPlan(activityCProxy, 0, 150);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		ActivityAction actionC3 = TestHelper.addActivityToPlan(activityCProxy, 0, 250);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		ActivityAction actionC4 = TestHelper.addActivityToPlan(activityCProxy, 0, 350);
		ActivityAction actionB3 = TestHelper.addActivityToPlan(activityBProxy, 0, 400);

		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getOrigins().contains(actionB));
		assertTrue(constraint.allowsExecution(actionC));
		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB2, failure));
		assertTrue(failure.getOrigins().contains(actionB2));
		assertTrue(constraint.allowsExecution(actionC2));
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC3));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionC4));
		assertTrue(constraint.allowsExecution(actionB3));
	}

	@Before
	public void setUp() {
		TestHelper.setLocaleToEnglish();
		GameConfig gameConfig = new GameConfig(1, "ResponseTestConfig", 5, 5, 700);
		Location locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		actionConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(actionConfigA);
		actionConfigB = new ActivityConfig("Activity B", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(actionConfigB);
		actionConfigC = new ActivityConfig("Activity C", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(actionConfigC);

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(actionConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(actionConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(actionConfigC);
	}
}
