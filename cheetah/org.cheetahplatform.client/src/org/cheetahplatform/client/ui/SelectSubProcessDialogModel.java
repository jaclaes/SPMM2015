package org.cheetahplatform.client.ui;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;
import static org.cheetahplatform.modeler.EditorRegistry.getDescriptors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.shared.EdgeHandle;
import org.cheetahplatform.shared.GraphHandle;
import org.cheetahplatform.shared.NodeHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SelectSubProcessDialogModel {

	private static class SubProcessLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ProcessSchemaHandle handle = (ProcessSchemaHandle) element;
			return handle.getName();
		}

	}

	private final List<ProcessSchemaHandle> processes;
	private ProcessSchemaHandle selection;

	public SelectSubProcessDialogModel(List<ProcessSchemaHandle> processes) {
		this.processes = processes;
		this.selection = processes.get(0);
	}

	public Graph computeGraph() {
		GraphHandle visualization = selection.getVisualization();
		Graph graph = new Graph(getDescriptors(BPMN));
		Map<NodeHandle, Node> handleToNode = new HashMap<NodeHandle, Node>();

		for (NodeHandle nodeHandle : visualization.getNodes()) {
			Node node = new Node(graph, getDescriptor(nodeHandle));
			node.setName(nodeHandle.getName());
			handleToNode.put(nodeHandle, node);
			graph.addNode(node);

			Point location = new Point(nodeHandle.getX(), nodeHandle.getY());
			node.setLocation(location);
		}

		for (EdgeHandle edgeHandle : visualization.getEdges()) {
			Node source = handleToNode.get(edgeHandle.getSource());
			Node target = handleToNode.get(edgeHandle.getTarget());

			Edge edge = new Edge(graph, EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW));
			graph.addEdge(edge);
			edge.setSource(source);
			edge.setTarget(target);
		}

		return graph;
	}

	public IBaseLabelProvider createLabelProvider() {
		return new SubProcessLabelProvider();
	}

	private INodeDescriptor getDescriptor(NodeHandle handle) {
		String type = EditorRegistry.BPMN_ACTIVITY;

		if (handle.getType().equals(IImperativeNode.TYPE_START_NODE)) {
			type = EditorRegistry.BPMN_START_EVENT;
		} else if (handle.getType().equals(IImperativeNode.TYPE_END_NODE)) {
			type = EditorRegistry.BPMN_END_EVENT;
		}

		return (INodeDescriptor) EditorRegistry.getDescriptor(type);
	}

	public Object getInput() {
		return processes;
	}

	/**
	 * @return the selection
	 */
	public ProcessSchemaHandle getSelection() {
		return selection;
	}

	public void setSelection(ProcessSchemaHandle selection) {
		this.selection = selection;

	}

}
