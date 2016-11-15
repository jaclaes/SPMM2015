/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. Any
 * use, reproduction or distribution of the program constitutes recipient's
 * acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.ui;

import static org.junit.Assert.assertEquals;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.test.core.TestGame;
import org.alaskasimulator.ui.AbstractValidator;
import org.alaskasimulator.ui.agile.logging.AgileCalendarLogger;
import org.alaskasimulator.ui.agile.model.AgileAction;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.model.AgileCalendarValidator;
import org.alaskasimulator.ui.model.GameConfigUiModel;
import org.alaskasimulator.ui.model.GameUiModel;
import org.alaskasimulator.ui.plandriven.model.IJourneyValidator.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AgileCalendarValidatorTest {

	private AgileCalendar calendar;
	private AbstractValidator validator;
	private TestGame testGame;

	@After
	public void after() {
		calendar.getLogger().close();
	}

	@Before
	public void before() {
		testGame = new TestGame();
		testGame.setBudget(1000);
		testGame.setDayDuration(720);
		testGame.setDuration(120);
		testGame.setJourneyDuration(7);
		testGame.setAvailabilityActivity1(1.0);
		testGame.createGameConfig();
		Game game = testGame.createGame();
		GameUiModel model = new GameUiModel(game, new GameConfigUiModel(testGame.getConfig(), null, 0));
		calendar = new AgileCalendar(model, new AgileCalendarLogger(game));

		validator = new AgileCalendarValidator();
		validator.init(calendar);
	}

	@Test
	public void multipleConflictingActivites() {
		ValidationResult result = validator.validate();
		assertEquals("Should have no failures.", 0, result.getFailures().size());

		// add activity to first day
		ActionConfigProxy actionConfigProxy = testGame.getGame().getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName());
		Action activity = actionConfigProxy.createAction();
		Time time = new Time(testGame.getGame(), 0, 0);
		activity.setStartTime(time);
		testGame.getGame().getPlan().insertPlanItem(activity);
		AgileAction action1 = new AgileAction(activity, time);
		calendar.getDays().get(0).getPlanningArea().addAction(action1, false);

		// another not overlapping activity
		ActionConfigProxy actionConfigProxy2 = testGame.getGame().getConfig()
				.findActionConfigProxy(testGame.getActivityConfig2().getName());
		Action activity2 = actionConfigProxy2.createAction();
		Time time2 = new Time(testGame.getGame(), 0, 70);
		activity2.setStartTime(time2);
		testGame.getGame().getPlan().insertPlanItem(activity2);
		calendar.getDays().get(1).getPlanningArea().addAction(new AgileAction(activity2, time2), false);

		ActionConfigProxy actionConfigProxy3 = testGame.getGame().getConfig()
				.findActionConfigProxy(testGame.getActivityConfig3().getName());
		Action activity3 = actionConfigProxy3.createAction();
		Time time3 = new Time(testGame.getGame(), 0, 80);
		activity3.setStartTime(time3);
		testGame.getGame().getPlan().insertPlanItem(activity3);
		calendar.getDays().get(1).getPlanningArea().addAction(new AgileAction(activity3, time3), false);

		activity.getGame().startJourney();
		activity.execute();
		result = validator.validate();
		assertEquals("Conflicting Activites.", 5, result.getFailures().size());
	}

	@Test
	public void validateConflictingParallelActions() throws Exception {
		ValidationResult result = validator.validate();
		assertEquals("Should have no failures.", 0, result.getFailures().size());

		// add activity to first day
		ActionConfigProxy actionConfigProxy = testGame.getGame().getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName());
		Action activity = actionConfigProxy.createAction();
		Time time = new Time(testGame.getGame(), 0, 0);
		activity.setStartTime(time);
		testGame.getGame().getPlan().insertPlanItem(activity);
		AgileAction action1 = new AgileAction(activity, time);
		calendar.getDays().get(0).getPlanningArea().addAction(action1, false);

		// another not overlapping activity
		ActionConfigProxy actionConfigProxy2 = testGame.getGame().getConfig()
				.findActionConfigProxy(testGame.getActivityConfig2().getName());
		Action activity2 = actionConfigProxy2.createAction();
		Time time2 = new Time(testGame.getGame(), 0, 130);
		activity2.setStartTime(time2);
		testGame.getGame().getPlan().insertPlanItem(activity2);
		calendar.getDays().get(1).getPlanningArea().addAction(new AgileAction(activity2, time2), false);

		ActionConfigProxy actionConfigProxy3 = testGame.getGame().getConfig()
				.findActionConfigProxy(testGame.getActivityConfig3().getName());
		Action activity3 = actionConfigProxy3.createAction();
		Time time3 = new Time(testGame.getGame(), 0, 180);
		activity3.setStartTime(time3);
		testGame.getGame().getPlan().insertPlanItem(activity3);
		calendar.getDays().get(1).getPlanningArea().addAction(new AgileAction(activity3, time3), false);

		activity.getGame().startJourney();
		activity.execute();
		result = validator.validate();
		assertEquals(
				"Should only have accommodation failures, as the first 2 activites are not overlapping and activity 2 is not executed yet.",
				1, result.getFailures().size());

		activity2.execute();
		result = validator.validate();
		assertEquals("Conflicting Activites.", 3, result.getFailures().size());
	}

	@Test
	public void validateEmptyJourney() throws Exception {
		ValidationResult result = validator.validate();
		assertEquals("Should have no failures.", 0, result.getFailures().size());
	}

	@Test
	public void validateNoAccommodation() throws Exception {
		ValidationResult result = validator.validate();
		assertEquals("Should have no failures.", 0, result.getFailures().size());

		// add activity to first day
		Action activity = testGame.getGame().getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		Time time = new Time(testGame.getGame(), 0, 0);
		calendar.getDays().get(0).getPlanningArea().addAction(new AgileAction(activity, time), false);
		result = validator.validate();
		assertEquals("Should have no warnings yet.", 0, result.getFailures().size());

		// another activity for the second day
		Action activity2 = testGame.getGame().getConfig().findActionConfigProxy(testGame.getActivityConfig1().getName()).createAction();
		Time time2 = new Time(testGame.getGame(), 1, 0);
		calendar.getDays().get(1).getPlanningArea().addAction(new AgileAction(activity2, time2), false);
		result = validator.validate();

		assertEquals("Should have one missing accommodation.", 1, result.getFailures().size());
	}

}
