/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.test.fitnesse.ui.FitnesseAgileJourneys;
import org.alaskasimulator.ui.agile.model.AgileAction;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.agile.policy.AddAgileActionCommand;
import org.junit.Test;


public class ActionMoveTest {
	@Test
	public void moveBookedAction() throws Exception {
		AgileCalendar calendar = FitnesseAgileJourneys.createSimpleJourneyToAlaska();
		Game game = calendar.getGameModel().getGame();
		Action action = game.getConfig().findActionConfigProxy("Short Hike").createAction();
		AddAgileActionCommand command = new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), action, new Time(game, 1, 0));
		command.execute();

		calendar.startJourney();
		boolean result = AgileAction.isValidStartTime(new Time(game, 0, 0), action);
		assertTrue("Should be valid start time, not booked yet.", result);

		game.getServiceProvider().getBookingService().createBooking(action);
		result = AgileAction.isValidStartTime(new Time(game, 1, 0), action);
		assertTrue("Should be moveable within the same day.", result);

		result = AgileAction.isValidStartTime(new Time(game, 0, 0), action);
		assertFalse("Should not be allowed to move the action to another day.", result);
	}

	@Test
	public void moveActionToPast() throws Exception {
		AgileCalendar calendar = FitnesseAgileJourneys.createSimpleJourneyToAlaska();
		Game game = calendar.getGameModel().getGame();
		calendar.startJourney();

		Action accommodation = game.getConfig().findActionConfigProxy("Hotel in Juneau").createAction();
		new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), accommodation, new Time(game, 0, 750)).execute();
		accommodation.execute();

		Action action = game.getConfig().findActionConfigProxy("Short Hike").createAction();
		new AddAgileActionCommand(calendar.getDays().get(1).getPlanningArea(), action, new Time(game, 1, 0)).execute();
		boolean result = AgileAction.isValidStartTime(new Time(game, 0, 0), action);
		assertFalse("Invalid, lays in the past.", result);

		result = AgileAction.isValidStartTime(new Time(game, 2, 0), action);
		assertTrue("Valid, future date.", result);

		result = AgileAction.isValidStartTime(new Time(game, 2, 0), action);
		assertTrue("Valid, future date.", result);
	}

	@Test
	public void moveAccommodation() throws Exception {
		AgileCalendar calendar = FitnesseAgileJourneys.createSimpleJourneyToAlaska();
		Game game = calendar.getGameModel().getGame();
		calendar.startJourney();

		Action accommodation = game.getConfig().findActionConfigProxy("Hotel in Juneau").createAction();
		new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), accommodation, new Time(game, 0, 750)).execute();

		boolean result = AgileAction.isValidStartTime(new Time(game, 0, 450), accommodation);
		assertFalse("Accommodations can only be inserted at the end of the day.", result);

		result = AgileAction.isValidStartTime(new Time(game, 0, 750), accommodation);
		assertTrue("Accommodations can only be inserted at the end of the day.", result);
	}

	@Test
	public void moveExecutedAction() throws Exception {
		AgileCalendar calendar = FitnesseAgileJourneys.createSimpleJourneyToAlaska();
		Game game = calendar.getGameModel().getGame();
		calendar.startJourney();

		Action action = game.getConfig().findActionConfigProxy("Short Hike").createAction();
		new AddAgileActionCommand(calendar.getDays().get(0).getPlanningArea(), action, new Time(game, 0, 0)).execute();
		action.execute();

		boolean result = AgileAction.isValidStartTime(new Time(game, 0, 180), action);

		assertFalse("Cannot move actions in final state.", result);
	}
}
