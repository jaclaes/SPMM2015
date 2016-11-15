package org.cheetahplatform.modeler.action;

import java.util.List;

import org.cheetahplatform.modeler.graph.command.CreateEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.command.MoveEdgeBendPointCommand;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RectangularAction extends AbstractModelingEditorAction<ScrollingGraphicalViewer> {

	public static final String ID = "org.cheetahplatform.modeler.action.RectangularAction";

	public RectangularAction() {
		super(ScrollingGraphicalViewer.class);

		setId(ID);
		setText("Make edge rectangular (Ctrl+R)");
	}

	private void adaptEdge(EdgeEditPart editPart) {
		Edge edge = editPart.getModel();
		Shell shell = editPart.getViewer().getControl().getShell();
		Point source = ((AbstractGraphicalEditPart) editPart.getSource()).getFigure().getBounds().getCenter();
		Point target = ((AbstractGraphicalEditPart) editPart.getTarget()).getFigure().getBounds().getCenter();

		if (edge.getBendPoints() != null && edge.getBendPoints().size() > 1) {
			MessageDialog.openInformation(shell, "Unsupported operation", "Not more than one bend point supported currently.");
			return;
		}

		if (edge.getBendPoints() == null || edge.getBendPoints().isEmpty()) {
			// case 1: no bend points at all --> create a new one
			Point newBendPoint = new Point(source.x, target.y);
			EditPart objectUnderBendPoint = editPart.getViewer().findObjectAt(newBendPoint);
			if (objectUnderBendPoint != null && !(objectUnderBendPoint instanceof GraphEditPart)) {
				newBendPoint = new Point(target.x, source.y);
			}

			editPart.getViewer().getEditDomain().getCommandStack().execute(new CreateEdgeBendPointCommand(edge, newBendPoint, 0));
		} else {
			// case 2: bend point available --> relayout
			Point newBendPoint = null;
			Point bendPoint = edge.getBendPoint(0);

			Point possibleChoice1 = new Point(source.x, target.y);
			Point possibleChoice2 = new Point(target.x, source.y);
			double distance1 = bendPoint.getCopy().getNegated().translate(possibleChoice1).getDistance(new Point());
			double distance2 = bendPoint.getCopy().getNegated().translate(possibleChoice2).getDistance(new Point());

			if (distance1 < distance2) {
				newBendPoint = possibleChoice1;
				if (newBendPoint.equals(bendPoint)) {
					newBendPoint = possibleChoice2;
				}
			} else {
				newBendPoint = possibleChoice2;
				if (newBendPoint.equals(bendPoint)) {
					newBendPoint = possibleChoice1;
				}
			}

			editPart.getViewer().getEditDomain().getCommandStack().execute(new MoveEdgeBendPointCommand(edge, 0, newBendPoint));
		}
	}

	@Override
	protected void run(ScrollingGraphicalViewer viewer) {
		List selection = viewer.getSelectedEditParts();
		for (Object editPart : selection) {
			if (!(editPart instanceof EdgeEditPart)) {
				continue;
			}

			adaptEdge((EdgeEditPart) editPart);
		}

		if (selection.isEmpty()) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "No selection", "Please select an edge first.");
		}
	}

}
