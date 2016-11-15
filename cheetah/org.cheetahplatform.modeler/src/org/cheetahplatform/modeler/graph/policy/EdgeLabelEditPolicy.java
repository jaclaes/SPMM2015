package org.cheetahplatform.modeler.graph.policy;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.command.MoveEdgeLabelCommand;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.editpart.EdgeLabelEditPart;
import org.cheetahplatform.modeler.graph.model.EdgeLabel;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.SelectEditPartTracker;

public class EdgeLabelEditPolicy extends NonResizableEditPolicy {
	@Override
	protected List createSelectionHandles() {
		List<MoveHandle> handles = new ArrayList<MoveHandle>();
		MoveHandle handle = new MoveHandle((GraphicalEditPart) getHost());

		if (!isDragAllowed()) {
			handle.setDragTracker(new SelectEditPartTracker(getHost()));
			handle.setCursor(Cursors.ARROW);
		}
		handles.add(handle);

		return handles;
	}

	@Override
	public Command getCommand(Request request) {
		if (REQ_DELETE.equals(request.getType())) {
			return new RenameCommand((GraphElement) getHost().getParent().getModel(), null);
		}

		return super.getCommand(request);
	}

	@Override
	protected Command getMoveCommand(ChangeBoundsRequest request) {
		EdgeLabelEditPart editPart = (EdgeLabelEditPart) getHost();
		return new MoveEdgeLabelCommand((EdgeLabel) editPart.getModel(), request.getMoveDelta());
	}

	@Override
	protected Command getOrphanCommand(Request request) {
		EdgeLabelEditPart editPart = (EdgeLabelEditPart) getHost();
		return new MoveEdgeLabelCommand((EdgeLabel) editPart.getModel(), ((ChangeBoundsRequest) request).getMoveDelta());
	}

}
