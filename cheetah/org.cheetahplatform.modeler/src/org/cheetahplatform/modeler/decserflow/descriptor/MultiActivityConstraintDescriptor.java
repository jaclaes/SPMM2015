package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.eclipse.gef.RequestConstants.REQ_DELETE;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiActivityConstraint;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.CreateMultiConstraintIncomingEdgeAction;
import org.cheetahplatform.modeler.decserflow.CreateMultiConstraintOutgoingEdgeAction;
import org.cheetahplatform.modeler.decserflow.DeleteMultiConstraintEdgeAction;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.graph.policy.EdgeBendPointEditPolicy;
import org.cheetahplatform.modeler.graph.policy.EdgeEditPolicy;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;

public abstract class MultiActivityConstraintDescriptor extends AbstractConstraintDescriptor {

	public static final int NO_MAXIMUM = Integer.MAX_VALUE;

	private int minimumIncoming;
	private int maximumIncoming;
	private int minimumOutgoing;
	private int maximumOutgoing;
	private String detailedImagePath;

	protected MultiActivityConstraintDescriptor(String imagePath, String name, String id, int minimumIncoming, int maximumIncoming,
			int minimumOutgoing, int maximumOutgoing, String detailedImagePath, IDeclarativeConstraint constraint) {
		super(imagePath, name, id, constraint);

		this.minimumIncoming = minimumIncoming;
		this.maximumIncoming = maximumIncoming;
		this.minimumOutgoing = minimumOutgoing;
		this.maximumOutgoing = maximumOutgoing;
		this.detailedImagePath = detailedImagePath;
	}

	@Override
	public void buildContextMenu(EditPart editPart, IMenuManager menu, Point dropLocation) {
		super.buildContextMenu(editPart, menu, dropLocation);

		EdgeEditPart castedEditPart = (EdgeEditPart) editPart;
		Edge edge = castedEditPart.getModel();
		boolean isIncoming = false;
		Node auxiliaryNode = null;
		NodeEditPart auxiliaryNodeEditPart = null;

		if (edge.getTarget() != null && edge.getTarget().getDescriptor() instanceof AuxiliaryNodeDescriptor) {
			isIncoming = true;
			auxiliaryNode = edge.getTarget();
			auxiliaryNodeEditPart = (NodeEditPart) castedEditPart.getTarget();
		} else {
			auxiliaryNode = edge.getSource();
			auxiliaryNodeEditPart = (NodeEditPart) castedEditPart.getSource();
		}

		int outgoingCount = auxiliaryNode.getSourceConnections().size();
		int incomingCount = auxiliaryNode.getTargetConnections().size();

		if (isIncoming) {
			if (incomingCount > minimumIncoming) {
				menu.add(new DeleteMultiConstraintEdgeAction(castedEditPart));
			}
		} else {
			if (outgoingCount > minimumOutgoing) {
				menu.add(new DeleteMultiConstraintEdgeAction(castedEditPart));
			}
		}

		if (incomingCount < maximumIncoming) {
			menu.add(new CreateMultiConstraintIncomingEdgeAction(auxiliaryNodeEditPart, (MultiActivityConstraintDescriptor) edge
					.getDescriptor()));
		}
		if (outgoingCount < maximumOutgoing) {
			menu.add(new CreateMultiConstraintOutgoingEdgeAction(auxiliaryNodeEditPart, (MultiActivityConstraintDescriptor) edge
					.getDescriptor()));
		}
	}

	/**
	 * Create an instance of the constraint represented by this descriptor.
	 * 
	 * @return the constraint
	 */
	public abstract MultiActivityConstraint createConstraint();

	@Override
	public void createEditPolicies(EditPart editPart) {
		GraphEditDomain editDomain = (GraphEditDomain) editPart.getViewer().getEditDomain();
		if (editDomain.isDirectEditingEnabled()) {
			editPart.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new MultiActivityConnectionEndpointEditPolicy());
			editPart.installEditPolicy(EditPolicy.CONNECTION_ROLE, new EdgeEditPolicy());
		}

		editPart.installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new EdgeBendPointEditPolicy());
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		Edge edge = (Edge) element;
		boolean isAuxiliaryNodeSource = edge.getSource() != null && edge.getSource().getDescriptor() instanceof AuxiliaryNodeDescriptor;

		if (isAuxiliaryNodeSource) {
			return createOutgoingConnection(edge);
		}

		return createIncomingConnection(edge);
	}

	protected SelectablePolylineConnection createIncomingConnection(Edge edge) {
		SelectablePolylineConnection connection = new SelectablePolylineConnection(edge);
		connection.setAntialias(SWT.ON);

		return connection;
	}

	protected SelectablePolylineConnection createOutgoingConnection(Edge edge) {
		SelectablePolylineConnection connection = new SelectablePolylineConnection(edge);
		connection.setAntialias(SWT.ON);

		return connection;
	}

	public INodeDescriptor getAuxiliaryNodeDescriptor() {
		return (INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_AUXILIARY_NODE);
	}

	@Override
	public Command getCommand(EditPart editPart, Request request) {
		if (REQ_DELETE.equals(request.getType())) {
			return new DeleteMultiActivityConstraintCommand((Edge) editPart.getModel(), this);
		}

		return super.getCommand(editPart, request);
	}

	public String getDetailedImagePath() {
		return detailedImagePath;
	}

	public int getMaximumIncoming() {
		return maximumIncoming;
	}

	public int getMaximumOutgoing() {
		return maximumOutgoing;
	}

	public int getMinimumIncoming() {
		return minimumIncoming;
	}

	public int getMinimumOutgoing() {
		return minimumOutgoing;
	}

}
