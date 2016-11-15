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

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.modeling.sese.ISingleEntrySingleExitComponent;
import org.cheetahplatform.core.imperative.modeling.sese.SingleEntrySingleExitDecomposer;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateBindingBoxInstance;
import org.cheetahplatform.core.imperative.runtime.LateModelingBoxInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.eclipse.core.runtime.Assert;

public class ImperativeFitnessHelper implements ILogListener {

	public static final ImperativeFitnessHelper INSTANCE = new ImperativeFitnessHelper();

	public static boolean hasChildAccessorSyntax(String expression) {
		return expression.contains("[");
	}

	public static String parseName(String text) {
		if (!text.contains("[")) {
			return text;
		}

		return text.substring(0, text.indexOf("["));
	}

	public static int parseOccurrence(String activity) {
		int occurrence = 0;

		if (activity.contains("[")) {
			String occurrenceString = activity.substring(activity.indexOf("[") + 1);
			occurrenceString = occurrenceString.replaceAll("]", "");
			occurrence = Integer.parseInt(occurrenceString);
		}

		return occurrence;
	}

	private final Map<String, INode> nodes = new HashMap<String, INode>();

	private ImperativeProcessSchema imperativeProcessSchema;

	private ImperativeProcessInstance processInstance;

	private List<AuditTrailEntry> logEntries = new ArrayList<AuditTrailEntry>();

	public void addToLateBindingBox(String boxName, String actionString) {
		LateBindingBox box = (LateBindingBox) nodes.get(boxName);
		ImperativeProcessSchema schema = new ImperativeProcessSchema(boxName + box.getSequences().size());
		IImperativeNode previousNode = schema.getStart();
		String[] actions = actionString.split(",");

		for (String action : actions) {
			checkName(action);
			ImperativeActivity activity = schema.createActivity(action.trim());
			previousNode.addSuccessor(activity);
			previousNode = activity;
		}

		previousNode.addSuccessor(schema.getEnd());
		box.addSequence(schema);
	}

	public void addToLateModelingBox(String boxName, String actionString) {
		LateModelingBox box = (LateModelingBox) nodes.get(boxName);
		String[] actions = actionString.split(",");

		for (String name : actions) {
			checkName(name);
			ImperativeActivity activity = imperativeProcessSchema.createActivity(name.trim());
			Assert.isTrue(!nodes.containsKey(name));
			nodes.put(name, activity);
			box.addActivity(activity);
		}
	}

	public void cancel(String activityName, int occurrence) {
		IImperativeNodeInstance nodeInstance = getNodeInstance(activityName, occurrence);
		nodeInstance.cancel();
	}

	private void checkName(String name) {
		Assert.isNotNull(name);
		Assert.isNotNull(imperativeProcessSchema, "Process schema was null.");
		Assert.isTrue(!nodes.containsKey(name), "Node name already in use!");
	}

	public void complete(String activityName, int occurrence) {
		IImperativeNodeInstance nodeInstance = getNodeInstance(activityName, occurrence);
		nodeInstance.complete();
	}

	public void createActivity(String activityName) {
		checkName(activityName);

		ImperativeActivity activity = imperativeProcessSchema.createActivity(activityName);
		nodes.put(activityName, activity);
	}

	public void createAndJoin(String name) {
		checkName(name);

		AndJoin join = imperativeProcessSchema.createAndJoin(name);
		nodes.put(name, join);
	}

	public void createAndSplit(String name) {
		checkName(name);

		AndSplit split = imperativeProcessSchema.createAndSplit(name);
		nodes.put(name, split);
	}

	public void createLateBindingBox(String name) {
		checkName(name);
		LateBindingBox lateBindingNode = imperativeProcessSchema.createLateBindingNode(name);
		nodes.put(name, lateBindingNode);
	}

	public void createLateModelingBox(String name) {
		checkName(name);
		LateModelingBox box = imperativeProcessSchema.createLateModelingBox(name);
		nodes.put(name, box);
	}

	public void createLoopEnd(String name) {
		checkName(name);

		LoopEnd loopEnd = imperativeProcessSchema.createLoopEnd(name);
		nodes.put(name, loopEnd);
	}

	public void createLoopStart(String name) {
		checkName(name);

		LoopStart start = imperativeProcessSchema.createLoopStart(name);
		nodes.put(name, start);
	}

	public void createProcessSchema() {
		imperativeProcessSchema = new ImperativeProcessSchema("schema");

		nodes.clear();
		logEntries.clear();
		for (INode node : imperativeProcessSchema.getNodes()) {
			nodes.put(node.getName(), node);
		}
	}

	public void createXorJoin(String name) {
		checkName(name);

		XorJoin split = imperativeProcessSchema.createXorJoin(name);
		nodes.put(name, split);
	}

	public void createXorSplit(String splitName) {
		checkName(splitName);

		XorSplit split = imperativeProcessSchema.createXorSplit(splitName);
		nodes.put(splitName, split);
	}

