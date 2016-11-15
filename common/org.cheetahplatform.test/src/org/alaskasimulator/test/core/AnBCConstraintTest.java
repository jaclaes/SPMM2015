/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.buildtime.AccommodationConfig;
import org.alaskasimulator.core.buildtime.ActivityConfig;
import org.alaskasimulator.core.buildtime.Certainty;
import org.alaskasimulator.core.buildtime.DurationRange;
import org.alaskasimulator.core.buildtime.GameConfig;
import org.alaskasimulator.core.buildtime.LocalWeatherCharacteristics;
import org.alaskasimulator.core.buildtime.Location;
import org.alaskasimulator.core.buildtime.UtmCoordinate;
import org.alaskasimulator.core.buildtime.constraint.AnBCConstraint;
import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.action.ActivityAction;
import org.alaskasimulator.core.runtime.proxy.AccommodationConfigProxy;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.cheetahplatform.test.TestHelper;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Before;
import org.junit.Test;

public class AnBCConstraintTest {

	private ActivityConfig activityConfigA;
	private ActivityConfig activityConfigB;
	private ActivityConfig activityConfigC;
	private Game game;
	private ActionConfigProxy activityBProxy;
	private ActionConfigProxy activityCProxy;
	private ActionConfigProxy activityAProxy;
	private ActivityConfig activityConfigD;
	private ActionConfigProxy activityDProxy;
	private AccommodationConfig accommodationConfig;
	private AccommodationConfigProxy accommodationProxy;

