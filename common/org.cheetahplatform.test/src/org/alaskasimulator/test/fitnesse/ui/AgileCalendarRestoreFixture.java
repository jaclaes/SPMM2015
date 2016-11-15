/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.ui;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.core.runtime.proxy.LocationProxy;
import org.alaskasimulator.ui.agile.logging.AgileCalendarJourneyRestorer;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.editpart.UiUtils;
import org.alaskasimulator.ui.logging.AbstractLogRestoreCommand;
import org.eclipse.core.runtime.Assert;

public class AgileCalendarRestoreFixture extends AbstractAgileCalendarFixture {
	private ListIterator<AbstractLogRestoreCommand> commands;
	private AgileCalendarJourneyRestorer restorer;

	public String actionConfig(String name) {
		ActionConfigProxy action = getGame().getConfig().findActionConfigProxy(name);
		return action.getName() + "," + action.getCost() + "," + action.getMaxBusinessValue() + "," + action.getAvailability();
	}

	public int actionConfigs() {
		Set<ActionConfigProxy> actions = new HashSet<ActionConfigProxy>();

		for (LocationProxy location : getGame().getConfig().getLocations()) {
			actions.addAll(location.getActionConfigs());
		}

		return actions.size();
	}

	public String actionState(String input) {
		StringTokenizer tokenizer = new StringTokenizer(input, ",");
		Assert.isTrue(tokenizer.countTokens() >= 3, "Expected: ActionName,day,minute");

		String actionName = tokenizer.nextToken().replaceAll("&lt;", "<");
		actionName = actionName.replaceAll("&gt;", ">");
		int day = Integer.parseInt(tokenizer.nextToken());
		int minute = Integer.parseInt(tokenizer.nextToken());
		Time time = new Time(getGame(), day, minute);
		int skip = 0;
		if (tokenizer.hasMoreTokens()) {
			skip = Integer.parseInt(tokenizer.nextToken());
		}

		for (Action action : getGame().getPlan().getActionsInPlan()) {
			if (action.getActionConfig().getName().equals(actionName) && action.getStartTime().compareTo(time) == 0) {
				if (skip > 0) {
					skip--;
					continue;
				}

				if (UiUtils.existsBooking(action)) {
					return action.getState().toString() + ", Booked";
				}
				return action.getState().toString();
			}
		}

		throw new IllegalArgumentException("Unknown action.");
	}

	public double businessValue() {
		return (double) Math.round(getGame().getPlan().getBusinessValue() * 10) / 10;
	}

	public int countCommands() {
		return restorer.getRestoreCommandList().size();
	}

	public void executeCommand() {
		commands.next().execute();
	}

	public double expectedBusinessValue() {
		return (double) Math.round(getGame().getPlan().getExpectedBusinessValue() * 10) / 10;
	}

	public double expectedExpenses() {
		return getGame().getPlan().getExpectedExpenses();
	}

	public double expenses() {
		return getGame().getPlan().getExpenses();
	}

	public String gameState() {
		return getGame().getGameState().toString();
	}

	private Game getGame() {
		return CALENDAR.getGameModel().getGame();
	}

	public void load(String input) throws Exception {
		org.cheetahplatform.test.TestHelper.setLocaleToEnglish();
		restorer = FitnesseAgileJourneys.loadJourney(input);
		commands = restorer.getRestoreCommandList().listIterator();
		CALENDAR = (AgileCalendar) restorer.getJourneyToRestore();
	}

	public String location() {
		return getGame().getCurrentLocation().getName();
	}

	public int restoredCommands() {
		return commands.nextIndex();
	}

	public String time() {
		Time time = getGame().getCurrentTime();
		if (time.getDay() == Time.UPFRONT_PLANNING_DAY) {
			return "Upfront";
		}

		return time.getDay() + "," + time.getMinute();
	}

}
