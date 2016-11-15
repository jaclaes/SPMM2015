package org.cheetahplatform.test.fitnesse;

import static org.cheetahplatform.test.fitnesse.ImperativeFitnessHelper.INSTANCE;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.runtime.AbstractImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateBindingBoxInstance;
import org.cheetahplatform.core.imperative.runtime.LateModelingBoxInstance;

import fit.ActionFixture;

public class ImperativeWorkflowExecutionFixture extends ActionFixture {

	private static final String LOG_ENTRY_SEPARATOR = ";";
	private static final String LOG_ENTRY_PART_SEPARATOR = ",";
	private boolean stop;

	private String buildRepresentation(List<AuditTrailEntry> logEntries) {
		boolean first = true;

		StringBuilder builder = new StringBuilder();
		for (AuditTrailEntry logEntry : logEntries) {
			if (!first) {
				builder.append(LOG_ENTRY_SEPARATOR);
			}
			first = false;
			builder.append(logEntry.getEventType());
			builder.append(LOG_ENTRY_PART_SEPARATOR);
			INodeInstance node = INSTANCE.getNodeInstance(logEntry.getWorkflowModeleElementAsLong());
			if (node == null) {
				ImperativeProcessInstance processInstance = INSTANCE.getProcessInstance();
				if (processInstance.getCheetahId() == logEntry.getWorkflowModeleElementAsLong()) {
					builder.append(processInstance.getSchema().getName());
				} else {
					throw new IllegalStateException("Unknown id");
				}
				continue;
			}
			builder.append(node.getNode().getName());

			// if (!logEntry.getAttributes().isEmpty()) {
			// builder.append(LOG_ENTRY_PART_SEPARATOR);
			// Set<String> keys = logEntry.getAttributeNames();
			// List<String> sortedKeys = new ArrayList<String>(keys);
			// Collections.sort(sortedKeys);
			// for (String key : sortedKeys) {
			// builder.append(key);
			// builder.append("=");
			// builder.append(logEntry.getAttribute(key));
			// }
			// }
		}
		return builder.toString();
	}

