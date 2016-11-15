/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.Succession2Constraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class Succession2ConstraintTest {
	private ActivityConfig actionConfigA;
	private ActivityConfig actionConfigB;
	private ActivityConfig actionConfigC;
	private ActivityConfig actionConfigD;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;
	private ActionConfigProxy activityDProxy;

	@Test
	public void allowsCompletionABC() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionADCDADB() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityDProxy, 0, 50);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityDProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityDProxy, 0, 400);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 500);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionDA() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionDB() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionDC() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionDCDADB() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityDProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityDProxy, 0, 400);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 500);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsExecutionABC() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 200);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionC));
	}

	@Test
	public void allowsExecutionADCDADB() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		ActivityAction actionA1 = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionD1 = TestHelper.addActivityToPlan(activityDProxy, 0, 50);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		ActivityAction actionD2 = TestHelper.addActivityToPlan(activityDProxy, 0, 200);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		ActivityAction actionD3 = TestHelper.addActivityToPlan(activityDProxy, 0, 400);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 500);

		assertTrue(constraint.allowsExecution(actionA1));
		assertTrue(constraint.allowsExecution(actionD1));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionD2));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionD3));
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionDA() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		ActivityAction actionD = TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);

		assertTrue(constraint.allowsExecution(actionD));
		assertTrue(constraint.allowsExecution(actionA));
	}

	@Test
	public void allowsExecutionDB() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		ActivityAction actionD = TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);

		assertTrue(constraint.allowsExecution(actionD));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionDC() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		ActivityAction actionD = TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 100);

		assertTrue(constraint.allowsExecution(actionD));
		assertFalse(constraint.allowsExecution(actionC));
	}

	@Test
	public void allowsExecutionDCDADB() {
		Succession2Constraint constraint = new Succession2Constraint(actionConfigA, actionConfigB, actionConfigC);
		ActivityAction actionD1 = TestHelper.addActivityToPlan(activityDProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		ActivityAction actionD2 = TestHelper.addActivityToPlan(activityDProxy, 0, 200);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		ActivityAction actionD3 = TestHelper.addActivityToPlan(activityDProxy, 0, 400);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 500);

		assertTrue(constraint.allowsExecution(actionD1));
		assertFalse(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionD2));
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionD3));
		assertTrue(constraint.allowsExecution(actionB));
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
		actionConfigD = new ActivityConfig("Activity D", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(actionConfigD);

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(actionConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(actionConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(actionConfigC);
		activityDProxy = game.getConfig().findActionConfigProxy(actionConfigD);
	}
}
