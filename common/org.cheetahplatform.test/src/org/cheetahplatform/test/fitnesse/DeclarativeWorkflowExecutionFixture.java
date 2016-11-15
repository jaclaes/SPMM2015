/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import static org.cheetahplatform.test.fitnesse.DeclarativeFitnesseHelper.INSTANCE;

import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;

public class DeclarativeWorkflowExecutionFixture extends DeclarativeWorkflowFixture {
	public void cancel() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = cells.more.text();
		INSTANCE.cancel(activityName);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void canTerminate() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		boolean actual = INSTANCE.canTerminate();
		boolean expected = Boolean.parseBoolean(cells.more.text().trim());
		if (actual == expected) {
			right(cells.more);
		} else {
			wrong(cells.more, String.valueOf(actual));
			return;
		}
	}

	public void complete() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = cells.more.text();
		INSTANCE.complete(activityName);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void getActiveReminders() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		String exprectedString = cells.more.text().trim();
		String[] expectedReminders = exprectedString.split(",");

		List<IReminderInstance> reminders = INSTANCE.getActiveReminders();

		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (IReminderInstance reminder : reminders) {
			if (!first) {
				builder.append(",");
			}
			first = false;
			builder.append(reminder.getReminder().getName().trim());
		}

		if (reminders.isEmpty() && (!(expectedReminders.length == 1 && expectedReminders[0].equals("")))) {
			wrong(cells.more, builder.toString());
			return;
		}

		for (String expected : expectedReminders) {
			for (IReminderInstance iReminderInstance : reminders) {
				if (!expected.trim().equals(iReminderInstance.getReminder().getName().trim())) {
					wrong(cells.more, builder.toString());
					return;
				}
			}
		}
		right(cells.more);
	}

	public void getExecutableTasks() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		List<DeclarativeActivity> executableTasks = INSTANCE.getExecutableTasks();
		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		for (DeclarativeActivity declarativeActivity : executableTasks) {
			if (!first) {
				stringBuilder.append(",");
			}
			stringBuilder.append(declarativeActivity.getName());
			first = false;
		}

		String expected = cells.more.text();
		List<String> expectedActivities = Arrays.asList(expected.split(","));

		if (expectedActivities.size() != executableTasks.size()) {
			wrong(cells.more, stringBuilder.toString());
			return;
		}

		for (DeclarativeActivity activity : executableTasks) {
			if (!expectedActivities.contains(activity.getName())) {
				wrong(cells.more, stringBuilder.toString());
				return;
			}
		}

		right(cells.more);
	}

	public void launch() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = cells.more.text();
		INSTANCE.launch(activityName);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void setTime() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		Duration duration = extractDuration(cells.more.text());
		INSTANCE.setTime(duration);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);

	}

	@Override
	public void start() throws Throwable {
		INSTANCE.instantiateSchema();

		super.start();
	}
}
