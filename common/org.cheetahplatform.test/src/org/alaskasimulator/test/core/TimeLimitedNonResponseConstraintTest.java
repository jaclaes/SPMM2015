/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.TimeLimitedNonResponseConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.ActivityConfigProxy;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class TimeLimitedNonResponseConstraintTest {

	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActivityConfig activityConfigC;
	private Game game;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;
	private ActionConfigProxy activityAProxy;

	@Test
	public void activitiesAtSameDay() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		addToPlan(activityBProxy, 0, 100);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertFalse(failures.isEmpty());
	}

	private Action addToPlan(ActionConfigProxy proxy, int day, int minutes) {
		ActivityAction action = new ActivityAction((ActivityConfigProxy) proxy);
		action.setStartTime(day, minutes);
		game.getPlan().insertPlanItem(action);
		return action;
	}

	@Test
	public void affects() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		assertTrue(constraint.affects(activityConfigA));
		assertTrue(constraint.affects(activityConfigB));
		assertFalse(constraint.affects(activityConfigC));
	}

	@Test
	public void allowExecutionOfActivityB() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		Action actionB = addToPlan(activityBProxy, 2, 300);
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowExecutionOfOtherAction() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		Action actionC = addToPlan(activityCProxy, 0, 200);
		Action actionB = addToPlan(activityBProxy, 0, 300);
		assertTrue(constraint.allowsExecution(actionC));
		assertFalse(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowExecutionOfSingleActivityB() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		Action actionB = addToPlan(activityBProxy, 2, 300);
		assertTrue(constraint.allowsExecution(actionB));
	}

	@Test
	public void allowsExecutionOfSingleActivityA() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		Action actionA = addToPlan(activityAProxy, 0, 0);
		addToPlan(activityBProxy, 1, 0);
		assertTrue(constraint.allowsExecution(actionA));
	}

	@Test
	public void doesNotAffectOtherActions() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 2);
		addToPlan(activityAProxy, 0, 300);
		addToPlan(activityCProxy, 0, 500);
		addToPlan(activityCProxy, 1, 500);
		addToPlan(activityBProxy, 3, 100);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertTrue(failures.isEmpty());
	}

	@Test
	public void getDescription() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		assertEquals("There has to be at least one day between the execution of 'Activity A' and 'Activity B'.", constraint
				.getDescription());

		constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 2);
		assertEquals("There have to be at least 2 days between the execution of 'Activity A' and 'Activity B'.", constraint
				.getDescription());

		constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 0);
		assertEquals("After executing 'Activity A' the execution of 'Activity B' has to be at the next day or later.", constraint
				.getDescription());
	}

	@Test
	public void getShortInfo() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		assertEquals("'Activity A' -> 1 day -> 'Activity B'", constraint.getShortInfo());

		constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 2);
		assertEquals("'Activity A' -> 2 days -> 'Activity B'", constraint.getShortInfo());

		constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 0);
		assertEquals("'Activity A' -> next day -> 'Activity B'", constraint.getShortInfo());
	}

	@Test
	public void ignoreCancelledActionA() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		Action actionA = addToPlan(activityAProxy, 0, 0);
		addToPlan(activityBProxy, 0, 300);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertFalse(failures.isEmpty());
		actionA.cancel();
		failures = constraint.validatePlan(game, null);
		assertTrue(failures.isEmpty());
	}

	@Test
	public void ignoreCancelledActionB() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		Action actionB = addToPlan(activityBProxy, 0, 300);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertFalse(failures.isEmpty());
		actionB.cancel();
		failures = constraint.validatePlan(game, null);
		assertTrue(failures.isEmpty());
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalActivityA() {
		new TimeLimitedNonResponseConstraint(null, activityConfigB, 1);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalActivityB() {
		new TimeLimitedNonResponseConstraint(activityConfigA, null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalDayAmount() {
		new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, -1);
	}

	@Test
	public void onlyActivityA() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertTrue(failures.isEmpty());
	}

	@Test
	public void permitExecutionActivityB() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		Action actionB = addToPlan(activityBProxy, 0, 300);
		assertFalse(constraint.allowsExecution(actionB));
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
		activityConfigC = new ActivityConfig("Activity C", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigC);
		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(activityConfigC);
	}

	@Test
	public void tooLessDaysBetweenActivities() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 2);
		addToPlan(activityAProxy, 0, 300);
		addToPlan(activityBProxy, 2, 100);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertFalse(failures.isEmpty());
	}

	@Test
	public void twoStartingActivities() {
		TimeLimitedNonResponseConstraint constraint = new TimeLimitedNonResponseConstraint(activityConfigA, activityConfigB, 1);
		addToPlan(activityAProxy, 0, 0);
		addToPlan(activityAProxy, 1, 0);
		addToPlan(activityBProxy, 1, 400);
		List<Failure> failures = constraint.validatePlan(game, null);
		assertFalse(failures.isEmpty());
	}
}
