package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.shared.ListenerList;
import org.eclipse.gef.EditPart;

public class Graph extends GenericModel {
	private List<Node> nodes;
	private List<Edge> edges;
	private ListenerList logListeners;
	private final List<IGraphElementDescriptor> descriptors;
	private IGraphDescriptor descriptor;

	public Graph(List<IGraphElementDescriptor> descriptors) {
		this(descriptors, new DefaultGraphDescriptor());
	}

	public Graph(List<IGraphElementDescriptor> descriptors, IGraphDescriptor descriptor) {
		super(null);

		this.descriptors = descriptors;
		this.descriptor = descriptor;
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
		this.logListeners = new ListenerList();
	}

	public void addEdge(Edge edge) {
		edges.add(edge);

		firePropertyChanged(ModelerConstants.PROPERTY_EDGES);
	}

	public void addLogListener(ILogListener listener) {
		logListeners.add(listener);
	}

	public void addNode(Node node) {
		nodes.add(node);

		firePropertyChanged(ModelerConstants.PROPERTY_NODES);
	}

	public boolean canRemoveEdge(Edge edge) {
		return !edges.contains(edge);
	}

	/**
	 * @param graphElement
	 * @return
	 */
	public boolean contains(GraphElement graphElement) {
		return getNodes().contains(graphElement) || getEdges().contains(graphElement);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new GraphEditPart(this);
	}

	public boolean existsConnection(Node source, Node target, IEdgeDescriptor descriptor) {
		for (Edge edge : edges) {
			if (edge.represents(source, target, descriptor)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<? extends Object> getChildren() {
		return nodes;
	}

	public IGraphDescriptor getDescriptor() {
		return descriptor;
	}

	public IGraphElementDescriptor getDescriptor(String id) {
		for (IGraphElementDescriptor descriptor : descriptors) {
			if (descriptor.getId().equals(id)) {
				return descriptor;
			}
		}

		return null;
	}

	/**
	 * Returns the edges.
	 * 
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return Collections.unmodifiableList(edges);
	}

	public List<IGraphElementDescriptor> getElementDescriptors() {
		return Collections.unmodifiableList(descriptors);
	}

	private GraphElement getGraphElement(List<? extends GraphElement> elements, long id) {
		for (GraphElement element : elements) {
			if (element.getId() == id) {
				return element;
			}
		}

		return null;
	}

	public GraphElement getGraphElement(long id) {
		GraphElement node = getGraphElement(nodes, id);
		if (node != null) {
			return node;
		}

		return getGraphElement(edges, id);
	}

	public List<GraphElement> getGraphElements() {
		List<GraphElement> elements = new ArrayList<GraphElement>();
		elements.addAll(getNodes());
		elements.addAll(getEdges());
		return elements;
	}

	public List<Node> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	public List<Edge> getSourceConnections(Node node) {
		List<Edge> sourceConnections = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if (node.equals(edge.getSource())) {
				sourceConnections.add(edge);
			}
		}

		return sourceConnections;
	}

	public List<Edge> getTargetConnections(Node node) {
		List<Edge> targetConnections = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if (node.equals(edge.getTarget())) {
				targetConnections.add(edge);
			}
		}

		return targetConnections;
	}

	public void log(AuditTrailEntry entry) {
		for (Object listener : logListeners.getListeners()) {
			((ILogListener) listener).log(entry);
		}
	}

	public void removeEdge(Edge edge) {
		if (edges.remove(edge)) {
			firePropertyChanged(ModelerConstants.PROPERTY_EDGES);
		}
	}

	public void removeLogListener(ILogListener listener) {
		logListeners.remove(listener);
	}

	public void removeNode(Node node) {
		if (nodes.remove(node)) {
			for (Edge edge : edges) {
				if (node.equals(edge.getSource())) {
					edge.setSource(null);
				}
				if (node.equals(edge.getTarget())) {
					edge.setTarget(null);
				}
			}

			firePropertyChanged(ModelerConstants.PROPERTY_EDGES);
		}
	}

	public void setDescriptor(IGraphDescriptor descriptor) {
		this.descriptor = descriptor;
	}

}
