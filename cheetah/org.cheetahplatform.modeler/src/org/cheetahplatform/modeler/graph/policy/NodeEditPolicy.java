package org.cheetahplatform.modeler.graph.policy;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.tools.SelectEditPartTracker;

public class NodeEditPolicy extends ResizableEditPolicy {

	private IFigure feedback;
	private GraphicalNodeEditPolicy connectionEditPolicy;

	public NodeEditPolicy() {
		connectionEditPolicy = new NodeGraphicalNodeEditPolicy();
	}

	@Override
	protected List createSelectionHandles() {
		List<MoveHandle> handles = new ArrayList<MoveHandle>();
		INodeDescriptor descriptor = ((Node) getHost().getModel()).getDescriptor();
		MoveHandle handle = descriptor.createSelectionHandle((NodeEditPart) getHost());
		handle.setDragTracker(new SelectEditPartTracker(getHost()));
		handle.setCursor(Cursors.ARROW);
		handles.add(handle);
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.NODES_RESIZABLE)
				&& ((Node) getHost().getModel()).getDescriptor().allowsResizing()) {
			ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), handles, PositionConstants.SOUTH_EAST);
		}

		handles.addAll(descriptor.createHandles((NodeEditPart) getHost()));

		return handles;
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		IFigure feedbackLayer = getFeedbackLayer();
		if (feedbackLayer.getChildren().contains(feedback)) {
			feedbackLayer.remove(feedback);
			feedback = null;
		} else {
			connectionEditPolicy.eraseSourceFeedback(request);
		}
	}

	@Override
	public Command getCommand(Request request) {
		if (workingOnForeignViewer(request)) {
			return null;
		}

		Object type = request.getType();
		if (REQ_ORPHAN.equals(type) || REQ_MOVE.equals(type)) {
			Node node = (Node) getHost().getModel();
			return new MoveNodeCommand(node, ((ChangeBoundsRequest) request).getMoveDelta());
		}
		if (REQ_CONNECTION_START.equals(type) || REQ_CONNECTION_END.equals(type)) {
			return connectionEditPolicy.getCommand(request);
		}
		if (REQ_RECONNECT_TARGET.equals(request.getType()) || REQ_RECONNECT_SOURCE.equals(request.getType())) {
			ReconnectRequest casted = (ReconnectRequest) request;

			// do not allow to disconnect from auxiliary nodes
			EditPart source = casted.getConnectionEditPart().getSource();
			if (casted.isMovingStartAnchor() && source != null
					&& ((Node) source.getModel()).getDescriptor() instanceof AuxiliaryNodeDescriptor) {
				return null;
			}
			EditPart target = casted.getConnectionEditPart().getTarget();
			if (!casted.isMovingStartAnchor() && target != null
					&& ((Node) target.getModel()).getDescriptor() instanceof AuxiliaryNodeDescriptor) {
				return null;
			}

			return connectionEditPolicy.getCommand(request);
		}
		if (REQ_RESIZE.equals(type)) {
			INodeDescriptor descriptor = ((NodeEditPart) getHost()).getModel().getDescriptor();
			if (!descriptor.allowsResizing()) {
				return null;
			}

			return descriptor.getCommand(getHost(), request);
		}

		return getDescriptor().getCommand(getHost(), request);
	}

	private INodeDescriptor getDescriptor() {
		NodeEditPart editPart = (NodeEditPart) getHost();
		INodeDescriptor descriptor = editPart.getDescriptor();
		return descriptor;
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		Object type = request.getType();

		if (REQ_ADD.equals(type)) {
			return getHost().getParent();
		}
		if (REQ_CONNECTION_START.equals(type) || REQ_CONNECTION_END.equals(type) || REQ_RECONNECT_SOURCE.equals(type)
				|| REQ_RECONNECT_TARGET.equals(type) || REQ_SELECTION.equals(type)) {
			return getHost();
		}

		return getDescriptor().getTargetEditPart(getHost(), request);
	}

	@Override
	public void setHost(EditPart host) {
		super.setHost(host);

		connectionEditPolicy.setHost(host);
	}

	private void showMoveFeedback(ChangeBoundsRequest request, Dimension customDimension) {
		Node model = (Node) getHost().getModel();
		if (model.getDescriptor() instanceof AuxiliaryNodeDescriptor) {
			return;
		}

		if (feedback == null) {
			feedback = model.getDescriptor().createFigure(model);
			feedback.setBounds(model.getBounds());
			((IGraphElementFigure) feedback).setSelected(true);

			IFigure feedbackLayer = getFeedbackLayer();
			feedbackLayer.add(feedback);
		}

		Point location = model.getBounds().getLocation();
		location.translate(request.getMoveDelta());
		feedback.setLocation(location);
		if (customDimension != null) {
			feedback.setSize(customDimension);
		}

	}

	@Override
	public void showSourceFeedback(Request request) {
		if (workingOnForeignViewer(request)) {
			eraseSourceFeedback(request);
			return;
		}

		Object type = request.getType();
		if (REQ_ORPHAN.equals(type) || REQ_MOVE.equals(type)
				|| (REQ_RESIZE.equals(type) && CheetahPlatformConfigurator.getBoolean(IConfiguration.NODES_RESIZABLE))) {
			ChangeBoundsRequest casted = (ChangeBoundsRequest) request;
			Dimension newDimenstion = null;
			Node node = (Node) getHost().getModel();

			if (REQ_RESIZE.equals(type)) {
				if (!node.getDescriptor().allowsResizing()) {
					return;
				}
				newDimenstion = node.getBounds().getSize().getCopy();
				newDimenstion.expand(casted.getSizeDelta());

			}
			showMoveFeedback(casted, newDimenstion);
		}
		if (REQ_CONNECTION_END.equals(type)) {
			connectionEditPolicy.showSourceFeedback(request);
		}
	}

	private boolean workingOnForeignViewer(Request request) {
		if (request.getExtendedData().containsKey(CommonConstants.KEY_VIEWER)) {
			if (!request.getExtendedData().get(CommonConstants.KEY_VIEWER).equals(getHost().getViewer())) {
				return true;
			}
		}

		return false;
	}

}
