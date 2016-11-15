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
import org.alaskasimulator.core.buildtime.constraint.RespondedExistenceConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class RespondedExistenceConstraintTest {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActivityConfig activityConfigC;
	private ActionConfigProxy activityCProxy;

	@Test
	public void allowsCompletionA() {
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionAB() {
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 10);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionACB() {
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 5);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 10);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionB() {
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBA() {
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBCA() {
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activityConfigA, activityConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 5);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 10);
		assertTrue(constraint.allowsCompletion(game));
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
