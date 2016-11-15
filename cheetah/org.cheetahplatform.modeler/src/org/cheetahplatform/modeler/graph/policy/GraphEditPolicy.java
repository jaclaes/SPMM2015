package org.cheetahplatform.modeler.graph.policy;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.generic.GEFUtils;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.EdgeLabelEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.daily.editpart.ActivityEditPart;
import org.cheetahplatform.tdm.daily.figure.ActivityFeedbackFigure;
import org.cheetahplatform.tdm.daily.policy.ActivityEditPolicy;
import org.cheetahplatform.tdm.daily.policy.IFeedbackHelper;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

public class GraphEditPolicy extends ContainerEditPolicy {
	private class CustomMarqueeTool extends MarqueeSelectionTool {
		@Override
		protected void handleFinished() {
			super.handleFinished();

			deactivate();
			EditDomain domain = getHost().getViewer().getEditDomain();
			domain.setActiveTool(originalTool);
		}
	}

	private IFigure feedback;
	private List<IFigure> activityFeedbackFigures;
	private Tool originalTool;

	public GraphEditPolicy() {
		this.activityFeedbackFigures = new ArrayList<IFigure>();
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		IFigure feedbackLayer = LayerManager.Helper.find(getHost()).getLayer(LayerConstants.FEEDBACK_LAYER);

		removeFeeddbackFigure(feedbackLayer, feedback);
		feedback = null;
		for (IFigure activityFeedback : activityFeedbackFigures) {
			removeFeeddbackFigure(feedbackLayer, activityFeedback);
		}
		activityFeedbackFigures.clear();
	}

	@Override
	public Command getCommand(Request request) {
		if (request.getType().equals(REQ_RECONNECT_SOURCE) || request.getType().equals(REQ_RECONNECT_TARGET)) {
			return handleMultiNodeEdgeReconnectCommand((ReconnectRequest) request);
		}

		// automatically activate the marquee selection tool if necessary
		if (!(request instanceof ChangeBoundsRequest)) {
			return super.getCommand(request);
		}

		ChangeBoundsRequest casted = (ChangeBoundsRequest) request;
		Command command = handleForeignViewerRequest(casted);
		if (command != null) {
			return command;
		}

		for (Object part : casted.getEditParts()) {
			if (part instanceof EdgeLabelEditPart) {
				return null;
			}
		}

		GraphEditDomain domain = (GraphEditDomain) getHost().getViewer().getEditDomain();
		Tool activeTool = domain.getActiveTool();
		if (!(activeTool instanceof MarqueeSelectionTool)) {
			originalTool = activeTool;

			CustomMarqueeTool tool = new CustomMarqueeTool();
			domain.setActiveTool(tool);
			tool.activate();

			MouseEvent event = domain.getLastMouseDown();
			tool.mouseDown(event, getHost().getViewer());
		}

		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (!(request.getNewObjectType() instanceof INodeDescriptor)) {
			return null;
		}

		Point location = GEFUtils.getDropLocation(getHost().getViewer());
		((GraphicalEditPart) getHost()).getFigure().translateToRelative(location);
		Node node = (Node) request.getNewObject();
		CreateNodeCommand command = new CreateNodeCommand((Graph) getHost().getModel(), node, location);
		return command;
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		Object type = request.getType();
		if (REQ_CREATE.equals(type) || REQ_ADD.equals(type) || REQ_MOVE.equals(type)) {
			return getHost();
		}
		if (REQ_RECONNECT_SOURCE.equals(type)) {
			ReconnectRequest casted = (ReconnectRequest) request;
			EditPart source = casted.getConnectionEditPart().getSource();
			if (source != null && ((GraphElement) source.getModel()).getDescriptor() instanceof AuxiliaryNodeDescriptor) {
				return getHost();
			}
		}
		if (REQ_RECONNECT_TARGET.equals(type)) {
			ReconnectRequest casted = (ReconnectRequest) request;
			EditPart target = casted.getConnectionEditPart().getTarget();
			if (target != null && ((GraphElement) target.getModel()).getDescriptor() instanceof AuxiliaryNodeDescriptor) {
				return getHost();
			}
		}

		return null;
	}

