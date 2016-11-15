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
import org.alaskasimulator.core.buildtime.constraint.AlternatePrecedenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class AlternatePrecedenceConstraintTest {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;

	@Test
	public void affectsAllActions() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		assertFalse(constraint.affects(activityConfigA));
		assertTrue(constraint.affects(activityConfigB));
	}

	@Test
	public void allowsExecutionAAB() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionABA() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 400);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
	}

	@Test
	public void allowsExecutionABB() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB2, failure));
		assertEquals(actionB2, failure.getFirstOrigin());
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());
	}

	@Test
	public void allowsExecutionBAA() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 400);

		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getOrigins().contains(actionB));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA, failure));
		assertTrue(failure.getOrigins().contains(actionA));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA2, failure));
		assertTrue(failure.getOrigins().contains(actionA2));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());
	}

	@Test
	public void allowsExecutionBAB() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 400);

		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getOrigins().contains(actionB));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA, failure));
		assertTrue(failure.getOrigins().contains(actionA));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB2, failure));
		assertTrue(failure.getOrigins().contains(actionB2));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());
	}

	@Test
	public void allowsExecutionBBA() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 400);

		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getOrigins().contains(actionB));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB2, failure));
		assertTrue(failure.getOrigins().contains(actionB2));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());

		failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA, failure));
		assertTrue(failure.getOrigins().contains(actionA));
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());
	}

	@Test
	public void permitExecutionOfSingleA() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsExecution(actionA));
	}

	@Test
	public void permitExecutionOfSingleB() {
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertEquals(actionB, failure.getFirstOrigin());
		assertEquals(Failure.TYPE_ALTERNATE_PRECEDENCE_CONSTRAINT, failure.getType());
	}

	@Before
	public void setUp() {
		TestHelper.setLocaleToEnglish();
		GameConfig gameConfig = new GameConfig(1, "ResponseTestConfig", 5, 5, 700);
		Location locationA = new Location(gameConfig, "Location A", new UtmCoordinate(1, 266000, 0));
		LocalWeatherCharacteristics weatherCharacteristics = new LocalWeatherCharacteristics(0.3, 0.3);
		locationA.setWeatherCharacteristics(weatherCharacteristics);
		activityConfigA = new ActivityConfig("Activity A", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigA);
		activityConfigB = new ActivityConfig("Activity B", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigB);

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
	}
}
