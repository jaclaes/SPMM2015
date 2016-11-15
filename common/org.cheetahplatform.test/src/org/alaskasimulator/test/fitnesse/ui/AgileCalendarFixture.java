/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.fitnesse.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import org.alaskasimulator.core.runtime.Game;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.proxy.ActionConfigProxy;
import org.alaskasimulator.ui.agile.model.IAgileBox;
import org.alaskasimulator.ui.agile.policy.AddAgileActionCommand;
import org.eclipse.core.runtime.Assert;

import fit.TypeAdapter;

public class AgileCalendarFixture extends AbstractAgileCalendarFixture {
	static class MyTypeAdapter extends TypeAdapter {
		public Object[] params;
		public Class<?> parameterType;

		@Override
		public Object invoke() throws IllegalAccessException, InvocationTargetException {
			return method.invoke(target, params);
		}

		@Override
		public Object parse(String s) throws Exception {
			if (type.equals(String.class)) {
				return s;
			}

			return Integer.valueOf(s);
		}
	}

	public void schedule(String input) {
		StringTokenizer tokenizer = new StringTokenizer(input, ",");
		Assert.isTrue(tokenizer.countTokens() >= 3, "Malformed input, expected: ActionName,day,minute,target where target is optional.");

		String actionName = tokenizer.nextToken();
		int day = Integer.parseInt(tokenizer.nextToken());
		int minute = Integer.parseInt(tokenizer.nextToken());
		IAgileBox target = CALENDAR.getDays().get(day).getPlanningArea();

		target = parseBoxId(tokenizer, day);

		Game game = CALENDAR.getGameModel().getGame();
		ActionConfigProxy proxy = game.getConfig().findActionConfigProxy(actionName);
		Action action = proxy.createAction();

		new AddAgileActionCommand(target, action, new Time(game, day, minute)).execute();
	}

	public void startJourney() {
		CALENDAR.startJourney();
	}

	public void useJourney(String name) {
		if (name.equals("Simple Journey")) {
			CALENDAR = FitnesseAgileJourneys.createSimpleJourney();
		} else if (name.equals("Simple Journey to Alaska")) {
			CALENDAR = FitnesseAgileJourneys.createSimpleJourneyToAlaska();
		} else {
			throw new Error("Unknown journey: " + name);
		}

	}

}
