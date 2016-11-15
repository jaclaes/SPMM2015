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
import org.alaskasimulator.core.buildtime.constraint.NegationAlternateResponseConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class NegationAlternateResponseContraintTest {

	private ActivityConfig actionConfigA;
	private ActivityConfig actionConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActivityConfig actionConfigC;
	private ActionConfigProxy activityCProxy;

	@Test
	public void affects() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		assertFalse(constraint.affects(actionConfigA));
		assertTrue(constraint.affects(actionConfigB));
	}

	@Test
	public void allowsCompletionABA() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionABABA() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 400);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionABB() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBAAB() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBABAB() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionBACAB() {
		NegationAlternateResponseConstraint constraint = new NegationAlternateResponseConstraint(actionConfigA, actionConfigB);
		TestHelper.addActivityToPlan(activityBProxy, 0, 0);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 0, 300);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		assertTrue(constraint.allowsCompletion(game));
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
