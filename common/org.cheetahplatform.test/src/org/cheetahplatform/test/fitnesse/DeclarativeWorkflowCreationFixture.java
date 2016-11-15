/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import static org.cheetahplatform.test.fitnesse.DeclarativeFitnesseHelper.INSTANCE;

import org.cheetahplatform.common.date.CustomDateTimeSource;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;

public class DeclarativeWorkflowCreationFixture extends DeclarativeWorkflowFixture {

	public void createActivity() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = cells.more.text();
		INSTANCE.createActivity(activityName);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createAlternatePrecedenceConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createAlternatePrecedenceConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createChainedPrecedenceConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createChainedPrecedenceConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createChainedResponseConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createChainedResponseConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createChainedSuccessionConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createChainedSuccessionConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createCoexistenceConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createCoexistenceConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createEarlierstStartTimeConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activityName = cells.more.text();
		String timeString = cells.more.more.text();
		Duration duration = extractDuration(timeString);

		INSTANCE.createEarliestStartTimeConstraint(activityName, duration);

		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createExclusiveChoiceConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createExclusiveChoiceConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createInitConstraint() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		String activity = cells.more.text();

		INSTANCE.createInitConstraint(activity);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createLastConstraint() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();

		INSTANCE.createLastConstraint(activity1);
		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createLatestStartTimeConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activityName = cells.more.text();
		String timeString = cells.more.more.text();
		Duration duration = extractDuration(timeString);
		INSTANCE.createLatestStartTimeConstraint(activityName, duration);

		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMaxSelectConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String amount = cells.more.more.text();

		INSTANCE.createMaxSelectConstraint(activity1, amount);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMilestone() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String milestoneName = cells.more.text();
		Duration durationSinceStart = extractDuration(cells.more.more.text());

		INSTANCE.createMilestone(milestoneName, durationSinceStart);

		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMilestoneActivityReminder() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null || cells.more.more.more.more == null
				|| cells.more.more.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least six columns");
		}

		String activityName = cells.more.text();
		String milestoneName = cells.more.more.text();
		String reminderName = cells.more.more.more.text();
		String reminderText = cells.more.more.more.more.text();
		Duration duration = extractDuration(cells.more.more.more.more.more.text());

		INSTANCE.createMilestoneActivityReminder(activityName, milestoneName, reminderName, reminderText, duration);

		cells.more.more.more.more.more.more.addToBody("Ok!");
		right(cells.more.more.more.more.more.more);
	}

	public void createMinMaxSelectConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null || cells.more.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activity1 = cells.more.text();
		String minimum = cells.more.more.text();
		String maximum = cells.more.more.more.text();

		INSTANCE.createMinMaxSelectConstraint(activity1, minimum, maximum);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMinSelectConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String amount = cells.more.more.text();

		INSTANCE.createMinSelectConstraint(activity1, amount);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMOutOfNConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activities = cells.more.text();
		String minimum = cells.more.more.text();

		INSTANCE.createMOutOfNConstraint(activities, minimum);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMultiExclusiveChoiceConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activities = cells.more.text();
		String amount = cells.more.more.text();

		INSTANCE.createMultiExclusiveChoiceConstraint(activities, amount);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMultiPrecedenceConstraint() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least two columns");
		}

		String activities = cells.more.text();

		INSTANCE.createMultiPrecedenceConstraint(activities);
		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createMultiResponseConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String startActivities = cells.more.text();
		String endActivities = cells.more.more.text();

		INSTANCE.createMultiResponseConstraint(startActivities, endActivities);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMultiSuccessionConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String startActivities = cells.more.text();
		String endActivities = cells.more.more.text();

		INSTANCE.createMultiSuccessionConstraint(startActivities, endActivities);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createMutualExclusionConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createMutualExclusionConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createNegationResponseConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createNegationResponseConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createPrerequisiteConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createPrerequisiteConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createRespondedExistenceConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createRespondedExistenceConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createResponseConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createResponseConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createSuccessionConstraint() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String activity1 = cells.more.text();
		String activity2 = cells.more.more.text();

		INSTANCE.createSuccessionConstraint(activity1, activity2);
		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	@Override
	public void start() throws Throwable {
		DateTimeProvider.setDateTimeSource(new CustomDateTimeSource());
		INSTANCE.createProcessSchema();
		super.start();
	}
}
