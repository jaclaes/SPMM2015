/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import junit.framework.Assert;

import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.TimeBasedRestrictionsConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class TimeBasedRestrictionsConstraintTest extends Assert {
	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private Game game;
	private ActionConfigProxy activityAProxy;
	private ActionConfigProxy activityBProxy;

	@Test
	public void affects() {
		TimeBasedRestrictionsConstraint constraint = new TimeBasedRestrictionsConstraint(activityConfigA,
				TimeBasedRestrictionsConstraint.MODE_JOURNEY, null, null, 12);
		assertTrue(constraint.affects(activityConfigA));
		assertFalse(constraint.affects(activityConfigB));
	}

	@Test
	public void allowsExecutionModeDay() {
		TimeBasedRestrictionsConstraint constraint = new TimeBasedRestrictionsConstraint(activityConfigA,
				TimeBasedRestrictionsConstraint.MODE_DAY, 0, null, 2);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
		assertFalse(constraint.allowsExecution(actionA3));
	}

	@Test
	public void allowsExecutionModeHours1() {
		TimeBasedRestrictionsConstraint constraint = new TimeBasedRestrictionsConstraint(activityConfigA,
				TimeBasedRestrictionsConstraint.MODE_HOUR, null, 12, 2);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
		assertFalse(constraint.allowsExecution(actionA3));
	}

	@Test
	public void allowsExecutionModeHours2() {
		TimeBasedRestrictionsConstraint constraint = new TimeBasedRestrictionsConstraint(activityConfigA,
				TimeBasedRestrictionsConstraint.MODE_HOUR, null, 12, 2);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 1, 0);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 1, 100);
		ActivityAction actionA4 = TestHelper.addActivityToPlan(activityAProxy, 1, 200);
		ActivityAction actionA5 = TestHelper.addActivityToPlan(activityAProxy, 1, 300);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionA3));
		assertTrue(constraint.allowsExecution(actionB2));
		assertTrue(constraint.allowsExecution(actionA4));
		assertFalse(constraint.allowsExecution(actionA5));
	}

	@Test
	public void allowsExecutionModeHours3() {
		TimeBasedRestrictionsConstraint constraint = new TimeBasedRestrictionsConstraint(activityConfigA,
				TimeBasedRestrictionsConstraint.MODE_HOUR, null, 15, 3);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 660);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 1, 30);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 1, 60);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 1, 180);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 1, 240);
		ActivityAction actionA4 = TestHelper.addActivityToPlan(activityAProxy, 1, 300);
		ActivityAction actionA5 = TestHelper.addActivityToPlan(activityAProxy, 1, 360);
		ActivityAction actionA6 = TestHelper.addActivityToPlan(activityAProxy, 1, 420);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
		assertTrue(constraint.allowsExecution(actionA3));
		assertTrue(constraint.allowsExecution(actionB2));
		assertTrue(constraint.allowsExecution(actionA4));
		assertTrue(constraint.allowsExecution(actionA5));
		assertFalse(constraint.allowsExecution(actionA6));
	}

	@Test
	public void allowsExecutionModeJourney() {
		TimeBasedRestrictionsConstraint constraint = new TimeBasedRestrictionsConstraint(activityConfigA,
				TimeBasedRestrictionsConstraint.MODE_JOURNEY, null, null, 2);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionA2 = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		ActivityAction actionA3 = TestHelper.addActivityToPlan(activityAProxy, 0, 300);

		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionA2));
		assertFalse(constraint.allowsExecution(actionA3));
	}

	@Before
	public void setUp() {
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