	@Test
	public void affectsAllActions() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 4, activityConfigC);
		assertTrue(constraint.affects(activityConfigA));
		assertTrue(constraint.affects(activityConfigB));
		assertTrue(constraint.affects(activityConfigC));
	}

	@Test
	public void allOnSameDay() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 4, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addActivityToPlan(activityBProxy, 1, 200);
		TestHelper.addActivityToPlan(activityCProxy, 1, 600);

		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test
	public void allowsCompletion() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		assertFalse(constraint.allowsCompletion(game));
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		assertTrue(constraint.allowsCompletion(game));
	}

	@Test
	public void errorForABCC() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 3, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		TestHelper.addActivityToPlan(activityCProxy, 0, 300);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test
	public void exactAmountOfB() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 510);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());

	}

	@Test
	public void getDescription() {
		List<String> messages = new ArrayList<String>();
		messages
				.add("'Activity A' has to be followed by at least 2 and at most 2 occurrences of 'Activity B' and one final occurrence of 'Activity C'. All actions have to be executed at the same day.");
		messages
				.add("'Activity A' has to be followed by at most 2 occurrences of 'Activity B' and one final occurrence of 'Activity C'. All actions have to be executed at the same day.");
		messages
				.add("'Activity A' has to be followed by at least 2 occurrences of 'Activity B' and one final occurrence of 'Activity C'. All actions have to be executed at the same day.");
		messages
				.add("'Activity A' has to be followed by at least 2 and at most 4 occurrences of 'Activity B' and one final occurrence of 'Activity C'. All actions have to be executed at the same day.");
		messages
				.add("'Activity A' has to be followed by one final occurrence of 'Activity C'. All actions have to be executed at the same day.");

		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 2, activityConfigC);
		assertEquals(messages.get(0), constraint.getDescription());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, -1, 2, activityConfigC);
		assertEquals(messages.get(1), constraint.getDescription());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, -1, activityConfigC);
		assertEquals(messages.get(2), constraint.getDescription());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 4, activityConfigC);
		assertEquals(messages.get(3), constraint.getDescription());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, -1, -1, activityConfigC);
		assertEquals(messages.get(4), constraint.getDescription());
	}

	@Test
	public void getShortInfo() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 2, activityConfigC);
		assertEquals("'Activity A' -> 2..2 x 'Activity B' -> 'Activity C'", constraint.getShortInfo());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, -1, 2, activityConfigC);
		assertEquals("'Activity A' -> *..2 x 'Activity B' -> 'Activity C'", constraint.getShortInfo());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, -1, activityConfigC);
		assertEquals("'Activity A' -> 2..* x 'Activity B' -> 'Activity C'", constraint.getShortInfo());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 4, activityConfigC);
		assertEquals("'Activity A' -> 2..4 x 'Activity B' -> 'Activity C'", constraint.getShortInfo());

		constraint = new AnBCConstraint(activityConfigA, activityConfigB, -1, -1, activityConfigC);
		assertEquals("'Activity A' -> *..* x 'Activity B' -> 'Activity C'", constraint.getShortInfo());
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalActionA() {
		new AnBCConstraint(null, activityConfigB, 2, 4, activityConfigC);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalActionB() {
		new AnBCConstraint(activityConfigA, null, 2, 4, activityConfigC);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalActionC() {
		new AnBCConstraint(activityConfigA, activityConfigB, 2, 4, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalBoundCombination() {
		new AnBCConstraint(activityConfigA, activityConfigB, 3, 2, activityConfigC);
	}

	@Test
	public void illegalElement() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addActivityToPlan(activityDProxy, 0, 400);
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalLowerBound() {
		new AnBCConstraint(activityConfigA, activityConfigB, -2, 4, activityConfigC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalUpperBound() {
		new AnBCConstraint(activityConfigA, activityConfigB, 2, -4, activityConfigC);
	}

	@Test
	public void missingActivityC() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
	}

	@Test
	public void onlyBinPlan() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test
	public void onlyCinPlan() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test
	public void onlyOneError() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 2, 4, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addAccommodationToPlan(accommodationProxy, 0, game.getDayDuration() - 1);
		TestHelper.addActivityToPlan(activityBProxy, 1, 200);
		TestHelper.addActivityToPlan(activityCProxy, 1, 600);

		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertEquals(1, failures.size());
	}

	@Test
	public void onlyOneErrorForMultipleBs() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		assertEquals(1, failures.size());
	}

	@Test
	public void onlyOneErrorForMultipleBsAndC() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		assertEquals(1, failures.size());
	}

	@Test
	public void openLowerRange() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, -1, 3, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
	}

	@Test
	public void openUpperRange() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, -1, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
	}

	@Test
	public void permitExecutionOfBAfterCompleteConstraint() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		ActivityAction actionA = TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		ActivityAction actionB = TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		ActivityAction actionC = TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		ActivityAction illegalAction = TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		assertTrue(constraint.allowsExecution(actionA));
		assertTrue(constraint.allowsExecution(actionB));
		assertTrue(constraint.allowsExecution(actionC));
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(illegalAction, failure));
		assertEquals(illegalAction, failure.getFirstOrigin());
		assertEquals(Failure.TYPE_ANBC_CONSTRAINT, failure.getType());
	}

	@Test
	public void permitExecutionOfMultipleErroneousActionsB() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 3, activityConfigC);
		ActivityAction actionB1 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		ActivityAction actionB2 = TestHelper.addActivityToPlan(activityBProxy, 1, 200);
		assertFalse(constraint.allowsExecution(actionB1));
		assertFalse(constraint.allowsExecution(actionB2));
	}

	@Test
	public void permitExecutionOfMultipleErroneousActionsBAndC() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 3, activityConfigC);
		ActivityAction actionB1 = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		ActivityAction actionC1 = TestHelper.addActivityToPlan(activityCProxy, 1, 200);
		assertFalse(constraint.allowsExecution(actionB1));
		assertFalse(constraint.allowsExecution(actionC1));
	}

	@Test
	public void permitExecutionOfMultipleErroneousActionsC() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 3, activityConfigC);
		ActivityAction actionC1 = TestHelper.addActivityToPlan(activityCProxy, 0, 200);
		ActivityAction actionC2 = TestHelper.addActivityToPlan(activityCProxy, 1, 200);
		assertFalse(constraint.allowsExecution(actionC1));
		assertFalse(constraint.allowsExecution(actionC2));
	}

	@Test
	public void permitExecutionOfSingleElementA() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		Action action = TestHelper.addActivityToPlan(activityAProxy, 0, 200);
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(action, failure));
		assertEquals(action, failure.getFirstOrigin());
		assertEquals(Failure.TYPE_ANBC_CONSTRAINT, failure.getType());
	}

	@Test
	public void permitExecutionOfSingleElementB() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		Action action = TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		Failure failure = new Failure();
		assertFalse(constraint.allowsExecution(action, failure));
		assertEquals(action, failure.getFirstOrigin());
		assertEquals(Failure.TYPE_ANBC_CONSTRAINT, failure.getType());
	}

	@Test
	public void rangeOfActivitiesB() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 3, activityConfigC);
		TestHelper.addActivityToPlan(activityAProxy, 0, 0);
		TestHelper.addActivityToPlan(activityCProxy, 0, 600);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 100);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 300);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertTrue(failures.isEmpty());
		TestHelper.addActivityToPlan(activityBProxy, 0, 400);
		failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertFalse(failures.isEmpty());
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
		activityConfigD = new ActivityConfig("Activity D", 10.0, 100, new Certainty(1.0), 1.0, new DurationRange(100), 4);
		locationA.addActionConfig(activityConfigD);
		accommodationConfig = new AccommodationConfig("Acc", 100.0, 200.0, new Certainty(1.0), 1.0, -1);
		locationA.addActionConfig(accommodationConfig);

		game = new Game(gameConfig, "Player");
		game.startJourney();

		activityAProxy = game.getConfig().findActionConfigProxy(activityConfigA);
		activityBProxy = game.getConfig().findActionConfigProxy(activityConfigB);
		activityCProxy = game.getConfig().findActionConfigProxy(activityConfigC);
		activityDProxy = game.getConfig().findActionConfigProxy(activityConfigD);
		accommodationProxy = (AccommodationConfigProxy) game.getConfig().findActionConfigProxy(accommodationConfig);
	}

	@Test
	public void twoMessagesForBCC() {
		AnBCConstraint constraint = new AnBCConstraint(activityConfigA, activityConfigB, 1, 2, activityConfigC);
		TestHelper.addActivityToPlan(activityBProxy, 0, 200);
		TestHelper.addActivityToPlan(activityCProxy, 0, 400);
		TestHelper.addActivityToPlan(activityCProxy, 0, 500);
		List<Failure> failures = constraint.validatePlan(game, new HashMap<String, Object>());
		assertEquals(2, failures.size());
	}
}
