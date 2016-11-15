package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

public class Node extends GraphElement implements ILocated {
	private Rectangle bounds;

	public Node(Graph parent, INodeDescriptor descriptor) {
		this(parent, (NodeDescriptor) descriptor, Services.getIdGenerator().generateId());
	}

	public Node(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);

		Point size = descriptor.getInitialSize();
		this.bounds = new Rectangle(0, 0, size.x, size.y);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new NodeEditPart(this);
	}

	public void delete() {
		getGraph().removeNode(this);
	}

	public List<Edge> getAllConnectedEdges() {
		List<Edge> connectedEdges = new ArrayList<Edge>();
		connectedEdges.addAll(getSourceConnections());
		connectedEdges.addAll(getTargetConnections());
		return connectedEdges;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public INodeDescriptor getDescriptor() {
		return (INodeDescriptor) descriptor;
	}

	@Override
	public Point getLocation() {
		return bounds.getLocation();
	}

	@Override
	public List<Edge> getSourceConnections() {
		return getGraph().getSourceConnections(this);
	}

	@Override
	public List<Edge> getTargetConnections() {
		return getGraph().getTargetConnections(this);
	}

	public boolean hasSourceConnections() {
		return !getSourceConnections().isEmpty();
	}

	public boolean hasTargetConnections() {
		return !getTargetConnections().isEmpty();
	}

	@Override
	public void move(Point moveDelta) {
		bounds.translate(moveDelta);

		firePropertyChanged(ModelerConstants.PROPERTY_LAYOUT);
	}

	public void setLocation(Point location) {
		bounds.setLocation(location);
	}

	public void setSize(Dimension dimension) {
		this.bounds.setSize(dimension);
		firePropertyChanged(ModelerConstants.PROPERTY_LAYOUT);
	}

	@Override
	public String toString() {
		return getNameNullSafe();
	}

}
