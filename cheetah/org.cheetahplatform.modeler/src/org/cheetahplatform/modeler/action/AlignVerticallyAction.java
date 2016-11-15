package org.cheetahplatform.modeler.action;

import java.util.List;

import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

public class AlignVerticallyAction extends AbstractModelingEditorAction<ScrollingGraphicalViewer> {
	public static final String ID = " org.cheetahplatform.modeler.action.AlignVerticallyAction";

	public AlignVerticallyAction() {
		super(ScrollingGraphicalViewer.class);

		setText("Align Vertically (Ctrl+V)");
		setId(ID);
	}

	@Override
	protected void run(ScrollingGraphicalViewer viewer) {
		List selection = viewer.getSelectedEditParts();
		if (selection.isEmpty()) {
			return;
		}

		int minimumX = Integer.MAX_VALUE;
		for (Object editPart : selection) {
			if (!(editPart instanceof NodeEditPart)) {
				continue;
			}

			int x = ((AbstractGraphicalEditPart) editPart).getFigure().getBounds().x;
			if (x < minimumX) {
				minimumX = x;
			}
		}

		CompoundCommand command = new CompoundCommand();
		for (Object editPart : selection) {
			if (!(editPart instanceof NodeEditPart)) {
				continue;
			}

			Node model = ((NodeEditPart) editPart).getModel();
			int modelX = model.getLocation().x;
			MoveNodeCommand moveCommand = new MoveNodeCommand(model, new Point(-(modelX - minimumX), 0));
			command.add(moveCommand);
		}

		viewer.getEditDomain().getCommandStack().execute(command);
	}
}