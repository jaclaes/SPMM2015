package org.cheetahplatform.conformance.core;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.xml.sax.SAXException;

public class BPMNTraceValidator implements ITraceValidator {

	private ImperativeProcessSchema schema;

	public BPMNTraceValidator(String modelName, InputStream model) throws SAXException, IOException, ParserConfigurationException {
		schema = new BPMNModelParser().parseModel(modelName, model);
	}

	@Override
	public TerminatingConformanceTrace generateTerminatingTrace(int minimumTraceLength) {
		List<ConformanceActivity> trace = new ArrayList<ConformanceActivity>();
		ImperativeProcessInstance instance = schema.instantiate();
		Random random = new Random();

		while (true) {
			IImperativeNodeInstance endInstance = instance.getEndInstance();
			boolean isEndInstanceEnabled = endInstance != null && endInstance.getState().equals(INodeInstanceState.ACTIVATED);

			if (trace.size() >= minimumTraceLength && isEndInstanceEnabled) {
				instance.requestTermination();
				break;
			}
			if (trace.size() >= MAXIMUM_TERMINATION_TRACE_LENGTH) {
				break;
			}

			List<INodeInstance> activities = instance.getActiveActivities();
			Iterator<INodeInstance> iterator = activities.iterator();
			while (iterator.hasNext()) {
				INodeInstance current = iterator.next();
				if (current.getNode().getType().equals(IImperativeNode.TYPE_END_NODE)) {
					iterator.remove();
				}
			}

			if (activities.isEmpty()) {
				instance.requestTermination();
				break;
			}

			INodeInstance randomActivity = activities.get(random.nextInt(activities.size()));
			randomActivity.launch();
			randomActivity.complete();
			trace.add(new ConformanceActivity(randomActivity.getNode().getName()));
		}

		boolean terminated = instance.canTerminateExplicitely();
		return new TerminatingConformanceTrace(trace, terminated);
	}

	@Override
	public List<ConformanceActivity> generateTrace(int traceLength) {
		List<ConformanceActivity> trace = new ArrayList<ConformanceActivity>();
		ImperativeProcessInstance instance = schema.instantiate();
		Random random = new Random();

		for (int i = 0; i < traceLength; i++) {
			List<INodeInstance> activities = instance.getActiveActivities();
			if (activities.isEmpty()) {
				break;
			}

			INodeInstance randomActivity = activities.get(random.nextInt(activities.size()));
			randomActivity.launch();

			if (!randomActivity.getNode().getType().equals(IImperativeNode.TYPE_END_NODE)) {
				// end nodes complete automatically, do not end them to the trace as they are never explicitly contained in e.g., a
				// declarative model
				randomActivity.complete();
				break;
			}
			trace.add(new ConformanceActivity(randomActivity.getNode().getName()));
		}

		return trace;
	}

	@Override
	public String getModelName() {
		return schema.getName();
	}

	private ImperativeProcessInstance internalReplay(List<ConformanceActivity> trace) throws NotConformantException {
		ImperativeProcessInstance instance = schema.instantiate();
		for (int i = 0; i < trace.size(); i++) {
			ConformanceActivity activity = trace.get(i);

			List<INodeInstance> activeActivities = instance.getActiveActivities();
			INodeInstance toExecute = null;

			for (INodeInstance active : activeActivities) {
				if (active.getNode().getName().equals(activity.getName())) {
					toExecute = active;
					break;
				}
			}

			if (toExecute == null) {
				String message = MessageFormat.format("Tried to execute activity ''{0}'' (index {1}), but was not enabled.", activity
						.getName(), i + 1);
				throw new NotConformantException(trace, message);
			}

			toExecute.launch();
			toExecute.complete();
		}

		return instance;
	}

	@Override
	public void replayTrace(List<ConformanceActivity> trace) throws NotConformantException {
		internalReplay(trace);
	}

	@Override
	public void terminatesFor(TerminatingConformanceTrace trace) throws NotConformantException {
		ImperativeProcessInstance instance = internalReplay(trace.getActivities());
		boolean canTerminate = instance.canTerminateExplicitely();

		if (canTerminate ^ trace.canTerminate()) {
			String message = MessageFormat.format("Termination behavior differs: Expected {0}, but was {1}.", trace.canTerminate(),
					canTerminate);
			throw new NotConformantException(trace.getActivities(), message);
		}
	}

}
