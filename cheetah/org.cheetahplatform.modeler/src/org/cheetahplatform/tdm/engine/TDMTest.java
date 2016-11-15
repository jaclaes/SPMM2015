/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.engine;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.common.date.CustomDateTimeSource;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.IDateTimeSource;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.shared.ListenerList;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;

public class TDMTest extends IdentifiableObject implements INamed {
	private static final long serialVersionUID = 691166435914448129L;

	private TDMProcess process;
	private String name;

	private ListenerList testListeners;
	private PropertyChangeSupport propertyChangeSupport;
	private List<ITDMStep> failures;

	private List<TDMExecutionAssertion> executionAssertions;
	private List<TDMTerminationAssertion> terminationAssertions;
	private TDMProcessInstance instance;

	public TDMTest(TDMProcess process, String name) {
		this(process, name, NO_ID);
	}

	public TDMTest(TDMProcess process, String name, long id) {
		super(id);

		this.process = process;
		this.name = name;
		this.testListeners = new ListenerList();
		this.instance = new TDMProcessInstance();
		this.failures = new ArrayList<ITDMStep>();
		this.executionAssertions = new ArrayList<TDMExecutionAssertion>();
		this.terminationAssertions = new ArrayList<TDMTerminationAssertion>();
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void addActivity(Activity activity) {
		TDMActivity tdmActivity = new TDMActivity(activity);
		instance.addActivity(tdmActivity);
	}

	public void addExecutionAssertion(ExecutionAssertion assertion) {
		executionAssertions.add(new TDMExecutionAssertion(assertion));
	}

	public void addPropertyListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addTerminationAssertion(TerminationAssertion assertion) {
		terminationAssertions.add(new TDMTerminationAssertion(assertion));
	}

	public void addTestListener(ITestListener listener) {
		testListeners.add(listener);
	}

	private List<ITDMStep> computeTestSteps() {
		List<ITDMStep> steps = new ArrayList<ITDMStep>();
		Set<DateTime> assertionRelevantTimePoints = new HashSet<DateTime>();

		for (TDMActivity activity : instance.getActivities()) {
			LaunchActivityStep launchActivity = new LaunchActivityStep(activity);
			steps.add(launchActivity);
			assertionRelevantTimePoints.add(launchActivity.getStartTime());

			CompleteActivityStep completeActivity = new CompleteActivityStep(activity);
			steps.add(completeActivity);
			assertionRelevantTimePoints.add(completeActivity.getStartTime());
		}

		for (TDMExecutionAssertion assertion : executionAssertions) {
			Set<DateTime> timePoints = new HashSet<DateTime>(assertionRelevantTimePoints);

			steps.add(new ExecutionAssertionStep(assertion, assertion.getStartTime()));
			steps.add(new ExecutionAssertionStep(assertion, assertion.getEndTime()));
			timePoints.remove(assertion.getStartTime());
			timePoints.remove(assertion.getEndTime());

			for (DateTime dateTime : timePoints) {
				if (!assertion.toBeCheckedAt(dateTime)) {
					continue;
				}

				steps.add(new ExecutionAssertionStep(assertion, dateTime));
			}
		}

		for (TDMTerminationAssertion assertion : terminationAssertions) {
			Set<DateTime> timePoints = new HashSet<DateTime>(assertionRelevantTimePoints);

			steps.add(new TerminationAssertionStep(assertion, assertion.getStartTime()));
			steps.add(new TerminationAssertionStep(assertion, assertion.getEndTime()));
			timePoints.remove(assertion.getStartTime());
			timePoints.remove(assertion.getEndTime());

			for (DateTime dateTime : timePoints) {
				if (!assertion.toBeCheckedAt(dateTime)) {
					continue;
				}

				steps.add(new TerminationAssertionStep(assertion, dateTime));
			}
		}

		Collections.sort(steps, new Comparator<ITDMStep>() {

			@Override
			public int compare(ITDMStep step1, ITDMStep step2) {
				return step1.getStartTime().compareTo(step2.getStartTime());
			}

		});

		return steps;
	}

	public ITDMStep getFailure(Activity activity) {
		for (ITDMStep step : failures) {
			if (step.getSource().equals(activity)) {
				return step;
			}
		}

		return null;
	}

	/**
	 * @return the failures
	 */
	public List<ITDMStep> getFailures() {
		return failures;
	}

	@Override
	public String getName() {
		return name;
	}

	public TDMProcess getProcess() {
		return process;
	}

	private void informListenersTestFailed(List<ITDMStep> failures) {
		for (Object listener : testListeners.getListeners()) {
			((ITestListener) listener).testFailed(this, failures);
		}
	}

	private void informListenersTestPassed() {
		for (Object listener : testListeners.getListeners()) {
			((ITestListener) listener).testPassed(this);
		}
	}

	/**
	 * Return the passes.
	 * 
	 * @return the passes
	 */
	public boolean passes() {
		return failures.isEmpty();
	}

	public void removePropertyListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removeTestListener(Object testListener) {
		testListeners.remove(testListener);
	}

	public void reset() {
		instance.reset();
		executionAssertions.clear();
		terminationAssertions.clear();
	}

	public void run() {
		List<ITDMStep> steps = computeTestSteps();
		failures = new ArrayList<ITDMStep>();

		IDateTimeSource originalProvider = DateTimeProvider.getDateTimeSource();
		CustomDateTimeSource testTime = new CustomDateTimeSource();
		DateTimeProvider.setDateTimeSource(testTime);

		DeclarativeProcessInstance instance = process.getProcess().instantiate(name);
		for (ITDMStep step : steps) {
			testTime.setTime(step.getStartTime());

			try {
				step.execute(instance);
			} catch (TDMTestFailedException e) {
				failures.add(e.getFailureCause());
			}
		}

		DateTimeProvider.setDateTimeSource(originalProvider);

		if (passes()) {
			informListenersTestPassed();
		} else {
			informListenersTestFailed(failures);
		}
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;

		propertyChangeSupport.firePropertyChange(TDMCommand.ATTRIBUTE_TEST_NAME, oldName, name);
	}

}
