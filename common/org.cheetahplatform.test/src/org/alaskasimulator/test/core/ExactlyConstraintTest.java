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
import org.alaskasimulator.core.buildtime.constraint.ExactlyConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class ExactlyConstraintTest {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;

	@Test
	public void affects() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 1);
		assertTrue(constraint.affects(activityConfigA));
		assertFalse(constraint.affects(activityConfigB));
	}

	@Test
	public void allowsCompletionAAB1() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 1);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 20);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionAAB2() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 2);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 20);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBABABAB2() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 2);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 20);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 30);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 40);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 50);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 60);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsExecutionAAB1() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 1);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsExecution(actionA));
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertFalse(constraint.allowsExecution(actionA2));
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 20);
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionAAB2() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 2);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsExecution(actionA));
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertTrue(constraint.allowsExecution(actionA2));
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 20);
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionBABABAB2() {
		ExactlyConstraint constraint = new ExactlyConstraint(activityConfigA, 2);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsExecution(actionB));
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertTrue(constraint.allowsExecution(actionA));
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 20);
		assertTrue(constraint.allowsExecution(actionB2));
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 30);
		assertTrue(constraint.allowsExecution(actionA2));
		ActivityAction actionB3 = TestHelper.addActivityToPlan(activityBProxy, 0, 40);
		assertTrue(constraint.allowsExecution(actionB3));
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 0, 50);
		assertFalse(constraint.allowsExecution(actionA3));
		ActivityAction actionB4 = TestHelper.addActivityToPlan(activityBProxy, 0, 60);
		assertTrue(constraint.allowsExecution(actionB4));
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
