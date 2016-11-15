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
import org.alaskasimulator.core.buildtime.constraint.NegationSuccessionConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class NegationSuccessionConstraintTest {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActivityConfig activityConfigC;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;

	@Test
	public void allowsCompletionA() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionACB() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionB() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBAB() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBBCACAA() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 50);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 150);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 250);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsExecutionA() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		assertTrue(constraint.allowsExecution(actionA));
	}

	@Test
	public void allowsExecutionACB() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 200);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB, failure));
		assertTrue(failure.getOrigins().contains(actionB));
	}

	@Test
	public void allowsExecutionB() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);

		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionBAB() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);

		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionB2, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_NEGATION_SUCCESSION));
		assertTrue(failure.getOrigins().contains(actionB2));
	}

	@Test
	public void allowsExecutionBBCACAA() {
		NegationSuccessionConstraint constraint = new NegationSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 50);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 100);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 150);
		ActivityAction actionC2 = TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 250);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionB2));
		assertTrue(constraint.allowsExecution(actionC));
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionC2));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionA3));
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

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(activityConfigC);
	}
}
