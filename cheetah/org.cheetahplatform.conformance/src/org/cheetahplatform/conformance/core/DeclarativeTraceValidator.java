package org.cheetahplatform.conformance.core;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.cheetahplatform.common.date.CustomDateTimeSource;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.IDateTimeSource;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.declarative.constraint.ConstraintEvaluation;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.xml.sax.SAXException;

public class DeclarativeTraceValidator implements ITraceValidator {

	private DeclarativeProcessSchema schema;

	public DeclarativeTraceValidator(String modelName, InputStream model) throws SAXException, IOException, ParserConfigurationException {
		schema = new DeclarativeModelParser().parse(modelName, model);
	}

	@Override
	public TerminatingConformanceTrace generateTerminatingTrace(int minimumTraceLength) {
		List<ConformanceActivity> trace = new ArrayList<ConformanceActivity>();
		DeclarativeProcessInstance instance = schema.instantiate();
		Random random = new Random();

		IDateTimeSource originalSource = DateTimeProvider.getDateTimeSource();
		CustomDateTimeSource dateSource = new CustomDateTimeSource();
		DateTimeProvider.setDateTimeSource(dateSource);

		try {
			while (true) {
				if ((trace.size() >= minimumTraceLength && instance.canTerminate().getResult())
						|| trace.size() > MAXIMUM_TERMINATION_TRACE_LENGTH) {
					break;
				}

				List<DeclarativeActivity> activities = instance.getActiveActivities();
				if (activities.isEmpty()) {
					break;
				}

				DeclarativeActivity randomActivity = activities.get(random.nextInt(activities.size()));
				trace.add(new ConformanceActivity(randomActivity.getName()));
				INodeInstance activityInstance = randomActivity.instantiate(instance);
				activityInstance.requestActivation();
				activityInstance.launch();
				activityInstance.complete();

				dateSource.increaseTime(new Duration(0, 1));
			}
		} finally {
			DateTimeProvider.setDateTimeSource(originalSource);
		}

		return new TerminatingConformanceTrace(trace, instance.canTerminate().getResult());
	}

	@Override
	public List<ConformanceActivity> generateTrace(int traceLength) {
		List<ConformanceActivity> trace = new ArrayList<ConformanceActivity>();
		DeclarativeProcessInstance instance = schema.instantiate();
		Random random = new Random();

		IDateTimeSource originalSource = DateTimeProvider.getDateTimeSource();
		CustomDateTimeSource dateSource = new CustomDateTimeSource();
		DateTimeProvider.setDateTimeSource(dateSource);

		try {
			for (int i = 0; i < traceLength; i++) {
				List<DeclarativeActivity> activities = instance.getActiveActivities();
				if (activities.isEmpty()) {
					break;
				}

				DeclarativeActivity randomActivity = activities.get(random.nextInt(activities.size()));
				trace.add(new ConformanceActivity(randomActivity.getName()));
				INodeInstance activityInstance = randomActivity.instantiate(instance);
				activityInstance.requestActivation();
				activityInstance.launch();
				activityInstance.complete();

				dateSource.increaseTime(new Duration(0, 1));
			}
		} finally {
			DateTimeProvider.setDateTimeSource(originalSource);
		}

		return trace;
	}

	private DeclarativeActivity getActivity(List<ConformanceActivity> trace, ConformanceActivity toExecute) throws NotConformantException {
		for (INode node : schema.getNodes()) {
			if (node.getName().equals(toExecute.getName())) {
				return (DeclarativeActivity) node;
			}
		}

		String message = MessageFormat.format("Could not find activity ''{0}'' in process model ''{1}''.", toExecute.getName(), schema
				.getName());
		throw new NotConformantException(trace, message);
	}

	@Override
	public String getModelName() {
		return schema.getName();
	}

	private DeclarativeProcessInstance internalReplay(List<ConformanceActivity> trace) throws NotConformantException {
		DeclarativeProcessInstance instance = schema.instantiate();
		IDateTimeSource originalSource = DateTimeProvider.getDateTimeSource();
		CustomDateTimeSource dateSource = new CustomDateTimeSource();
		DateTimeProvider.setDateTimeSource(dateSource);

		try {
			for (int i = 0; i < trace.size(); i++) {
				ConformanceActivity toExecute = trace.get(i);
				DeclarativeActivity activity = getActivity(trace, toExecute);
				ConstraintEvaluation result = instance.isExecutable(activity);
				if (!result.getResult()) {
					String message = MessageFormat.format(
							"Tried to execute activity ''{0}'' (index {1}), but was not enabled due to the following constraint:\n{2}",
							toExecute.getName(), i + 1, result.getConstraint().getDescription());
					throw new NotConformantException(trace, message);
				}

				INodeInstance activityInstance = activity.instantiate(instance);
				activityInstance.requestActivation();
				activityInstance.launch();
				activityInstance.complete();

				dateSource.increaseTime(new Duration(0, 1));
			}
		} finally {
			DateTimeProvider.setDateTimeSource(originalSource);
		}

		return instance;
	}

	@Override
	public void replayTrace(List<ConformanceActivity> trace) throws NotConformantException {
		internalReplay(trace);
	}

	@Override
	public void terminatesFor(TerminatingConformanceTrace trace) throws NotConformantException {
		DeclarativeProcessInstance instance = internalReplay(trace.getActivities());
		ConstraintEvaluation result = instance.canTerminate();

		if (result.getResult() ^ trace.canTerminate()) {
			IDeclarativeConstraint conflictingConstraint = result.getConstraint();
			String additionalInfo = "None.";
			if (conflictingConstraint != null) {
				additionalInfo = conflictingConstraint.getDescription();
			}

			String message = MessageFormat.format(
					"Termination behavior differs: Expected {0}, but was {1}.\n\nAdditional information: {2}", trace.canTerminate(), result
							.getResult(), additionalInfo);
			throw new NotConformantException(trace.getActivities(), message);
		}

	}

}
