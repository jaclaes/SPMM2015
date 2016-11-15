/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

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
import org.alaskasimulator.core.buildtime.constraint.AlternativePrecedenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class AlternativePrecedenceConstraintTest {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActivityConfig activityConfigC;
	private ActivityConfig activityConfigD;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;
	private ActionConfigProxy activityDProxy;

	@Test
	public void allowsExecutionAB() {
		AlternativePrecedenceConstraint constraint = new AlternativePrecedenceConstraint(activityConfigA, activityConfigB, activityConfigC);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionABC() {
		AlternativePrecedenceConstraint constraint = new AlternativePrecedenceConstraint(activityConfigA, activityConfigB, activityConfigC);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionC));
	}

	@Test
	public void allowsExecutionABDC() {
		AlternativePrecedenceConstraint constraint = new AlternativePrecedenceConstraint(activityConfigA, activityConfigB, activityConfigC);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionD = TestHelper.addActivityToPlan(activityDProxy, 0, 150);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionD));
		assertTrue(constraint.allowsExecution(actionC));
	}

	@Test
	public void allowsExecutionCBA() {
		AlternativePrecedenceConstraint constraint = new AlternativePrecedenceConstraint(activityConfigA, activityConfigB, activityConfigC);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionC, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_ALTERNATIVE_PRECEDENCE_CONSTRAINT));
		assertTrue(failure.getOrigins().contains(actionC));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA));
	}

	@Test
	public void allowsExecutionDCBA() {
		AlternativePrecedenceConstraint constraint = new AlternativePrecedenceConstraint(activityConfigA, activityConfigB, activityConfigC);
		ActivityAction actionD = TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		assertTrue(constraint.allowsExecution(actionD));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionC, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_ALTERNATIVE_PRECEDENCE_CONSTRAINT));
		assertTrue(failure.getOrigins().contains(actionC));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA));
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
		activityConfigC = new ActivityConfig("Activity C", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigC);
		activityConfigD = new ActivityConfig("Activity D", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigD);

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(activityConfigC);
		activityDProxy = game.getConfig().findActionConfigProxy(activityConfigD);
	}
}
