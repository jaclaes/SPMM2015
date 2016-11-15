package org.cheetahplatform.modeler.engine.tdm;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_WORKSPACE;

import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.engine.IdValidator;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.test.TDMTestWorkspace;
import org.eclipse.core.runtime.Assert;

public class TDMTestInitializer {

	private TDMTest test;
	private TDMProcess process;
	private Workspace workspace;
	private IdValidator idValidator;

	public TDMTestInitializer(TDMProcess process, IdValidator idValidator, String testName) {
		this(process, idValidator, testName, idValidator.getLowestId() - 1);
	}

	public TDMTestInitializer(TDMProcess process, IdValidator idValidator, String testName, long id) {
		this.process = process;
		this.idValidator = idValidator;
		this.test = new TDMTest(process, testName, id);
		this.idValidator.validate(id);

		DeclarativeProcessInstance instance = process.getProcess().instantiate();
		instance.setStartTime(new DateTime(new Date(0), true));
		workspace = new Workspace(instance);
		Services.getCheetahObjectLookup().registerObject(NAMESPACE_WORKSPACE, new TDMTestWorkspace(test, workspace));
		process.add(test);
	}

	public void addActivityInstance(char activityName, int hour) {
		addActivityInstance(String.valueOf(activityName), hour);
	}

	public void addActivityInstance(String activityName, int hour) {
		addActivityInstance(activityName, hour, idValidator.getLowestId() - 1);
	}

	public void addActivityInstance(String activityName, int hour, int duration, long id) {
		idValidator.validate(id);
		DeclarativeActivity activity = getActivity(activityName);
		Activity added = workspace.addActivityInstance(activity, new DateTime(hour, 0, true));
		added.setCheetahId(id);
		added.setTimeSlot(new TimeSlot(new DateTime(hour, 0, true), new DateTime(hour + duration, 0, false)));
	}

	public void addActivityInstance(String activityName, int hour, long id) {
		addActivityInstance(activityName, hour, 1, id);
	}

	public void addExecutionAssertion(char activity, int hour, boolean executable) {
		addExecutionAssertion(String.valueOf(activity), hour, executable);
	}

	public void addExecutionAssertion(char activity, int startHour, int startMinute, int endHour, int endMinute, boolean executable) {
		addExecutionAssertion(String.valueOf(activity), startHour, startMinute, endHour, endMinute, executable);
	}

	public void addExecutionAssertion(String activityName, int hour, boolean executable) {
		addExecutionAssertion(activityName, hour, executable, idValidator.getLowestId() - 1);
	}

	public void addExecutionAssertion(String activityName, int hour, boolean executable, long id) {
		addExecutionAssertion(activityName, hour, 0, hour + 1, 0, executable, id);
	}

	public void addExecutionAssertion(String activityName, int startHour, int startMinute, int endHour, int endMinute, boolean executable) {
		addExecutionAssertion(activityName, startHour, startMinute, endHour, endMinute, executable, idValidator.getLowestId() - 1);
	}

	public void addExecutionAssertion(String activityName, int startHour, int startMinute, int endHour, int endMinute, boolean executable,
			long id) {
		idValidator.validate(id);
		DeclarativeActivity activity = getActivity(activityName);

		DateTime start = new DateTime(startHour, startMinute, true);
		Day day = workspace.getDay(start);
		DateTime end = new DateTime(start, false);
		int startInMinutes = startHour * 60 + startMinute;
		int endInMinutes = endHour * 60 + endMinute;
		end.plus(new Duration(new DateTime(0, endInMinutes - startInMinutes, false)));
		TimeSlot slot = new TimeSlot(start, end);

		ExecutionAssertion assertion = new ExecutionAssertion(day, slot, activity);
		assertion.setExecutable(executable);
		workspace.addAssertion(assertion);
		assertion.setCheetahId(id);
	}

	public void addTerminationAssertion(int startHour, int endHour, boolean canTerminate) {
		addTerminationAssertion(startHour, endHour, canTerminate, idValidator.getLowestId() - 1);
	}

	public void addTerminationAssertion(int startHour, int endHour, boolean canTerminate, long id) {
		addTerminationAssertion(startHour, 0, endHour, 0, canTerminate, id);
	}

	public void addTerminationAssertion(int startHour, int startMinute, int endHour, int endMinute, boolean canTerminate) {
		addTerminationAssertion(startHour, startMinute, endHour, endMinute, canTerminate, idValidator.getLowestId() - 1);
	}

	public void addTerminationAssertion(int startHour, int startMinute, int endHour, int endMinute, boolean canTerminate, long id) {
		idValidator.validate(id);

		DateTime start = new DateTime(startHour, startMinute, true);
		Day day = workspace.getDay(start);
		DateTime end = new DateTime(start, false);
		end.plus(new Duration(new DateTime(endHour - startHour, endMinute, false)));
		TimeSlot slot = new TimeSlot(start, end);

		TerminationAssertion assertion = new TerminationAssertion(day, slot);
		assertion.setCanTerminate(canTerminate);
		workspace.addAssertion(assertion);
		assertion.setCheetahId(id);
	}

	public DeclarativeActivity getActivity(String activityName) {
		DeclarativeProcessSchema schema = process.getProcess();
		List<DeclarativeActivity> activities = schema.getActivities(activityName);
		Assert.isTrue(activities.size() == 1);
		DeclarativeActivity activity = activities.get(0);

		return activity;
	}

	public TDMTest getTest() {
		return test;
	}
}
