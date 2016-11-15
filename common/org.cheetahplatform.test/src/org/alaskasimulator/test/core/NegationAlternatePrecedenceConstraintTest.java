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
import org.alaskasimulator.core.buildtime.constraint.NegationAlternatePrecedenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class NegationAlternatePrecedenceConstraintTest {

	private ActivityConfig actionConfigA;
	private ActivityConfig actionConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;

	@Test
	public void affects() {
		NegationAlternatePrecedenceConstraint constraint = new NegationAlternatePrecedenceConstraint(actionConfigA, actionConfigB);
		assertTrue(constraint.affects(actionConfigA));
		assertFalse(constraint.affects(actionConfigB));
	}

	@Test
	public void allowsExecutionABA() {
		NegationAlternatePrecedenceConstraint constraint = new NegationAlternatePrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
	}

	@Test
	public void allowsExecutionABB() {
		NegationAlternatePrecedenceConstraint constraint = new NegationAlternatePrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionB2));
	}

	@Test
	public void allowsExecutionBAB() {
		NegationAlternatePrecedenceConstraint constraint = new NegationAlternatePrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertTrue(constraint.allowsExecution(actionB));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA, failure));
		assertEquals(failure.getType(), Failure.TYPE_NEGATION_ALTERNATE_PRECEDENCE);
		assertTrue(failure.getOrigins().contains(actionA));
		assertTrue(constraint.allowsExecution(actionB2));
	}

	@Test
	public void allowsExecutionBABAB() {
		NegationAlternatePrecedenceConstraint constraint = new NegationAlternatePrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		ActivityAction actionB3 = TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		assertTrue(constraint.allowsExecution(actionB));

		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA, failure));
		assertEquals(failure.getType(), Failure.TYPE_NEGATION_ALTERNATE_PRECEDENCE);
		assertTrue(failure.getOrigins().contains(actionA));

		assertTrue(constraint.allowsExecution(actionB2));

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA2, failure));
		assertEquals(failure.getType(), Failure.TYPE_NEGATION_ALTERNATE_PRECEDENCE);
		assertTrue(failure.getOrigins().contains(actionA2));

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

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(actionConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(actionConfigB);
	}
}
