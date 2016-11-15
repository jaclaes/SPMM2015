/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_ACTIVITIES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.shared.ListenerList;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.test.model.Weekly;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyConstraint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.preference.IPreferenceStore;

public class Workspace extends GenericModel {
	private static final String SHOW_ROLE = "SHOW_ROLE_";

	public static final int DEFAULT_DAYS = 1;

	public static void initializeRoleFilterPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		List<Role> roles = RoleLookup.getInstance().getAllRoles();

		for (Role role : roles) {
			String key = SHOW_ROLE + role.getName();
			if (!store.contains(key)) {
				store.setValue(key, String.valueOf(true));
			}
		}
	}

	private final DeclarativeProcessInstance processInstance;
	private ListenerList logListeners;

	private final List<Day> days;
	private final List<Activity> activities;
	private final Weekly weekly;
	private TimeSlot planningAreaSelection;
	private TimeSlot executionAreaSelection;
	private TimeSlot terminationAreaSelection;
	private List<ExecutionAssertion> executionAssertions;
	private List<TerminationAssertion> terminationAssertions;
	private boolean showCompletedActivities;
	private TDMTest test;

	public Workspace() {
		this(new DeclarativeProcessSchema(), DEFAULT_DAYS);
	}

	public Workspace(DeclarativeProcessInstance instance) {
		this(instance, DEFAULT_DAYS);
	}

	public Workspace(DeclarativeProcessInstance instance, int defaultDays) {
		super(null);

		this.processInstance = instance;
		this.logListeners = new ListenerList();
		this.days = new ArrayList<Day>();
		this.activities = new ArrayList<Activity>();
		this.executionAssertions = new ArrayList<ExecutionAssertion>();
		this.terminationAssertions = new ArrayList<TerminationAssertion>();

		for (int i = 0; i < defaultDays; i++) {
			Date date = new Date(processInstance.getStartTime());
			date.increaseDaysBy(i);
			days.add(new Day(this, date));
		}

		this.weekly = new Weekly(this);
		this.weekly.initialize();
		this.showCompletedActivities = true;
		if (Platform.isRunning()) {
			initializePreferences();
		}
	}

	public Workspace(DeclarativeProcessSchema schema) {
		this(schema, DEFAULT_DAYS);
	}

	public Workspace(DeclarativeProcessSchema schema, int defaultDays) {
		this(schema.instantiate(), defaultDays);
	}

	public Workspace(int defaultDays) {
		this(new DeclarativeProcessSchema(), defaultDays);
	}

	public void addActivityInstance(Activity activity) {
		activities.add(activity);
		DeclarativeActivityInstance instance = activity.getActivity();
		weekly.addActivityInstance(instance);

		firePropertyChanged(TDMConstants.PROPERTY_ACTIVITIES);
		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_ADD_ACTIVITIY_INSTANCE);
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_ID, instance.getNode().getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_NAME, instance.getNode().getName());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TIME_SLOT, instance.getSlot().toStringRepresentation());
		entry.setAttribute(AbstractGraphCommand.ID, activity.getCheetahId());
		log(entry);
	}

	public Activity addActivityInstance(DeclarativeActivity activity, DateTime start) {
		Services.getCheetahObjectLookup().registerObject(NAMESPACE_DECLARATIVE_ACTIVITIES, activity);

		DeclarativeActivityInstance instance = activity.instantiate(processInstance);
		instance.setStartTime(start);
		DateTime end = new DateTime(start, false);
		end.plus(activity.getExpectedDuration());
		instance.setEndTime(end);

		return addActivityInstance(instance);
	}

	public Activity addActivityInstance(DeclarativeActivityInstance instance) {
		DateTime start = instance.getStartTime();
		Day day = getDay(start);
		if (day == null) {
			day = new Day(this, start);
			days.add(day);
		}

		Activity tddInstance = new Activity(day, instance);
		addActivityInstance(tddInstance);
		return tddInstance;
	}

	/**
	 * Create a new declarative activity and add an instance for the given timeslot.
	 * 
	 * @param day
	 *            the day on which the activity is added
	 * @param name
	 *            the name of the activity to be created
	 * @param slot
	 *            the slot
	 * @return the created activity
	 */
	public Activity addActivityInstance(String name, TimeSlot slot) {
		DeclarativeActivity activity = (processInstance.getSchema()).createActivity(name);
		Activity instance = addActivityInstance(activity, slot.getStart());
		instance.getTimeSlot().setEnd(slot.getEnd());
		return instance;
	}

	public void addAssertion(ExecutionAssertion assertion) {
		Assert.isNotNull(assertion);
		executionAssertions.add(assertion);
		firePropertyChanged(TDMConstants.PROPERTY_EXECUTION_ASSERTIONS);

		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_ADD_EXECUTION_ASSERTION);
		entry.setAttribute(AbstractGraphCommand.ID, assertion.getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_ID, assertion.getActivityDefinition().getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TIME_SLOT, assertion.getTimeSlot().toStringRepresentation());
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_NAME, assertion.getActivityDefinition().getName());
		log(entry);
	}

	public void addAssertion(TerminationAssertion assertion) {
		Assert.isNotNull(assertion);
		terminationAssertions.add(assertion);
		firePropertyChanged(TDMConstants.PROPERTY_TERMINATION_ASSERTIONS);

		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_ADD_TERMINATION_ASSERTION);
		entry.setAttribute(TDMCommand.ATTRIBUTE_TIME_SLOT, assertion.getTimeSlot().toStringRepresentation());
		entry.setAttribute(AbstractGraphCommand.ID, assertion.getCheetahId());
		log(entry);
	}

	public Day addDay(Date date) {
		Day day = new Day(this, date);
		days.add(day);
		return day;
	}

	public void addLogListener(ILogListener listener) {
		logListeners.add(listener);
	}

	public void appendDay() {
		Date date = null;
		if (days.isEmpty()) {
			date = new Date();
		} else {
			date = new Date(days.get(days.size() - 1).getDate());
			date.increaseDaysBy(1);
		}

		days.add(new Day(this, date));
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WorkspaceEditPart(this);
	}

	private void fireTimeSlotSelectionChange() {
		for (Day day : days) {
			day.getPlanningArea().firePropertyChanged(TDMConstants.PROPERTY_TIMESLOT_SELECTION);
			day.getExecutionAssertionArea().firePropertyChanged(TDMConstants.PROPERTY_TIMESLOT_SELECTION);
			day.getTerminationAssertionArea().firePropertyChanged(TDMConstants.PROPERTY_TIMESLOT_SELECTION);
		}

		firePropertyChanged(TDMConstants.PROPERTY_TIMESLOT_SELECTION);
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public Activity getActivity(long activityId) {
		for (Activity activity : activities) {
			if (activity.getCheetahId() == activityId) {
				return activity;
			}
		}

		return null;
	}

	/**
	 * Determine the given day's activities.
	 * 
	 * @param tddDay
	 *            the day
	 * @return the activities
	 */
	public List<Activity> getActivityInstances(Day tddDay) {
		return getOccurringAt(activities, tddDay);
	}

	private Assertion getAssertion(List<? extends Assertion> assertions, long id) {
		for (Assertion assertion : assertions) {
			if (assertion.getCheetahId() == id) {
				return assertion;
			}
		}

		return null;
	}

	public Assertion getAssertion(long id) {
		ExecutionAssertion assertion = getExecutionAssertion(id);
		if (assertion != null) {
			return assertion;
		}

		return getTerminationAssertion(id);
	}

	@Override
	public List<? extends Object> getChildren() {
		return days;
	}

	/**
	 * Determine the day for the given date.
	 * 
	 * @param date
	 *            the date
	 * @return the corresponding day, <code>null</code> if there is no day for the given date
	 */
	public Day getDay(Date date) {
		for (Day day : days) {
			if (day.getDate().sameDay(date)) {
				return day;
			}
		}

		return null;
	}

	public List<Day> getDays() {
		return days;
	}

	/**
	 * @return the executionAreaSelection
	 */
	public TimeSlot getExecutionAreaSelection() {
		return executionAreaSelection;
	}

	public ExecutionAssertion getExecutionAssertion(long id) {
		return (ExecutionAssertion) getAssertion(executionAssertions, id);
	}

	/**
	 * @return the executionAssertions
	 */
	public List<ExecutionAssertion> getExecutionAssertions() {
		return executionAssertions;
	}

	public List<Activity> getExecutionAssertions(Day day) {
		return getOccurringAt(executionAssertions, day);
	}

	/**
	 * Determine all incoming constraints for this activity, i.e. where the arrow in the visualization for the constraints ends at the given
	 * activity.
	 * 
	 * @param activity
	 *            the activity
	 * @return the constraints
	 */
	public List<WeeklyConstraint> getIncomingConstraints(WeeklyActivity activity) {
		return weekly.getIncomingConstraints(activity);
	}

	private List<Activity> getOccurringAt(List<? extends Activity> activities, Day day) {
		List<Activity> instances = new ArrayList<Activity>();
		for (Activity instance : activities) {
			if (instance.occursAt(day)) {
				instances.add(instance);
			}
		}

		return instances;
	}

	/**
	 * Determine all outgoing constraints for this activity, i.e. where the arrow in the visualization for the constraints starts at the
	 * given activity.
	 * 
	 * @param activity
	 *            the activity
	 * @return the constraints
	 */
	public List<WeeklyConstraint> getOutgoingConstraints(WeeklyActivity activity) {
		return weekly.getOutgoingConstraints(activity);
	}

	/**
	 * @return the selection
	 */
	public TimeSlot getPlanningAreaSelection() {
		return planningAreaSelection;
	}

	/**
	 * Return the processInstance.
	 * 
	 * @return the processInstance
	 */
	public DeclarativeProcessInstance getProcessInstance() {
		return processInstance;
	}

	/**
	 * Determine the roles defined in this workspace.
	 * 
	 * @return all roles
	 */
	public List<Role> getRoles() {
		Set<Role> roles = new HashSet<Role>();
		for (INodeInstance node : processInstance.getNodeInstances()) {
			Role role = RoleLookup.getInstance().getRole((DeclarativeActivityInstance) node);
			if (role != null) {
				roles.add(role);
			}
		}

		return new ArrayList<Role>(roles);
	}

	/**
	 * @return the terminationAreaSelection
	 */
	public TimeSlot getTerminationAreaSelection() {
		return terminationAreaSelection;
	}

	public TerminationAssertion getTerminationAssertion(long id) {
		return (TerminationAssertion) getAssertion(terminationAssertions, id);
	}

	public List<TerminationAssertion> getTerminationAssertions() {
		return terminationAssertions;
	}

	public List<Activity> getTerminationAssertions(Day day) {
		return getOccurringAt(terminationAssertions, day);
	}

	public TDMTest getTest() {
		return test;
	}

	/**
	 * Return the weekly.
	 * 
	 * @return the weekly
	 */
	public Weekly getWeekly() {
		return weekly;
	}

	private void initializePreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		this.showCompletedActivities = store.getBoolean(TDMConstants.KEY_SHOW_COMPLETED_ACTIVITIES);

		initializeRoleFilterPreferences();
	}

	public boolean isFirstDay(Day targetDay) {
		return days.indexOf(targetDay) == 0;
	}

	public boolean isLastDay(Day targetDay) {
		return days.indexOf(targetDay) == days.size() - 1;
	}

	public boolean isShowCompletedActivities() {
		return showCompletedActivities;
	}

	public boolean isShowRole(Role role) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String key = SHOW_ROLE + role.getName();
		return Boolean.valueOf(store.getString(key));
	}

	public void log(AuditTrailEntry entry) {
		for (Object listener : logListeners.getListeners()) {
			((ILogListener) listener).log(entry);
		}
	}

	public void logInProcessInstance(AuditTrailEntry entry) {
		processInstance.log(entry);
	}

	public void removeActivity(Activity activity) {
		Assert.isTrue(activities.remove(activity));

		firePropertyChanged(TDMConstants.PROPERTY_ACTIVITIES);
		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_REMOVE_ACTIVITY_INSTANCE);
		entry.setAttribute(AbstractGraphCommand.ID, activity.getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_ACTIVITY_NAME, activity.getName());
		log(entry);
	}

	public void removeAssertion(Assertion assertion) {
		if (assertion instanceof ExecutionAssertion) {
			removeAssertion((ExecutionAssertion) assertion);
		} else if (assertion instanceof TerminationAssertion) {
			removeAssertion((TerminationAssertion) assertion);
		} else {
			throw new IllegalStateException();
		}
	}

	public void removeAssertion(ExecutionAssertion executionAssertion) {
		executionAssertions.remove(executionAssertion);
		firePropertyChanged(TDMConstants.PROPERTY_EXECUTION_ASSERTIONS);

		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_REMOVE_EXECUTION_ASSERTION);
		entry.setAttribute(AbstractGraphCommand.ID, executionAssertion.getCheetahId());
		log(entry);
	}

	public void removeAssertion(TerminationAssertion terminationAssertion) {
		terminationAssertions.remove(terminationAssertion);
		firePropertyChanged(TDMConstants.PROPERTY_TERMINATION_ASSERTIONS);

		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_REMOVE_TERMINATION_ASSERTION);
		entry.setAttribute(AbstractGraphCommand.ID, terminationAssertion.getCheetahId());
		log(entry);
	}

	public void removeLoggListener(ILogListener listener) {
		logListeners.remove(listener);
	}

	public void selectExclusively(Activity toSelect) {
		List<Activity> selectables = new ArrayList<Activity>();
		selectables.addAll(activities);
		selectables.addAll(executionAssertions);
		selectables.addAll(terminationAssertions);

		for (Activity selectable : selectables) {
			if (selectable.equals(toSelect)) {
				selectable.select();
			} else {
				selectable.unselect();
			}
		}
	}

	/**
	 * @param executionAreaSelection
	 *            the executionAreaSelection to set
	 */
	public void setExecutionAreaSelection(TimeSlot executionAreaSelection) {
		this.executionAreaSelection = executionAreaSelection;
		this.planningAreaSelection = null;
		this.terminationAreaSelection = null;

		fireTimeSlotSelectionChange();
	}

	/**
	 * @param executionAssertions
	 *            the executionAssertions to set
	 */
	public void setExecutionAssertions(List<ExecutionAssertion> executionAssertions) {
		this.executionAssertions = executionAssertions;
	}

	/**
	 * @param selection
	 *            the selection to set
	 */
	public void setPlanningAreaSelection(TimeSlot selection) {
		this.planningAreaSelection = selection;
		this.executionAreaSelection = null;
		this.terminationAreaSelection = null;

		fireTimeSlotSelectionChange();
	}

	public void setShowCompletedActivities(boolean state) {
		showCompletedActivities = state;
		firePropertyChanged(TDMConstants.PROPERTY_ACTIVITIES);
	}

	public void setShowRole(Role role, boolean show) {
		String key = SHOW_ROLE + role.getName();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(key, String.valueOf(show));

		firePropertyChanged(TDMConstants.PROPERTY_ACTIVITIES);
	}

	/**
	 * @param terminationAreaSelection
	 *            the terminationAreaSelection to set
	 */
	public void setTerminationAreaSelection(TimeSlot terminationAreaSelection) {
		this.terminationAreaSelection = terminationAreaSelection;
		this.planningAreaSelection = null;
		this.executionAreaSelection = null;

		fireTimeSlotSelectionChange();
	}

	public void setTest(TDMTest test) {
		this.test = test;
	}

}
