/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.alaskasimulator.core.Failure;
import org.alaskasimulator.core.runtime.Time;
import org.alaskasimulator.test.fitnesse.ui.AgileCalendarFixture.MyTypeAdapter;
import org.alaskasimulator.ui.agile.model.AgileAction;
import org.alaskasimulator.ui.agile.model.AgileBox;
import org.alaskasimulator.ui.agile.model.AgileCalendar;
import org.alaskasimulator.ui.agile.model.IAgileBox;
import org.alaskasimulator.ui.agile.model.ITimeBounded;
import org.alaskasimulator.ui.agile.model.PlanningArea;
import org.alaskasimulator.ui.agile.model.TimeBoundedStartTimeComparator;
import org.alaskasimulator.ui.agile.policy.ResizeAgileBoxCommand;
import org.eclipse.core.runtime.Assert;

import fit.ActionFixture;
import fit.Parse;

public abstract class AbstractAgileCalendarFixture extends ActionFixture {
	protected static AgileCalendar CALENDAR;

	public String structure(int day) {
		StringBuilder structure = new StringBuilder();
		PlanningArea planningArea = CALENDAR.getDays().get(day).getPlanningArea();
		structure(planningArea, structure);

		for (AgileAction action : planningArea.getAccommodations()) {
			structure.append(", ");
			structure.append(action.getAction().getActionConfig().getName());
			structure.append(' ');
			structure.append(action.getStartTime().getMinute());
		}

		return structure.toString();
	}

	private void structure(IAgileBox currentBox, StringBuilder structure) {
		List<ITimeBounded> children = new ArrayList<ITimeBounded>();
		children.addAll(currentBox.getBoxes());
		if (!(currentBox instanceof PlanningArea)) {
			children.addAll(currentBox.getActions());
		}
		Collections.sort(children, new TimeBoundedStartTimeComparator());

		boolean first = true;
		for (ITimeBounded bounded : children) {
			if (!first) {
				structure.append(", ");
			}

			if (bounded instanceof AgileBox) {
				structure.append("Box ");
				structure.append(bounded.getStartTime().getMinute());
				structure.append(':');
				structure.append(bounded.getEndTime().getMinute());
				structure.append('[');

				structure((IAgileBox) bounded, structure);

				structure.append(']');
				first = false;
			} else {
				structure.append(((AgileAction) bounded).getAction().getActionConfig().getName());
				structure.append(' ');
				structure.append(bounded.getStartTime().getMinute());
				first = false;
			}
		}
	}

	public int totalActions() {
		return CALENDAR.getAllContainedActions().size();
	}

	public int actions(int day) {
		return CALENDAR.getDays().get(day).getPlanningArea().getActions().size();
	}

	public int boxes(int day) {
		int boxCount = 0;
		List<IAgileBox> boxes = new ArrayList<IAgileBox>();
		boxes.addAll(CALENDAR.getDays().get(day).getPlanningArea().getBoxes());

		while (!boxes.isEmpty()) {
			IAgileBox box = boxes.get(boxes.size() - 1);
			boxes.remove(boxes.size() - 1);
			boxes.addAll(box.getBoxes());

			boxCount++;
		}

		return boxCount;
	}

	public void setEndTime(String input) {
		StringTokenizer tokenizer = new StringTokenizer(input, ",");
		Assert.isTrue(tokenizer.countTokens() == 3, "Wrong input, expected: day, boxId, newEndTime");

		int day = Integer.parseInt(tokenizer.nextToken().trim());
		AgileBox box = (AgileBox) parseBoxId(tokenizer, day);
		int minute = Integer.parseInt(tokenizer.nextToken().trim());

		new ResizeAgileBoxCommand(box, null, new Time(CALENDAR.getGameModel().getGame(), day, minute)).execute();
	}

	public void setStartTime(String input) {
		StringTokenizer tokenizer = new StringTokenizer(input, ",");
		Assert.isTrue(tokenizer.countTokens() == 3, "Wrong input, expected: day, boxId, newStartTime");

		int day = Integer.parseInt(tokenizer.nextToken().trim());
		AgileBox box = (AgileBox) parseBoxId(tokenizer, day);
		int minute = Integer.parseInt(tokenizer.nextToken().trim());

		new ResizeAgileBoxCommand(box, new Time(CALENDAR.getGameModel().getGame(), day, minute), null).execute();
	}

	@Override
	public void check() throws Throwable {
		Parse valueToCheck = cells.more.more;
		Assert.isNotNull(valueToCheck, "No value to check specified");

		valueToCheck = cells.more.more.more;
		if (valueToCheck == null) {
			super.check();
			return;
		}

		MyTypeAdapter adapter;
		Method theMethod = method(1);

		adapter = new MyTypeAdapter();
		adapter.method = theMethod;
		adapter.type = theMethod.getReturnType();
		adapter.parameterType = theMethod.getParameterTypes()[0];
		adapter.target = actor;

		Object param = cells.more.more.body;
		if (adapter.parameterType.equals(int.class)) {
			param = Integer.parseInt(cells.more.more.body);
		}

		adapter.params = new Object[] { param };
		adapter.fixture = actor;

		Parse checkValueCell = cells.more.more.more;
		check(checkValueCell, adapter);
	}

	protected IAgileBox parseBoxId(StringTokenizer tokenizer, int day) {
		if (tokenizer.hasMoreTokens()) {
			StringTokenizer boxIdTokenizer = new StringTokenizer(tokenizer.nextToken(), ";");
			List<Integer> id = new ArrayList<Integer>();

			while (boxIdTokenizer.hasMoreTokens()) {
				id.add(Integer.parseInt(boxIdTokenizer.nextToken()));
			}

			return CALENDAR.getDays().get(day).getPlanningArea().getBox(id);
		}

		return CALENDAR.getDays().get(day).getPlanningArea();
	}

	public int failures() {
		List<Failure> failures = CALENDAR.validate().getFailures();
		return failures.size();
	}
}
