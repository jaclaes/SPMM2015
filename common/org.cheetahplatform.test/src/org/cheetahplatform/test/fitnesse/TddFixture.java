/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ActivityLayouter;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.core.runtime.Assert;

import fit.ActionFixture;
import fit.Parse;

public class TddFixture extends ActionFixture {
	private DeclarativeProcessSchema schema;
	private DeclarativeProcessInstance instance;
	private Workspace workspace;
	private Day day;
	private ActivityLayouter layouter;

	public TddFixture() {
		schema = new DeclarativeProcessSchema();
		instance = schema.instantiate();
		workspace = new Workspace(schema);
		day = workspace.addDay(new Date(1970, 0, 1));
	}

	public void addActivity() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null || cells.more.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least five columns");
		}

		DeclarativeActivity activity = schema.createActivity(cells.more.text());
		DateTime start = parse(cells.more.more, true);
		DateTime end = parse(cells.more.more.more, false);
		int minutes = new TimeSlot(start, end).getDurationInMinutes();
		activity.getExpectedDuration().setHour(0);
		activity.getExpectedDuration().setMinute(minutes);
		IDeclarativeNodeInstance activityInstance = activity.instantiate(instance);
		activityInstance.setStartTime(start);
		workspace.addActivityInstance(activity, start);

		cells.more.more.more.more.addToBody("Ok!");
		right(cells.more.more.more.more);
	}

	private Activity findActivity(String name) {
		Activity toCheck = null;
		for (Activity instance : day.getPlanningArea().getActivities()) {
			if (instance.getActivity().getNode().getName().equals(name)) {
				toCheck = instance;
				break;
			}
		}
		Assert.isNotNull(toCheck, "No activity found for name :" + name);
		return toCheck;
	}

	public void height() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		String[] parts = cells.more.more.text().split("h");
		int expected = Integer.parseInt(parts[0]) * DayTimeLineFigure.HOUR_HEIGHT;
		if (parts.length > 1) {
			expected += Integer.parseInt(parts[1]) * DayTimeLineFigure.HOUR_HEIGHT / 60.0;
		}

		Activity toCheck = findActivity(name);
		int actual = toCheck.getHeight(day);
		if (expected == actual) {
			cells.more.more.addToBody(", in pixel: " + expected);
			right(cells.more.more);
		} else {
			cells.more.more.addToBody(", in pixel: " + expected);
			wrong(cells.more.more, String.valueOf(toCheck.getHeight(day) + ", in hours: " + (float) toCheck.getHeight(day)
					/ DayTimeLineFigure.HOUR_HEIGHT));
		}
	}

	public void layout() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		layouter = new ActivityLayouter(day.getPlanningArea());
		layouter.layout();

		cells.more.addToBody("Ok!");
		right(cells.more);
	}

	public void maxParallel() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		int expected = Integer.parseInt(cells.more.text());
		if (expected == layouter.getParallelMax()) {
			right(cells.more);
		} else {
			wrong(cells.more, String.valueOf(layouter.getParallelMax()));
		}
	}

	public void parallelCount() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		DateTime time = parse(cells.more, true);
		int expected = Integer.parseInt(cells.more.more.text());
		int actual = layouter.getParallelCount(time);

		if (expected == actual) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, String.valueOf(actual));
		}
	}

	private DateTime parse(Parse parse, boolean inclusive) {
		String content = parse.text();
		int separatorIndex = content.indexOf(':');
		int hour = Integer.parseInt(content.substring(0, separatorIndex));
		int minute = Integer.parseInt(content.substring(separatorIndex + 1, content.length()));

		return new DateTime(hour, minute, inclusive);
	}

	public void width() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		int expected = Integer.parseInt(cells.more.more.text()) * layouter.getSlotWidth();

		Activity toCheck = findActivity(name);

		if (expected == toCheck.getWidth(day)) {
			cells.more.more.addToBody(", in pixel: " + toCheck.getWidth(day));
			right(cells.more.more);
		} else {
			wrong(cells.more.more, String.valueOf(toCheck.getWidth(day) / layouter.getSlotWidth()));
		}
	}

	public void x() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		int expected = Integer.parseInt(cells.more.more.text()) * layouter.getSlotWidth();

		Activity toCheck = findActivity(name);

		if (expected == toCheck.getX(day)) {
			cells.more.more.addToBody(", in pixel: " + expected);
			right(cells.more.more);
		} else {
			cells.more.more.addToBody(", in pixel: " + expected);
			wrong(cells.more.more, String.valueOf(toCheck.getX(day) + ", in slots: " + toCheck.getX(day) / layouter.getSlotWidth()));
		}
	}

	public void y() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		String[] parts = cells.more.more.text().split("h");
		int expected = Integer.parseInt(parts[0]) * DayTimeLineFigure.HOUR_HEIGHT;
		if (parts.length > 1) {
			expected += Float.parseFloat(parts[1]) * DayTimeLineFigure.HOUR_HEIGHT / 60.0;
		}

		expected += DayTimeLineFigure.FIRST_HOUR_TO_BE_DISPLAYED * DayTimeLineFigure.HOUR_HEIGHT;
		Activity toCheck = findActivity(name);
		int actual = toCheck.getY(day);

		if (expected == actual) {
			cells.more.more.addToBody(", in pixel: " + expected);
			right(cells.more.more);
		} else {
			wrong(cells.more.more, String.valueOf(actual));
		}
	}

}
