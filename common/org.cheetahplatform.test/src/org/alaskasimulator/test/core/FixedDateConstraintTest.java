/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.ExecutionTimeFrame;
import org.alaskasimulator.core.buildtime.constraint.FixedDateConstraint;
import org.alaskasimulator.core.buildtime.constraint.TimeFrame;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class FixedDateConstraintTest extends Assert {

	private ActivityConfig actionConfigA;
	private ActivityConfig actionConfigB;
	private ActivityConfig actionConfigC;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;

	@Test
	public void affects() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, null);
		assertTrue(constraint.affects(actionConfigA));
	}

	@Test
	public void allowsCompletionES1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, null);
		TestHelper.addActivityToPlan(activityAProxy, 0, 15);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionES2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, null);
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionES3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, null);
		TestHelper.addActivityToPlan(activityAProxy, 2, 150);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLC1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, getEcutionTimeFrame(0));
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLC2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLC3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 100);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLC4() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, getEcutionTimeFrame(150));
		TestHelper.addActivityToPlan(activityAProxy, 1, 50);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLC5() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), null, getEcutionTimeFrame(150));
		TestHelper.addActivityToPlan(activityAProxy, 2, 150);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLS1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0), null);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLS2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0), null);
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLS3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0), null);
		TestHelper.addActivityToPlan(activityAProxy, 1, 1);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLS4() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(150), null);
		TestHelper.addActivityToPlan(activityAProxy, 1, 150);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLS5() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(150), null);
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLSLC1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0),
				getEcutionTimeFrame(0));
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLSLC2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0),
				getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLSLC3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0),
				getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 100);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLSLC4() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0),
				getEcutionTimeFrame(150));
		TestHelper.addActivityToPlan(activityAProxy, 1, 50);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionESLSLC5() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(0),
				getEcutionTimeFrame(150));
		TestHelper.addActivityToPlan(activityAProxy, 2, 150);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLC1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, null, getEcutionTimeFrame(0));
		TestHelper.addActivityToPlan(activityAProxy, 0, 15);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLC2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, null, getEcutionTimeFrame(0));
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLC3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, null, getEcutionTimeFrame(99));
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLC4() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, null, getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLC5() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, null, getEcutionTimeFrame(0));
		TestHelper.addActivityToPlan(activityAProxy, 2, 150);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLS1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), null);
		TestHelper.addActivityToPlan(activityAProxy, 0, 15);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLS2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), null);
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLS3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), null);
		TestHelper.addActivityToPlan(activityAProxy, 2, 150);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLSLC1() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), getEcutionTimeFrame(0));
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLSLC2() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 0);

		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLSLC3() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), getEcutionTimeFrame(100));
		TestHelper.addActivityToPlan(activityAProxy, 1, 100);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLSLC4() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), getEcutionTimeFrame(150));
		TestHelper.addActivityToPlan(activityAProxy, 1, 50);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionLSLC5() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, null, getEcutionTimeFrame(0), getEcutionTimeFrame(150));
		TestHelper.addActivityToPlan(activityAProxy, 2, 150);

		assertFalse(constraint.allowsCompletion(game));
	}

	@Test
	public void allowsCompletionRandom() {
		FixedDateConstraint constraint = new FixedDateConstraint(actionConfigA, getEcutionTimeFrame(0), getEcutionTimeFrame(500),
				getEcutionTimeFrame(600));
		TestHelper.addActivityToPlan(activityBProxy, 1, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 1, 50);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 1, 100);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 1, 200);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 1, 300);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 1, 400);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 1, 500);
		assertTrue(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityAProxy, 1, 501);
		assertFalse(constraint.allowsCompletion(game));
	}

	private ExecutionTimeFrame getEcutionTimeFrame(int time) {
		TimeFrame timeFrame = new TimeFrame(new DurationRange(0), 480 + time);
		List<TimeFrame> timeFrames = new ArrayList<TimeFrame>();
		timeFrames.add(timeFrame);
		return new ExecutionTimeFrame(timeFrames, 1);
	}

	@Before
	public void setUp() {
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