	public void cancel() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());
		INSTANCE.cancel(activityName, occurrence);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void checkAbsence() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());
		boolean absent = INSTANCE.isAbsent(activityName, occurrence);

		if (absent) {
			cells.more.more.addToBody("Ok!");
			right(cells.more.more);
		} else {
			wrong(cells.more.more, "Could find an instance");
		}
	}

	public void checkActiveActivities() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		String activities = cells.more.text();
		StringTokenizer tokenizer = new StringTokenizer(activities, ",");

		List<String> expectedActivities = new ArrayList<String>();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			expectedActivities.add(token);
		}

		List<INodeInstance> activeTasks = INSTANCE.getActiveActivities();
		Collections.sort(activeTasks, new Comparator<INodeInstance>() {

			@Override
			public int compare(INodeInstance o1, INodeInstance o2) {
				String name1 = o1.getNode().getName();
				String name2 = o2.getNode().getName();
				return Collator.getInstance().compare(name1, name2);
			}
		});

		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		for (INodeInstance activityInstance : activeTasks) {
			if (!first) {
				stringBuilder.append(",");
			}
			first = false;
			stringBuilder.append(activityInstance.getNode().getName());
		}

		if (expectedActivities.size() != activeTasks.size()) {
			wrong(cells.more, stringBuilder.toString());
			return;
		}

		for (INodeInstance activityInstance : activeTasks) {
			if (!expectedActivities.contains(activityInstance.getNode().getName().trim())) {
				wrong(cells.more, stringBuilder.toString());
				return;
			}
		}

		right(cells.more);
	}

	private void checkActiveBoxes(List<? extends AbstractImperativeNodeInstance> boxes) {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		String sequences = cells.more.text();
		StringTokenizer tokenizer = new StringTokenizer(sequences, ",");
		List<String> expectedSequences = new ArrayList<String>();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			expectedSequences.add(token);
		}

		List<AbstractImperativeNodeInstance> activeSequences = new ArrayList<AbstractImperativeNodeInstance>(boxes);
		Collections.sort(activeSequences, new Comparator<AbstractImperativeNodeInstance>() {
			@Override
			public int compare(AbstractImperativeNodeInstance o1, AbstractImperativeNodeInstance o2) {
				return Collator.getInstance().compare(o1.getNode().getName(), o2.getNode().getName());
			}
		});

		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		for (AbstractImperativeNodeInstance instance : activeSequences) {
			if (!first) {
				stringBuilder.append(",");
			}
			first = false;
			stringBuilder.append(instance.getNode().getName());
		}

		if (expectedSequences.size() != activeSequences.size()) {
			wrong(cells.more, stringBuilder.toString());
			return;
		}

		for (AbstractImperativeNodeInstance instance : activeSequences) {
			if (!expectedSequences.contains(instance.getNode().getName().trim())) {
				wrong(cells.more, stringBuilder.toString());
				return;
			}
		}

		right(cells.more);
	}

	public void checkActiveLateBindingBoxes() {
		List<LateBindingBoxInstance> boxes = INSTANCE.getActiveLateBindingBoxes();

		checkActiveBoxes(boxes);
	}

	public void checkActiveLateModelingBoxes() {
		List<LateModelingBoxInstance> boxes = INSTANCE.getActiveLateModelingBoxes();

		checkActiveBoxes(boxes);
	}

	public void checkLog() {
		if (cells.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}
		List<AuditTrailEntry> newLogEntries = INSTANCE.getNewLogEntries();

		String[] logEntries = cells.more.text().trim().split(LOG_ENTRY_SEPARATOR);

		if (newLogEntries.size() != logEntries.length) {
			wrong(cells.more, buildRepresentation(newLogEntries));
			return;
		}

		for (int i = 0; i < logEntries.length; i++) {
			String entry = logEntries[i];

			String[] expected = entry.trim().split(LOG_ENTRY_PART_SEPARATOR);

			if (expected.length < 2) {
				wrong(cells.more, buildRepresentation(newLogEntries));
				return;
			}

			if (!expected[0].equals(newLogEntries.get(i).getEventType().toString())) {
				wrong(cells.more, buildRepresentation(newLogEntries));
				return;
			}

			List<IImperativeNodeInstance> nodes = INSTANCE.getNode(expected[1]);
			if (nodes.isEmpty()) {
				ImperativeProcessInstance processInstance = INSTANCE.getProcessInstance();
				if (processInstance.getCheetahId() != newLogEntries.get(i).getWorkflowModeleElementAsLong()) {
					wrong(cells.more, buildRepresentation(newLogEntries));
					return;
				}
			} else {
				boolean found = false;
				for (IImperativeNodeInstance iImperativeNodeInstance : nodes) {
					if (iImperativeNodeInstance.getCheetahId() == newLogEntries.get(i).getWorkflowModeleElementAsLong())
						found = true;
				}
				if (!found) {
					wrong(cells.more, buildRepresentation(newLogEntries));
					return;
				}
			}
		}

		right(cells.more);
	}

	public void checkState() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());
		String expectedState = cells.more.more.text();

		if (INSTANCE.isValidState(activityName, occurrence, expectedState)) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, INSTANCE.getState(activityName, occurrence).getName());
		}
	}

	public void checkSuccessorCount() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());
		int expectedCount = Integer.parseInt(cells.more.more.text());

		int actualCount = INSTANCE.getSuccessorCount(activity, occurrence);
		if (actualCount == expectedCount) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, String.valueOf(actualCount));
		}
	}

	public void complete() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());

		if (stop) {
			System.out.println("stop");
		}
		INSTANCE.complete(activityName, occurrence);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createActivity() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = cells.more.text();
		INSTANCE.createActivity(activityName);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void launch() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());
		if (stop) {
			System.out.println();
		}
		INSTANCE.launch(activity, occurrence);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void selectActivitiesFromBox() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String name = cells.more.text();
		List<String> toSelect = Arrays.asList(cells.more.more.text().split(","));
		INSTANCE.selectActivitiesFromBox(name, toSelect);
		right(cells.more.more.more);
		cells.more.more.more.addToBody("Ok!");
	}

	public void selectSequence() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String boxName = cells.more.text();
		int number = Integer.parseInt(cells.more.more.text());
		INSTANCE.selectSequence(boxName, number);
	}

	public void skip() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity = ImperativeFitnessHelper.parseName(cells.more.text());
		int occurrence = ImperativeFitnessHelper.parseOccurrence(cells.more.text());
		INSTANCE.skip(activity, occurrence);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	@Override
	public void start() throws Throwable {
		INSTANCE.instantiateSchema();

		super.start();
	}

	public void stop() {
		stop = true;
	}
}