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
import org.alaskasimulator.core.buildtime.constraint.NegationPrecedenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class NegationPrecedenceTest {
	private ActivityConfig actionConfigA;
	private ActivityConfig actionConfigB;
	private ActivityConfig actionConfigC;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;

	@Test
	public void allowsExecutionAB() {
		NegationPrecedenceConstraint constraint = new NegationPrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsExecution(actionA));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_NEGATION_PRECEDENCE));
		assertTrue(failure.getOrigins().contains(actionB));
	}

	@Test
	public void allowsExecutionACB() {
		NegationPrecedenceConstraint constraint = new NegationPrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionBA() {
		NegationPrecedenceConstraint constraint = new NegationPrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA));
	}

	@Test
	public void allowsExecutionBCA() {
		NegationPrecedenceConstraint constraint = new NegationPrecedenceConstraint(actionConfigA, actionConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionA));
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
