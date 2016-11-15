/*******************************************************************************

 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.cheetahplatform.common.date.CustomDateTimeSource;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.declarative.constraint.AbstractDeclarativeConstraintWithOneActivity;
import org.cheetahplatform.core.declarative.constraint.AlternatePrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.ChainedPrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.ChainedResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.ChainedSuccessionConstraint;
import org.cheetahplatform.core.declarative.constraint.CoexistenceConstraint;
import org.cheetahplatform.core.declarative.constraint.EarliestStartTimeConstraint;
import org.cheetahplatform.core.declarative.constraint.ExclusiveChoiceConstraint;
import org.cheetahplatform.core.declarative.constraint.InitConstraint;
import org.cheetahplatform.core.declarative.constraint.LastConstraint;
import org.cheetahplatform.core.declarative.constraint.LatestStartTimeConstraint;
import org.cheetahplatform.core.declarative.constraint.MOutOfNConstraint;
import org.cheetahplatform.core.declarative.constraint.MaxSelectConstraint;
import org.cheetahplatform.core.declarative.constraint.MinSelectConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiExclusiveChoiceConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiPrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiSuccessionConstraint;
import org.cheetahplatform.core.declarative.constraint.MutualExclusionConstraint;
import org.cheetahplatform.core.declarative.constraint.NegationResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.RespondedExistenceConstraint;
import org.cheetahplatform.core.declarative.constraint.ResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.SelectionConstraint;
import org.cheetahplatform.core.declarative.constraint.SuccessionConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.IReminder;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.modeling.MilestoneActivityReminder;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.core.declarative.runtime.IReminderInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;

public class DeclarativeFitnesseHelper {
	private final Map<String, DeclarativeActivity> nodes = new HashMap<String, DeclarativeActivity>();
	private final Map<String, Milestone> milestones = new HashMap<String, Milestone>();
	public static DeclarativeFitnesseHelper INSTANCE = new DeclarativeFitnesseHelper();
	private DeclarativeProcessSchema processSchema;
	private DeclarativeProcessInstance processInstance;

	public void cancel(String activityName) {
		DeclarativeActivity node = nodes.get(activityName);
		List<IDeclarativeNodeInstance> nodeInstances = processInstance.getNodeInstances(node);
		for (IDeclarativeNodeInstance instance : nodeInstances) {
			if (instance.getState().equals(INodeInstanceState.LAUNCHED)) {
				instance.cancel();
				return;
			}
		}

		throw new IllegalStateException("Unable to find an activity named " + activityName + " that is in state "
				+ INodeInstanceState.LAUNCHED);
	}

	public boolean canTerminate() {
		return processInstance.canTerminate().getResult();
	}

	public void complete(String activityName) {
		DeclarativeActivity node = nodes.get(activityName);
		List<IDeclarativeNodeInstance> nodeInstances = processInstance.getNodeInstances(node);
		for (IDeclarativeNodeInstance instance : nodeInstances) {
			if (instance.getState().equals(INodeInstanceState.LAUNCHED)) {
				instance.complete();
				return;
			}
		}

		throw new IllegalStateException("Unable to find an activity named " + activityName + " that is in state "
				+ INodeInstanceState.LAUNCHED);
	}

	public void createActivity(String activityName) {
		DeclarativeActivity activity = processSchema.createActivity(activityName);
		nodes.put(activityName, activity);
	}

	public void createAlternatePrecedenceConstraint(String activityName1, String activityName2) {
		DeclarativeActivity activity1 = nodes.get(activityName1);
		DeclarativeActivity activity2 = nodes.get(activityName2);
		AlternatePrecedenceConstraint constraint = new AlternatePrecedenceConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createChainedPrecedenceConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		ChainedPrecedenceConstraint constraint = new ChainedPrecedenceConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createChainedResponseConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		ChainedResponseConstraint constraint = new ChainedResponseConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createChainedSuccessionConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		ChainedSuccessionConstraint constraint = new ChainedSuccessionConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createCoexistenceConstraint(String activityName1, String activityName2) {
		DeclarativeActivity activity1 = nodes.get(activityName1);
		DeclarativeActivity activity2 = nodes.get(activityName2);
		CoexistenceConstraint constraint = new CoexistenceConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createEarliestStartTimeConstraint(String activityName, Duration duration) {
		DeclarativeActivity node = nodes.get(activityName);
		EarliestStartTimeConstraint constraint = new EarliestStartTimeConstraint(node, duration);
		processSchema.addConstraint(constraint);
	}

	public void createExclusiveChoiceConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		ExclusiveChoiceConstraint constraint = new ExclusiveChoiceConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createInitConstraint(String activityName) {
		DeclarativeActivity activity = nodes.get(activityName);
		InitConstraint constraint = new InitConstraint(activity);
		processSchema.addConstraint(constraint);
	}

	public void createLastConstraint(String activityName) {
		DeclarativeActivity activity = nodes.get(activityName);
		LastConstraint constraint = new LastConstraint(activity);
		processSchema.addConstraint(constraint);
	}

	public void createLatestStartTimeConstraint(String activityName, Duration duration) {
		DeclarativeActivity node = nodes.get(activityName);
		AbstractDeclarativeConstraintWithOneActivity constraint = new LatestStartTimeConstraint(node, duration);
		processSchema.addConstraint(constraint);
	}

	public void createMaxSelectConstraint(String activityName, String amountString) {
		DeclarativeActivity activity = nodes.get(activityName);
		int amount = Integer.parseInt(amountString);
		SelectionConstraint constraint = new MaxSelectConstraint(activity, amount);
		processSchema.addConstraint(constraint);
	}

	public void createMilestone(String milestoneName, Duration durationSinceStart) {
		Milestone milestone = new Milestone(milestoneName, durationSinceStart);
		milestones.put(milestoneName, milestone);
		processSchema.addMilestone(milestone);
	}

	public void createMilestoneActivityReminder(String activityName, String milestoneName, String name, String reminderText,
			Duration duration) {
		DeclarativeActivity activity = nodes.get(activityName);
		Milestone milestone = milestones.get(milestoneName);
		IReminder reminder = new MilestoneActivityReminder(activity, milestone, name, reminderText, duration);

		processSchema.addReminder(reminder);
	}

	public void createMinMaxSelectConstraint(String activityName, String minimumString, String maximumString) {
		DeclarativeActivity activity = nodes.get(activityName);
		int minimum = Integer.parseInt(minimumString);
		int maximum = Integer.parseInt(maximumString);
		SelectionConstraint constraint = new SelectionConstraint(activity, minimum, maximum);
		processSchema.addConstraint(constraint);
	}

	public void createMinSelectConstraint(String activityName, String amountString) {
		DeclarativeActivity activity = nodes.get(activityName);
		int amount = Integer.parseInt(amountString);
		SelectionConstraint constraint = new MinSelectConstraint(activity, amount);
		processSchema.addConstraint(constraint);
	}

	public void createMOutOfNConstraint(String activitiesString, String minimumString) {
		int minimum = Integer.parseInt(minimumString);
		StringTokenizer tokenizer = new StringTokenizer(activitiesString, ",");
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			DeclarativeActivity activity = nodes.get(token);
			activities.add(activity);
		}

		MOutOfNConstraint constraint = new MOutOfNConstraint(activities, minimum);
		processSchema.addConstraint(constraint);
	}

	public void createMultiExclusiveChoiceConstraint(String activitiesString, String amountString) {
		int amount = Integer.parseInt(amountString);
		StringTokenizer tokenizer = new StringTokenizer(activitiesString, ",");
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			DeclarativeActivity activity = nodes.get(token);
			activities.add(activity);
		}

		MultiExclusiveChoiceConstraint constraint = new MultiExclusiveChoiceConstraint(activities, amount);
		processSchema.addConstraint(constraint);
	}

	public void createMultiPrecedenceConstraint(String activitiesString) {
		StringTokenizer tokenizer = new StringTokenizer(activitiesString, ",");
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			DeclarativeActivity activity = nodes.get(token);
			activities.add(activity);
		}

		MultiPrecedenceConstraint constraint = new MultiPrecedenceConstraint(activities.subList(0, activities.size() - 2),
				activities.get(activities.size() - 1));
		processSchema.addConstraint(constraint);
	}

	public void createMultiResponseConstraint(String startActivities, String endActivities) {
		List<DeclarativeActivity> start = getActivities(startActivities);
		List<DeclarativeActivity> end = getActivities(endActivities);

		MultiResponseConstraint constraint = new MultiResponseConstraint(start.get(0), end);
		processSchema.addConstraint(constraint);
	}

	public void createMultiSuccessionConstraint(String startActivities, String endActivities) {
		List<DeclarativeActivity> start = getActivities(startActivities);
		List<DeclarativeActivity> end = getActivities(endActivities);

		MultiSuccessionConstraint constraint = new MultiSuccessionConstraint(start, end);
		processSchema.addConstraint(constraint);
	}

	public void createMutualExclusionConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		MutualExclusionConstraint constraint = new MutualExclusionConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createNegationResponseConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		NegationResponseConstraint constraint = new NegationResponseConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createPrerequisiteConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		PrecedenceConstraint constraint = new PrecedenceConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createProcessSchema() {
		processSchema = new DeclarativeProcessSchema();
	}

	public void createRespondedExistenceConstraint(String activityName1, String activityName2) {
		DeclarativeActivity activity1 = nodes.get(activityName1);
		DeclarativeActivity activity2 = nodes.get(activityName2);
		RespondedExistenceConstraint constraint = new RespondedExistenceConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createResponseConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		ResponseConstraint constraint = new ResponseConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public void createSuccessionConstraint(String activity1Name, String activity2Name) {
		DeclarativeActivity activity1 = nodes.get(activity1Name);
		DeclarativeActivity activity2 = nodes.get(activity2Name);
		SuccessionConstraint constraint = new SuccessionConstraint(activity1, activity2);
		processSchema.addConstraint(constraint);
	}

	public List<IReminderInstance> getActiveReminders() {
		return processInstance.getActiveReminders();
	}

	protected List<DeclarativeActivity> getActivities(String toParse) {
		StringTokenizer tokenizer = new StringTokenizer(toParse, ",");
		List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			DeclarativeActivity activity = nodes.get(token);
			activities.add(activity);
		}

		return activities;
	}

	public List<DeclarativeActivity> getExecutableTasks() {
		return processInstance.getActiveActivities();
	}

	public void instantiateSchema() {
		processInstance = processSchema.instantiate();
	}

	public void launch(String activityName) {
		DeclarativeActivity node = nodes.get(activityName);
		INodeInstance nodeInstance = node.instantiate(processInstance);
		nodeInstance.requestActivation();
		nodeInstance.launch();
		CustomDateTimeSource dateTimeSource = (CustomDateTimeSource) DateTimeProvider.getDateTimeSource();
		dateTimeSource.increaseTime(new Duration(0, 30));
	}

	public void setTime(Duration duration) {
		DateTime startTime = new DateTime(processInstance.getStartTime());
		startTime.plus(duration);
		CustomDateTimeSource dateTimeSource = (CustomDateTimeSource) DateTimeProvider.getDateTimeSource();

		dateTimeSource.setTime(startTime);
	}
}