	public ISingleEntrySingleExitComponent decomposeToSingleEntrySingleExit() {
		SingleEntrySingleExitDecomposer decomposer = new SingleEntrySingleExitDecomposer();
		imperativeProcessSchema.getStart().accept(decomposer);
		return decomposer.getComponent();
	}

	public List<INodeInstance> getActiveActivities() {
		return processInstance.getActiveActivities();
	}

	public List<LateBindingBoxInstance> getActiveLateBindingBoxes() {
		return processInstance.getActiveLateBindingBoxes();
	}

	public List<LateModelingBoxInstance> getActiveLateModelingBoxes() {
		return processInstance.getActiveLateModelingBoxes();
	}

	public List<AuditTrailEntry> getNewLogEntries() {
		List<AuditTrailEntry> entries = new ArrayList<AuditTrailEntry>(logEntries);
		logEntries.clear();
		return entries;
	}

	public List<IImperativeNodeInstance> getNode(String string) {
		NodeInstanceVisitor visitor = new NodeInstanceVisitor();
		processInstance.visit(visitor);

		return visitor.getNodeInstances(string);

	}

	public INodeInstance getNodeInstance(long cheetahId) {
		NodeInstanceVisitor visitor = new NodeInstanceVisitor();
		processInstance.visit(visitor);

		IImperativeNodeInstance nodeInstance = visitor.getNodeInstance(cheetahId);
		return nodeInstance;
	}

	private IImperativeNodeInstance getNodeInstance(String activityName, int occurrence) {
		activityName = activityName.replaceAll(" ", "");
		NodeInstanceVisitor visitor = new NodeInstanceVisitor();
		processInstance.visit(visitor);

		IImperativeNodeInstance nodeInstance = visitor.getNodeInstance(activityName, occurrence);
		return nodeInstance;
	}

	/**
	 * Return the processInstance.
	 * 
	 * @return the processInstance
	 */
	public ImperativeProcessInstance getProcessInstance() {
		return processInstance;
	}

	public INodeInstanceState getState(String activityName, int occurrence) {
		IImperativeNodeInstance nodeInstance = getNodeInstance(activityName, occurrence);
		INodeInstanceState state = nodeInstance.getState();
		return state;
	}

	public int getSuccessorCount(String activityName, int occurrence) {
		IImperativeNodeInstance nodeInstance = getNodeInstance(activityName, occurrence);
		return nodeInstance.getSuccessors().size();
	}

	public void instantiateSchema() {
		processInstance = imperativeProcessSchema.instantiate();
		processInstance.addLogListener(this, true);
	}

	public boolean isAbsent(String activityName, int occurrence) {
		activityName = activityName.replaceAll(" ", "");
		NodeInstanceVisitor visitor = new NodeInstanceVisitor();
		processInstance.visit(visitor);

		return !visitor.contains(activityName, occurrence);
	}

	public boolean isValidState(String activityName, int occurrence, String expectedState) {
		INodeInstanceState state = getState(activityName, occurrence);
		return state.getName().equalsIgnoreCase(expectedState.replaceAll(" ", ""));
	}

	public void launch(String activityName, int occurrence) {
		IImperativeNodeInstance nodeInstance = getNodeInstance(activityName, occurrence);
		nodeInstance.launch();
	}

	public void link(String nodeName1, String nodeName2) {
		IImperativeNode node1 = (IImperativeNode) nodes.get(nodeName1);
		IImperativeNode node2 = (IImperativeNode) nodes.get(nodeName2);

		Assert.isNotNull(node1, "Cannot find a node for name " + nodeName1);
		Assert.isNotNull(node2, "Cannot find a node for name " + nodeName2);

		node1.addSuccessor(node2);
	}

	@Override
	public void log(AuditTrailEntry entry) {
		logEntries.add(entry);
	}

	public void selectActivitiesFromBox(String name, List<String> toSelect) {
		LateModelingBoxInstance instance = (LateModelingBoxInstance) getNodeInstance(name, 0);
		ImperativeProcessSchema subProcess = new ImperativeProcessSchema();
		IImperativeNode current = subProcess.getStart();

		for (String string : toSelect) {
			INode node = nodes.get(string);
			Assert.isNotNull(node, "No node for name: " + string);
			current.addSuccessor((IImperativeNode) node);
			current = (IImperativeNode) node;
		}

		current.addSuccessor(subProcess.getEnd());

		instance.selectSubProcess(subProcess);
	}

	public void selectSequence(String boxName, int number) {
		LateBindingBoxInstance instance = (LateBindingBoxInstance) getNodeInstance(boxName, 0);
		LateBindingBox box = (LateBindingBox) instance.getNode();
		ImperativeProcessSchema toSelect = box.getSequences().get(number);
		instance.selectSubProcess(toSelect);
	}

	public void skip(String activity, int occurrence) {
		IImperativeNodeInstance nodeInstance = getNodeInstance(activity, occurrence);
		nodeInstance.skip(false);
	}
}