	private void handleForeginViewerTargetFeedback(Request request) {
		if (!(request instanceof ChangeBoundsRequest)) {
			return;
		}

		ChangeBoundsRequest casted = (ChangeBoundsRequest) request;
		for (Object editPart : casted.getEditParts()) {
			if (!(editPart instanceof ActivityEditPart)) {
				return;
			}
		}

		if (activityFeedbackFigures.isEmpty()) {
			IFigure feedbackLayer = LayerManager.Helper.find(getHost()).getLayer(LayerConstants.FEEDBACK_LAYER);
			for (Object editPart : casted.getEditParts()) {
				ActivityEditPart castedEditPart = (ActivityEditPart) editPart;
				ActivityEditPolicy policy = (ActivityEditPolicy) castedEditPart.getEditPolicy(EditPolicy.COMPONENT_ROLE);
				IFeedbackHelper feedbackHelper = policy.getFeedbackHelper();
				ActivityFeedbackFigure figure = feedbackHelper.createActivityFigure(castedEditPart.getModel(), false, false);
				figure.setSize(feedbackHelper.getDefaultFeedbackFigureSize());
				activityFeedbackFigures.add(figure);
				feedbackLayer.add(figure);
			}
		}

		// layout the activities
		org.eclipse.swt.graphics.Point cursor = Display.getDefault().getCursorLocation();
		cursor = getHost().getViewer().getControl().toControl(cursor);
		Point cursorRelative = new Point(cursor);
		((AbstractGraphicalEditPart) getHost()).getFigure().translateToRelative(cursorRelative);

		int x = cursorRelative.x;
		int y = cursorRelative.y;
		for (IFigure figure : activityFeedbackFigures) {
			figure.setLocation(new Point(x, y));
			x += figure.getSize().width + 10;
		}
	}

	private Command handleForeignViewerRequest(ChangeBoundsRequest casted) {
		if (casted.getEditParts().isEmpty()) {
			return null;
		}

		for (Object editPart : casted.getEditParts()) {
			if (!(editPart instanceof ActivityEditPart)) {
				return null;
			}
		}

		CompoundCommand command = new CompoundCommand();
		for (Object editPart : casted.getEditParts()) {
			Command subCommand = ((ActivityEditPart) editPart).getCommand(new Request(RequestConstants.REQ_DELETE));
			command.add(subCommand);
		}

		return command;
	}

	private Command handleMultiNodeEdgeReconnectCommand(ReconnectRequest request) {
		ConnectionEditPart editPart = request.getConnectionEditPart();
		Node auxiliaryNode = null;

		Node source = (Node) editPart.getSource().getModel();
		Node target = (Node) editPart.getTarget().getModel();
		if (source.getDescriptor() instanceof AuxiliaryNodeDescriptor) {
			auxiliaryNode = source;
		} else {
			auxiliaryNode = target;
		}

		Point moveDelta = request.getLocation().getCopy();
		moveDelta.translate(auxiliaryNode.getLocation().getCopy().negate());
		return new MoveNodeCommand(auxiliaryNode, moveDelta);
	}

	protected void removeFeeddbackFigure(IFigure feedbackLayer, IFigure feedback) {
		if (feedbackLayer.getChildren().contains(feedback)) {
			feedbackLayer.remove(feedback);
		}
	}

	private void showAddNodeFeedback(CreateRequest request) {
		if (!(request.getNewObjectType() instanceof INodeDescriptor)) {
			return;
		}

		Node node = (Node) request.getNewObject();
		if (feedback == null) {
			feedback = node.getDescriptor().createFigure(node);
			IFigure feedbackLayer = LayerManager.Helper.find(getHost()).getLayer(LayerConstants.FEEDBACK_LAYER);
			feedback.setSize(node.getBounds().getSize());
			feedbackLayer.add(feedback);
		}

		Point location = request.getLocation().getCopy();
		((GraphicalEditPart) getHost()).getFigure().translateToRelative(location);
		feedback.setLocation(location);
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType())) {
			showAddNodeFeedback((CreateRequest) request);
		}

		handleForeginViewerTargetFeedback(request);
	}
}
