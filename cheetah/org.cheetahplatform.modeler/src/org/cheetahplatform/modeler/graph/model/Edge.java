package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;

public class Edge extends GraphElement {
	protected Node source;
	protected Node target;
	private EdgeLabel label;
	private List<Point> bendPoints;

	public Edge(IGenericModel parent, IGraphElementDescriptor descriptor) {
		this(parent, descriptor, Services.getIdGenerator().generateId());
	}

	public Edge(IGenericModel parent, IGraphElementDescriptor descriptor, long id) {
		super(parent, descriptor, id);

		this.label = new EdgeLabel(this);
		this.bendPoints = new ArrayList<Point>();
	}

	public void addBendPoint(Point location, int index) {
		bendPoints.add(index, location);

		firePropertyChanged(ModelerConstants.PROPERTY_BENDPOINT);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new EdgeEditPart(this);
	}

	public void delete() {
		getGraph().removeEdge(this);
		firePropertyChanged(ModelerConstants.PROPERTY_DELETED);
	}

	public Point getBendPoint(int index) {
		return bendPoints.get(index);
	}

	public int getBendPointCount() {
		return bendPoints.size();
	}

	public int getBendPointIndex(Point point) {
		return bendPoints.indexOf(point);
	}

	public List<Bendpoint> getBendPoints() {
		if (bendPoints.isEmpty()) {
			return null;
		}

		List<Bendpoint> list = new ArrayList<Bendpoint>();
		for (Point point : bendPoints) {
			list.add(new AbsoluteBendpoint(point));
		}

		return list;
	}

	@Override
	public List<? extends Object> getChildren() {
		List<Object> children = new ArrayList<Object>();
		if (name != null && !name.trim().isEmpty()) {
			children.add(label);
		}

		return children;
	}

	@Override
	public IEdgeDescriptor getDescriptor() {
		return (IEdgeDescriptor) descriptor;
	}

	public EdgeLabel getLabel() {
		return label;
	}

	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public void moveBendPoint(int toMoveIndex, Point newLocation) {
		Point toMove = bendPoints.get(toMoveIndex);
		toMove.setLocation(newLocation);

		firePropertyChanged(ModelerConstants.PROPERTY_BENDPOINT);
	}

	public Point removeBendPoint(int index) {
		if (bendPoints.isEmpty()) {
			return null;
		}

		Point removed = bendPoints.remove(index);

		firePropertyChanged(ModelerConstants.PROPERTY_BENDPOINT);
		return removed;
	}

	public void removeBendPoint(Point location) {
		bendPoints.remove(location);

		firePropertyChanged(ModelerConstants.PROPERTY_BENDPOINT);
	}

	public boolean represents(Node source, Node target, IEdgeDescriptor descriptor) {
		Assert.isNotNull(source);
		Assert.isNotNull(target);

		boolean isSameDirection = source.equals(this.source) && target.equals(this.target);
		boolean isReversedDirection = source.equals(this.target) && target.equals(this.target);
		boolean isSameDescriptor = this.descriptor.equals(descriptor);

		return (isSameDirection || isReversedDirection) && isSameDescriptor;
	}

	@Override
	public void setName(String name) {
		super.setName(name);

		if (name == null) {
			label.setOffset(new Point());
		}
	}

	public void setSource(Node source) {
		if (source == null && this.source == null) {
			return;
		}
		if (source != null && source.equals(this.source)) {
			return;
		}

		this.source = source;
		firePropertyChanged(Graph.class, ModelerConstants.PROPERTY_EDGES);
	}

	public void setTarget(Node target) {
		if (target == null && this.target == null) {
			return;
		}
		if (target != null && target.equals(this.target)) {
			return;
		}

		this.target = target;
		firePropertyChanged(Graph.class, ModelerConstants.PROPERTY_EDGES);
	}
}
