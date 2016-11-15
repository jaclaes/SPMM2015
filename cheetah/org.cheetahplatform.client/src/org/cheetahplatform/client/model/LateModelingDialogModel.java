package org.cheetahplatform.client.model;

import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_END_NODE;
import static org.cheetahplatform.core.imperative.modeling.IImperativeNode.TYPE_START_NODE;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_END_EVENT;
import static org.cheetahplatform.modeler.EditorRegistry.BPMN_START_EVENT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.client.ui.SelectLateModelingActivityDialog;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.shared.ActivityHandle;
import org.cheetahplatform.shared.EdgeHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.cheetahplatform.shared.NodeHandle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class LateModelingDialogModel {

	private class LateModelingActivityDescriptor extends ActivityDescriptor {
		@Override
		public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			SelectLateModelingActivityDialog dialog = new SelectLateModelingActivityDialog(shell, activities);
			if (dialog.open() != Window.OK) {
				return false;
			}

			ActivityHandle activity = dialog.getSelectedActivity();
			element.setProperty(KEY_ACTIVITY, activity);
			((CreateNodeCommand) command).setName(activity.getName());

			return true;
		}

		@Override
		public boolean hasCustomName() {
			return false; // do not allows renaming, as only predefined activites are allowed
		}
	}

	private final List<ActivityHandle> activities;
	private Graph graph;

	public static final String KEY_ACTIVITY = "KEY_ACTIVITY";

	public LateModelingDialogModel(List<ActivityHandle> activities) {
		this.activities = activities;
	}

	public Graph computeGraph() {
		IGraphElementDescriptor activity = new LateModelingActivityDescriptor();
		IGraphElementDescriptor sequenceFlow = EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW);
		IGraphElementDescriptor startEvent = EditorRegistry.getDescriptor(EditorRegistry.BPMN_START_EVENT);
		IGraphElementDescriptor endEvent = EditorRegistry.getDescriptor(EditorRegistry.BPMN_END_EVENT);

		List<IGraphElementDescriptor> descriptors = new ArrayList<IGraphElementDescriptor>();
		descriptors.add(startEvent);
		descriptors.add(endEvent);
		descriptors.add(activity);
		descriptors.add(sequenceFlow);

		graph = new Graph(descriptors);
		Node startNode = new Node(graph, (INodeDescriptor) startEvent);
		startNode.setProperty(KEY_ACTIVITY, new ActivityHandle(-1, "", TYPE_START_NODE));
		startNode.setLocation(new Point(100, 100));
		graph.addNode(startNode);
		Node endNode = new Node(graph, (INodeDescriptor) endEvent);
		endNode.setProperty(KEY_ACTIVITY, new ActivityHandle(-2, "", TYPE_END_NODE));
		endNode.setLocation(new Point(500, 100));
		graph.addNode(endNode);

		return graph;
	}

	public GraphHandle getProcessSchema() {
		GraphHandle schema = new GraphHandle(0, "", "");
		Map<Node, NodeHandle> nodeToHandle = new HashMap<Node, NodeHandle>();

		for (Node node : graph.getNodes()) {
			ActivityHandle activity = (ActivityHandle) node.getProperty(KEY_ACTIVITY);
			String type = INode.TYPE_ACTIVITY;

			if (node.getDescriptor().getId().equals(BPMN_START_EVENT)) {
				type = IImperativeNode.TYPE_START_NODE;
			} else if (node.getDescriptor().getId().equals(BPMN_END_EVENT)) {
				type = IImperativeNode.TYPE_END_NODE;
			}

			NodeHandle nodeHandle = new NodeHandle(activity.getId(), activity.getName(), type);
			nodeToHandle.put(node, nodeHandle);
			schema.addNode(nodeHandle);
		}

		for (Edge edge : graph.getEdges()) {
			NodeHandle source = nodeToHandle.get(edge.getSource());
			NodeHandle target = nodeToHandle.get(edge.getTarget());
			EdgeHandle edgeHandle = new EdgeHandle("", source, target);
			schema.addEdge(edgeHandle);
		}

		return schema;
	}

	public String validate() {
		for (Edge edge : graph.getEdges()) {
			if (edge.getSource() == null || edge.getTarget() == null) {
				return "All sequence flows must be connected.";
			}
		}

		int startNodeCount = 0;
		int endNodeCount = 0;
		for (Node node : graph.getNodes()) {
			if (node.getDescriptor().getId().equals(BPMN_START_EVENT)) {
				startNodeCount++;
				if (node.hasTargetConnections()) {
					return "The start event is not allowed to have incoming sequence flows.";
				}
				if (node.getSourceConnections().size() != 1) {
					return "The start event must have exactly one outgoing sequence flow.";
				}

				continue;
			} else if (node.getDescriptor().getId().equals(BPMN_END_EVENT)) {
				endNodeCount++;
				if (node.hasSourceConnections()) {
					return "The end event is not allowed to have outgoing sequence flows.";
				}
				if (node.getTargetConnections().size() != 1) {
					return "The end event must have exactly one incoming sequence flow.";
				}

				continue;
			}

			if (node.getSourceConnections().size() != 1 || node.getTargetConnections().size() != 1) {
				return "Each activity must have exactly one ingoing and one outgoing edge.";
			}
		}

		return null;
	}
}
