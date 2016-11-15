package org.cheetahplatform.modeler.decserflow.descriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.FeedbackHelper;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.ConnectionEndpointHandle;
import org.eclipse.gef.requests.ReconnectRequest;

public class MultiActivityConnectionEndpointEditPolicy extends SelectionHandlesEditPolicy {

	private static class FeedbackInfo {
		private FeedbackHelper feedbackHelper;
		private ConnectionAnchor originalAnchor;
		private boolean movingStartAnchor;
		private ConnectionEditPart editPart;

		public FeedbackInfo(FeedbackHelper feedbackHelper, ConnectionAnchor originalAnchor, boolean movingStartAnchor,
				ConnectionEditPart editPart) {
			this.feedbackHelper = feedbackHelper;
			this.originalAnchor = originalAnchor;
			this.movingStartAnchor = movingStartAnchor;
			this.editPart = editPart;
		}

		public ConnectionEditPart getEditPart() {
			return editPart;
		}

		public FeedbackHelper getFeedbackHelper() {
			return feedbackHelper;
		}

		public ConnectionAnchor getOriginalAnchor() {
			return originalAnchor;
		}

		public boolean isMovingStartAnchor() {
			return movingStartAnchor;
		}

	}

	private Map<EditPart, FeedbackInfo> editPartToFeedback;
	private NodeEditPart auxiliaryNode;

	public MultiActivityConnectionEndpointEditPolicy() {
		this.editPartToFeedback = new HashMap<EditPart, FeedbackInfo>();
	}

	@Override
	protected List createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		list.add(new ConnectionEndpointHandle((ConnectionEditPart) getHost(), ConnectionLocator.TARGET));
		list.add(new ConnectionEndpointHandle((ConnectionEditPart) getHost(), ConnectionLocator.SOURCE));

		return list;
	}

	/**
	 * Erases connection move feedback. This method is called when a ReconnectRequest is received.
	 * 
	 * @param request
	 *            the reconnect request.
	 */
	protected void eraseConnectionMoveFeedback() {
		for (FeedbackInfo info : editPartToFeedback.values()) {
			ConnectionAnchor originalAnchor = info.getOriginalAnchor();
			if (originalAnchor == null) {
				continue;
			}

			Connection connection = (Connection) info.getEditPart().getFigure();
			if (info.isMovingStartAnchor()) {
				connection.setSourceAnchor(originalAnchor);
			} else {
				connection.setTargetAnchor(originalAnchor);
			}
		}

		if (auxiliaryNode != null) {
			IFigure graphFigure = ((GraphicalEditPart) auxiliaryNode.getParent()).getFigure();
			graphFigure.add(auxiliaryNode.getFigure());
		}

		auxiliaryNode = null;
		editPartToFeedback.clear();
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	@Override
	public void eraseSourceFeedback(Request request) {
		if (REQ_RECONNECT_TARGET.equals(request.getType()) || REQ_RECONNECT_SOURCE.equals(request.getType())) {
			eraseConnectionMoveFeedback();
		}
	}

	/**
	 * Convenience method for obtaining the host's <code>Connection</code> figure.
	 * 
	 * @return the Connection figure
	 */
	protected Connection getConnection() {
		return (Connection) ((GraphicalEditPart) getHost()).getFigure();
	}

	/**
	 * Lazily creates and returns the feedback helper for the given request. The helper will be configured as either moving the source or
	 * target end of the connection.
	 * 
	 * @param request
	 *            the reconnect request
	 * @return the feedback helper
	 */
	protected FeedbackInfo getFeedbackInfo(ReconnectRequest request, ConnectionEditPart editPart) {
		FeedbackInfo feedbackInfo = editPartToFeedback.get(editPart);

		if (feedbackInfo == null) {
			FeedbackHelper feedbackHelper = new FeedbackHelper();
			feedbackHelper.setConnection((Connection) editPart.getFigure());

			boolean isMovingStartAnchor = false;
			if (editPart.equals(getHost())) {
				isMovingStartAnchor = request.isMovingStartAnchor();
			} else {
				if (((GraphElement) editPart.getSource().getModel()).getDescriptor() instanceof AuxiliaryNodeDescriptor) {
					isMovingStartAnchor = true;
				}
			}

			feedbackHelper.setMovingStartAnchor(isMovingStartAnchor);

			ConnectionAnchor originalAnchor = null;
			if (isMovingStartAnchor) {
				originalAnchor = ((Connection) editPart.getFigure()).getSourceAnchor();
			} else {
				originalAnchor = ((Connection) editPart.getFigure()).getTargetAnchor();
			}

			feedbackInfo = new FeedbackInfo(feedbackHelper, originalAnchor, isMovingStartAnchor, editPart);
			editPartToFeedback.put(editPart, feedbackInfo);
		}

		return feedbackInfo;
	}

	/**
	 * Shows or updates connection move feedback. Called whenever a show feedback request is received for reconnection.
	 * 
	 * @param request
	 *            the reconnect request
	 */
	@SuppressWarnings("unchecked")
	protected void showConnectionMoveFeedback(ReconnectRequest request) {
		ConnectionEditPart connectionToReconnect = request.getConnectionEditPart();
		boolean isTargetAuxiliary = ((GraphElement) connectionToReconnect.getTarget().getModel()).getDescriptor() instanceof AuxiliaryNodeDescriptor;
		boolean isMovingSource = request.isMovingStartAnchor();
		if (isMovingSource && !isTargetAuxiliary) {
			auxiliaryNode = (NodeEditPart) connectionToReconnect.getSource();
		} else if (!isMovingSource && isTargetAuxiliary) {
			auxiliaryNode = (NodeEditPart) connectionToReconnect.getTarget();
		}

		if (auxiliaryNode != null) {
			IFigure graphFigure = ((GraphicalEditPart) auxiliaryNode.getParent()).getFigure();
			if (graphFigure.getChildren().contains(auxiliaryNode.getFigure())) {
				graphFigure.remove(auxiliaryNode.getFigure());
			}

			List<Object> connectionsToAdapt = new ArrayList<Object>();
			connectionsToAdapt.addAll(auxiliaryNode.getSourceConnections());
			connectionsToAdapt.addAll(auxiliaryNode.getTargetConnections());

			for (Object object : connectionsToAdapt) {
				ConnectionEditPart editPart = (ConnectionEditPart) object;
				FeedbackInfo helper = getFeedbackInfo(request, editPart);
				helper.getFeedbackHelper().update(null, request.getLocation());
			}
		} else {
			ConnectionAnchor anchor = null;
			if (request.getTarget() instanceof NodeEditPart) {
				NodeEditPart node = (NodeEditPart) request.getTarget();
				if (request.isMovingStartAnchor()) {
					anchor = node.getSourceConnectionAnchor(request);
				} else {
					anchor = node.getTargetConnectionAnchor(request);
				}
			}

			FeedbackInfo helper = getFeedbackInfo(request, request.getConnectionEditPart());
			helper.getFeedbackHelper().update(anchor, request.getLocation());
		}
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	@Override
	public void showSourceFeedback(Request request) {
		if (REQ_RECONNECT_SOURCE.equals(request.getType()) || REQ_RECONNECT_TARGET.equals(request.getType())) {
			showConnectionMoveFeedback((ReconnectRequest) request);
		}
	}

}
