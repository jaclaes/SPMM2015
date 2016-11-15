/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import static org.cheetahplatform.tdm.TDMConstants.PROPERTY_START_WEEK;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.constraint.EarliestStartTimeConstraint;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.runtime.AbstractDeclarativeNodeInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.IDeclarativeNodeInstance;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.GenericTDMModel;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.weekly.RelativeBounds;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyEditPart;
import org.eclipse.gef.EditPart;

public class Weekly extends GenericTDMModel implements PropertyChangeListener {
	private Date startWeek;
	private final List<MultiWeek> multiWeeks;
	private final List<WeeklyActivity> weeklyActivities;
	private final List<WeeklyConstraint> constraints;
	private final List<WeeklyMilestone> milestones;

	public Weekly(Workspace workspace) {
		super(workspace);

		this.weeklyActivities = new ArrayList<WeeklyActivity>();
		this.multiWeeks = new ArrayList<MultiWeek>();
		this.constraints = new ArrayList<WeeklyConstraint>();
		this.milestones = new ArrayList<WeeklyMilestone>();
		this.startWeek = Date.weekStart(workspace.getProcessInstance().getStartTime());
		this.startWeek.plus(new Duration(1, 0, 0));
	}

	/**
	 * Moves the activity to another milestone, if necessary.
	 * 
	 * @param activity
	 */
	public void adaptMilestone(WeeklyActivity activity) {
		for (WeeklyMilestone milestone : milestones) {
			milestone.removeActivity(activity);
		}

		DateTime startTime = Date.weekStart(activity.getStartTime().copy());
		startTime.minus(new Duration(0, 0, 1, false));
		WeeklyMilestone milestone = findMilestone(startTime);
		if (milestone != null) {
			milestone.addActivity(activity);
		}
	}

	public void addActivityInstance(DeclarativeActivityInstance instance) {
		MultiWeek week = findWeek(instance.getStartTime());
		WeeklyActivity activity = new WeeklyActivity(week, instance);
		weeklyActivities.add(activity);
	}

	public void addConstraint(IDeclarativeConstraint constraint) {
		constraints.add(new WeeklyConstraint(this, constraint));
	}

	public MultiWeek addWeek(DateTime start) {
		MultiWeek week = new MultiWeek(parent, start);
		multiWeeks.add(week);

		return week;
	}

	private MultiWeek appendWeek() {
		DateTime latestStart = null;

		if (multiWeeks.isEmpty()) {
			Workspace workspace = getWorkspace();
			DateTime startTime = Date.weekStart(workspace.getProcessInstance().getStartTime());
			MultiWeek week = new MultiWeek(this, startTime);
			multiWeeks.add(week);

			return week;
		}

		latestStart = multiWeeks.get(multiWeeks.size() - 1).getSlot().getStart();
		DateTime newStart = new DateTime(latestStart, true);
		newStart.plus(new Duration(WeeklyEditPart.getWeeksPerRow() * 7, 0, 0));
		MultiWeek week = new MultiWeek(this, newStart);
		multiWeeks.add(week);
		return week;
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WeeklyEditPart(this);
	}

