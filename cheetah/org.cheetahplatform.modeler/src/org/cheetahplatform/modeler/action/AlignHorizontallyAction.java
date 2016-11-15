package org.cheetahplatform.modeler.action;

import java.util.List;

import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

public class AlignHorizontallyAction extends AbstractModelingEditorAction<ScrollingGraphicalViewer> {
	public static final String ID = " org.cheetahplatform.modeler.action.AlignHorizontallyAction";

	public AlignHorizontallyAction() {
		super(ScrollingGraphicalViewer.class);

		setText("Align Horizontally (Ctrl+H)");
		setId(ID);
	}

	@Override
	protected void run(ScrollingGraphicalViewer viewer) {
		List selection = viewer.getSelectedEditParts();
		if (selection.isEmpty()) {
			return;
		}

		int minimumY = Integer.MAX_VALUE;
		for (Object editPart : selection) {
			if (!(editPart instanceof NodeEditPart)) {
				continue;
			}

			int y = ((AbstractGraphicalEditPart) editPart).getFigure().getBounds().y;
			if (y < minimumY) {
				minimumY = y;
			}
		}

		CompoundCommand command = new CompoundCommand();
		for (Object editPart : selection) {
			if (!(editPart instanceof NodeEditPart)) {
				continue;
			}

			Node model = ((NodeEditPart) editPart).getModel();
			int modelY = model.getLocation().y;
			MoveNodeCommand moveCommand = new MoveNodeCommand(model, new Point(0, -(modelY - minimumY)));
			command.add(moveCommand);
		}

		viewer.getEditDomain().getCommandStack().execute(command);
	}
}
