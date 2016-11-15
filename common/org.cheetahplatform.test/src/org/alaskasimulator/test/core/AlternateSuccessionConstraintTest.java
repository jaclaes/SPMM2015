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
import org.alaskasimulator.core.buildtime.constraint.AlternateSuccessionConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class AlternateSuccessionConstraintTest {

	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActivityConfig activityConfigC;
	private ActionConfigProxy activityCProxy;

	@Test
	public void affects() {
		AlternateSuccessionConstraint constraint = new AlternateSuccessionConstraint(activityConfigA, activityConfigB);
		assertTrue(constraint.affects(activityConfigA));
		assertTrue(constraint.affects(activityConfigB));
	}

	@Test
	public void allowsCompletionABA() {
		AlternateSuccessionConstraint constraint = new AlternateSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionACBABAB() {
		AlternateSuccessionConstraint constraint = new AlternateSuccessionConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 150);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 250);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsExecutionABA() {
		AlternateSuccessionConstraint constraint = new AlternateSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA1 = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB1 = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 200);

		assertTrue(constraint.allowsExecution(actionA1));
		assertTrue(constraint.allowsExecution(actionB1));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(actionA2, failure));
		assertTrue(failure.getType().equals(Failure.TYPE_ALTERNATE_SUCCESSION_CONSTRAINT));
		assertTrue(failure.getOrigins().contains(actionA2));
	}

	@Test
	public void allowsExecutionACBABAB() {
		AlternateSuccessionConstraint constraint = new AlternateSuccessionConstraint(activityConfigA, activityConfigB);
		ActivityAction actionA1 = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionC1 = TestHelper.addActivityToPlan(activityCProxy, 0, 50);
		ActivityAction actionB1 = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 150);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 0, 250);
		ActivityAction actionB3 = TestHelper.addActivityToPlan(activityBProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionA1));
		assertTrue(constraint.allowsExecution(actionC1));
		assertTrue(constraint.allowsExecution(actionB1));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionB2));
		assertTrue(constraint.allowsExecution(actionA3));
		assertTrue(constraint.allowsExecution(actionB3));
	}

	@Before
	public void setup() {
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