	/**
	 * Determine the milestone for the given date.
	 * 
	 * @param current
	 *            the date
	 * @return the next upcoming milestone
	 */
	public WeeklyMilestone findMilestone(DateTime current) {
		if (milestones.isEmpty()) {
			return null;
		}

		List<WeeklyMilestone> copy = new ArrayList<WeeklyMilestone>(milestones);
		Collections.sort(copy, new Comparator<WeeklyMilestone>() {
			@Override
			public int compare(WeeklyMilestone o1, WeeklyMilestone o2) {
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
		});

		TimeSlot slot = new TimeSlot(new DateTime(0, 0, 1, 0, 0, true), new DateTime(0, 0, 1, 0, 1, false));
		for (int i = 0; i < copy.size(); i++) {
			WeeklyMilestone milestone = copy.get(i);
			slot = new TimeSlot(new DateTime(slot.getEnd(), true), new DateTime(milestone.getStartTime(), false));
			if (slot.includes(current)) {
				return milestone;
			}
		}

		return null;
	}

	/**
	 * Tries to get an existing week for given start time, if there is no such, a new one is added.
	 * 
	 * @param startTime
	 *            the time
	 * @return the corresponding week.
	 */
	public MultiWeek findWeek(DateTime startTime) {
		MultiWeek newParent = null;
		prependWeeks(startTime);

		while (newParent == null) {
			newParent = getWeek(startTime);
			appendWeek();
		}
		return newParent;
	}

	public WeeklyActivity getActivity(DeclarativeActivity activity) {
		for (WeeklyActivity current : weeklyActivities) {
			if (current.getActivity().getNode().equals(activity)) {
				return current;
			}
		}

		return null;
	}

	public WeeklyActivity getActivityInstance(AbstractDeclarativeNodeInstance instance) {
		for (WeeklyActivity activity : weeklyActivities) {
			if (activity.getActivity().equals(instance)) {
				return activity;
			}
		}

		return null;
	}

	public List<WeeklyActivity> getActivityInstances() {
		return weeklyActivities;
	}

	public List<WeeklyActivity> getActivityInstances(MultiWeek week) {
		List<WeeklyActivity> activities = new ArrayList<WeeklyActivity>();
		for (WeeklyActivity activity : weeklyActivities) {
			if (week.getSlot().includes(activity.getStartTime())) {
				if (!activity.isVisible()) {
					continue;
				}
				activities.add(activity);
			}
		}

		sortActivities(activities);
		return activities;
	}

	@Override
	public List<? extends Object> getChildren() {
		List<MultiWeek> children = new ArrayList<MultiWeek>();
		int rows = WeeklyEditPart.getRows();
		boolean foundStartWeek = false;
		prependWeeks(startWeek);

		for (MultiWeek multiWeek : multiWeeks) {
			if (multiWeek.getSlot().includesDay(startWeek)) {
				foundStartWeek = true;
			}

			if (foundStartWeek && children.size() < rows) {
				children.add(multiWeek);

				if (children.size() == rows) {
					break;
				}
			}
		}

		while (children.size() < rows) {
			children.add(appendWeek());
		}

		return children;
	}

	public List<WeeklyConstraint> getIncomingConstraints(WeeklyActivity activity) {
		List<WeeklyConstraint> incoming = new ArrayList<WeeklyConstraint>();
		for (WeeklyConstraint constraint : constraints) {
			if (constraint.isIncoming(activity)) {
				incoming.add(constraint);
			}
		}

		return incoming;
	}

	public List<WeeklyMilestone> getMilestones(MultiWeek week) {
		List<WeeklyMilestone> matched = new ArrayList<WeeklyMilestone>();

		for (WeeklyMilestone milestone : this.milestones) {
			if (week.getSlot().includes(milestone.getStartTime())) {
				matched.add(milestone);
			}
		}

		Collections.sort(matched, new Comparator<WeeklyMilestone>() {
			@Override
			public int compare(WeeklyMilestone o1, WeeklyMilestone o2) {
				return Collator.getInstance().compare(o1.getMilestone().getNode().getName(), o2.getMilestone().getNode().getName());
			}
		});

		return matched;
	}

	public WeeklyActivity getOrAdd(DeclarativeActivityInstance instance) {
		for (WeeklyActivity activity : weeklyActivities) {
			if (activity.getActivity().equals(instance)) {
				return activity;
			}
		}

		getWorkspace().addActivityInstance(instance);
		return getActivityInstance(instance);
	}

	public List<WeeklyConstraint> getOutgoingConstraints(WeeklyActivity activity) {
		List<WeeklyConstraint> outgoing = new ArrayList<WeeklyConstraint>();
		for (WeeklyConstraint constraint : constraints) {
			if (constraint.isOutgoing(activity)) {
				outgoing.add(constraint);
			}
		}

		return outgoing;
	}

	/**
	 * Determine all possible source activities for the given constraint.
	 * 
	 * @param constraint
	 *            the constraint
	 * @return all possible sources
	 */
	public List<WeeklyActivity> getSourceActivities(WeeklyConstraint constraint) {
		DeclarativeActivity activity = constraint.getEndActivity();
		List<WeeklyActivity> possibleSources = new ArrayList<WeeklyActivity>();

		for (WeeklyActivity current : weeklyActivities) {
			if (activity.equals(current.getActivity().getNode())) {
				possibleSources.add(current);
			}
		}

		return possibleSources;
	}

	public List<WeeklyActivity> getTargetActivities(WeeklyConstraint constraint) {
		DeclarativeActivity activity = constraint.getStartActivity();
		List<WeeklyActivity> possibleTargets = new ArrayList<WeeklyActivity>();

		for (WeeklyActivity current : weeklyActivities) {
			if (activity.equals(current.getActivity().getNode())) {
				possibleTargets.add(current);
			}
		}

		return possibleTargets;
	}

	/**
	 * Returns the week for given time.
	 * 
	 * @param start
	 *            the time
	 * @return the associated week, <code>null</code> if there is no such
	 */
	public MultiWeek getWeek(DateTime start) {
		for (MultiWeek week : multiWeeks) {
			if (week.getSlot().includes(start)) {
				return week;
			}
		}

		return null;
	}

	public void initialize() {
		getWorkspace().addPropertyChangeListener(this);
		DeclarativeProcessInstance instance = getWorkspace().getProcessInstance();
		instance.setStartTime(Date.weekStart(instance.getStartTime()));

		initializeWeeks();
		initializeActivities();
		initializeConstraints();
		initializeMilestones();
	}

	private void initializeActivities() {
		DeclarativeProcessInstance instance = getWorkspace().getProcessInstance();
		DeclarativeProcessSchema schema = instance.getSchema();
		DateTime start = getWorkspace().getProcessInstance().getStartTime();

		instantiateActivitiesByConstraints(instance, schema, start);
		instantiateActivityByMilestone(instance);
	}

	private void initializeConstraints() {
		for (IDeclarativeConstraint constraint : (getWorkspace().getProcessInstance().getSchema()).getConstraints()) {
			addConstraint(constraint);
		}
	}

	private void initializeMilestones() {
		DeclarativeProcessInstance instance = getWorkspace().getProcessInstance();
		for (MilestoneInstance milestone : instance.getMilestones()) {
			MultiWeek week = findWeek(milestone.getStartTime());
			milestones.add(new WeeklyMilestone(week, milestone));
		}

		DeclarativeProcessSchema schema = instance.getSchema();
		for (Milestone milestone : schema.getMilestones()) {
			boolean mileStoneInstanceExists = false;
			for (WeeklyMilestone milestoneInstance : milestones) {
				if (milestoneInstance.getMilestone().getNode().equals(milestone)) {
					mileStoneInstanceExists = true;
					break;
				}
			}

			if (!mileStoneInstanceExists) {
				MilestoneInstance newInstance = (MilestoneInstance) milestone.instantiate(instance);
				newInstance.setStartTime(Date.weekEnd(newInstance.getStartTime()));
				IGenericModel week = findWeek(newInstance.getStartTime());
				milestones.add(new WeeklyMilestone(week, newInstance));
			}
		}
	}

	private void initializeWeeks() {
		int rows = WeeklyEditPart.getRows();
		int existingWeeks = multiWeeks.size();

		int requiredWeeks = existingWeeks / WeeklyEditPart.getWeeksPerRow();
		for (int i = 0; i < rows - requiredWeeks; i++) {
			appendWeek();
		}
	}

	private void instantiateActivitiesByConstraints(DeclarativeProcessInstance instance, DeclarativeProcessSchema schema, DateTime start) {
		for (IDeclarativeConstraint constraint : schema.getConstraints()) {
			if (constraint instanceof EarliestStartTimeConstraint) {
				EarliestStartTimeConstraint startTimeConstraint = (EarliestStartTimeConstraint) constraint;
				if (!instance.getNodeInstances(((EarliestStartTimeConstraint) constraint).getActivity()).isEmpty()) {
					continue; // do not instantiate activities twice by default
				}

				Duration duration = startTimeConstraint.getDurationSinceStart();
				DateTime earliestStart = new DateTime(start);
				earliestStart.plus(duration);

				MultiWeek week = findWeek(earliestStart);
				DeclarativeActivityInstance activityInstance = startTimeConstraint.getActivity().instantiate(instance);
				activityInstance.setStartTime(earliestStart);
				WeeklyActivity activity = new WeeklyActivity(week, activityInstance);
				weeklyActivities.add(activity);
			}
		}
	}

	private void instantiateActivityByMilestone(DeclarativeProcessInstance instance) {
		Date date = instance.getStartTime();
		List<MilestoneInstance> coreMilestones = new ArrayList<MilestoneInstance>(instance.getMilestones());
		Collections.sort(coreMilestones, new Comparator<MilestoneInstance>() {
			@Override
			public int compare(MilestoneInstance o1, MilestoneInstance o2) {
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
		});

		for (int i = 0; i < coreMilestones.size(); i++) {
			MilestoneInstance milestone = coreMilestones.get(i);

			for (DeclarativeActivity activity : ((Milestone) milestone.getNode()).getActivities()) {
				if (!instance.getNodeInstances(activity).isEmpty()) {
					continue; // do not instantiate activities twice by default
				}

				DeclarativeActivityInstance activityInstance = activity.instantiate(instance);
				milestone.addActivity(activityInstance);
				activityInstance.setStartTime(new DateTime(date));
				getOrAdd(activityInstance);
			}

			date = new DateTime(milestone.getStartTime());
			date.plus(new Duration(7, 0, 0));
		}
	}

	public boolean isMoveProhibitedByMilestone(WeeklyActivity activity, DateTime newStart) {
		IDeclarativeNodeInstance instance = activity.getActivity();
		// milestones are always places at the very beginning of the week, but we need the end
		// newStart = Date.weekStart(newStart.copy());
		// newStart.minus(new Duration(0, 0, 1, false));

		for (WeeklyMilestone milestoneUiModel : milestones) {
			MilestoneInstance milestone = milestoneUiModel.getMilestone();
			if (!milestone.containsActivity((DeclarativeActivityInstance) instance)) {
				continue;
			}

			if (!((Milestone) milestone.getNode()).allowsMoving((DeclarativeActivity) instance.getNode())) {
				WeeklyMilestone target = findMilestone(newStart);
				return !milestoneUiModel.equals(target);
			}

			break;
		}

		return false;
	}

	private void logMilestoneMove(WeeklyMilestone milestone, boolean forward) {
		String direction = TDMConstants.MILESTONE_MOVE_FORWARD;
		if (!forward) {
			direction = TDMConstants.MILESTONE_MOVE_BACKWARD;
		}
		String cheetahId = String.valueOf(milestone.getMilestone().getCheetahId());
		AuditTrailEntry entry = new AuditTrailEntry(TDMConstants.LOG_EVENT_MILESTONE_MOVE, cheetahId);
		entry.setAttribute(TDMConstants.ATTRIBUTE_MILESTONE_MOVE_DIRECTION, direction);
		getWorkspace().getProcessInstance().log(entry);
	}

	/**
	 * Move all activities which are dependent on the milestone.
	 * 
	 * @param milestoneUiModel
	 *            the milestone
	 * @param forward
	 *            <code>true</code> if the milestone is moved into the future, <code>false</code> if to the past
	 */
	private void moveDependentActivities(WeeklyMilestone milestoneUiModel, boolean forward) {
		MilestoneInstance milestoneInstance = milestoneUiModel.getMilestone();
		Duration duration = new Duration(7, 0, 0);

		for (DeclarativeActivityInstance activity : milestoneInstance.getActivities()) {
			WeeklyActivity uiModel = getActivityInstance(activity);

			boolean notLaunched = !activity.isInFinalState();

			if (notLaunched) {
				DateTime start = activity.getStartTime();
				if (forward) {
					start.plus(duration);
				} else {
					start.minus(duration);
				}

				uiModel.setEndTime(null);
				uiModel.setStartTime(start, false);

				RelativeBounds bounds = uiModel.getActivityBounds();
				double factor = 1.0 / WeeklyEditPart.getWeeksPerRow();
				if (!forward) {
					factor *= -1;
				}
				bounds.translateXModulo(factor);
			}
		}
	}

	/**
	 * Move a given milestone.
	 * 
	 * @param milestone
	 *            the milestone to be moved
	 * @param forward
	 *            if <code>true</code>, milestones are moved towards future, else into the past
	 */
	private void moveMilestone(WeeklyMilestone milestone, boolean forward) {
		Duration duration = new Duration(7, 0, 0, true);
		DateTime startTime = new DateTime(milestone.getStartTime());
		if (forward) {
			startTime.plus(duration);
		} else {
			startTime.minus(duration);
		}

		milestone.getMilestone().setStartTime(startTime);
		milestone.setParent(findWeek(startTime));
		for (WeeklyMilestone current : milestones) {
			if (current.getStartTime().compareTo(milestone.getStartTime()) >= 0) {
				moveDependentActivities(current, forward);
			}
		}

		logMilestoneMove(milestone, forward);
		firePropertyChanged(TDMConstants.PROPERTY_START_WEEK);
	}

	public void moveToNextWeek(WeeklyMilestone milestone) {
		moveMilestone(milestone, true);
	}

	public void moveToPreviousWeek(WeeklyMilestone milestone) {
		moveMilestone(milestone, false);
	}

	private void prependWeeks(Date desiredStart) {
		DateTime start = new DateTime(multiWeeks.get(0).getStartTime());
		int weeksPerRow = WeeklyEditPart.getWeeksPerRow();

		if (!start.sameWeek(desiredStart) && desiredStart.compareTo(start) < 0) {
			while (desiredStart.compareTo(start) < 0) {
				start.minus(new Duration(weeksPerRow * 7, 0, 0, true));
				DateTime newWeekStart = new DateTime(start);
				multiWeeks.add(0, new MultiWeek(this, newWeekStart));
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		firePropertyChanged(evt);
	}

	public void showNextWeek() {
		startWeek.plus(new Duration(WeeklyEditPart.getWeeksPerRow() * 7, 0, 0, true));
		firePropertyChanged(PROPERTY_START_WEEK);
	}

	public void showPreviousWeek() {
		startWeek.minus(new Duration(WeeklyEditPart.getWeeksPerRow() * 7, 0, 0, true));
		firePropertyChanged(PROPERTY_START_WEEK);
	}

	private void sortActivities(List<WeeklyActivity> activities) {
		Collections.sort(activities, new Comparator<WeeklyActivity>() {
			@Override
			public int compare(WeeklyActivity o1, WeeklyActivity o2) {
				if (o1.isCompleted()) {
					return -1;
				}
				if (o2.isCompleted()) {
					return 1;
				}
				if (o1.isLaunched()) {
					return -1;
				}
				if (o2.isLaunched()) {
					return 1;
				}

				String name1 = o1.getActivity().getNode().getName();
				String name2 = o2.getActivity().getNode().getName();
				return Collator.getInstance().compare(name1, name2);
			}
		});
	}
}
