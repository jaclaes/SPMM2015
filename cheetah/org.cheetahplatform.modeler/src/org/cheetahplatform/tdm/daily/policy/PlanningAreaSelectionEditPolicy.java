package org.cheetahplatform.tdm.daily.policy;

import java.lang.reflect.Method;
import java.util.List;

import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.tdm.daily.editpart.AbstractPlanningAreaEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

public class PlanningAreaSelectionEditPolicy extends NonResizableEditPolicy {

	@Override
	public AbstractPlanningAreaEditPart getHost() {
		return (AbstractPlanningAreaEditPart) super.getHost();
	}

	private GraphicalViewer getViewer() {
		return (GraphicalViewer) getHost().getViewer();
	}

	@Override
	protected void showSelection() {
		MouseEvent lastMouseDown = ((CustomEditDomain) getViewer().getEditDomain()).getCurrentMouseDown();
		if (lastMouseDown != null) {
			int button = lastMouseDown.button;
			int leftButton = 1;
			if (button != leftButton) {
				unselect();
				return;
			}
		}

		org.eclipse.swt.graphics.Point location = Display.getDefault().getCursorLocation();
		org.eclipse.swt.graphics.Point relative = getViewer().getControl().toControl(location);

		getHost().selectTimeslot(new Point(relative));
		unselect();
	}

	private void unselect() {
		getHost().setSelected(EditPart.SELECTED_NONE);
		setSelectedState(EditPart.SELECTED_NONE);
		getViewer().getSelectionManager().setSelection(new StructuredSelection());

		try {
			Method selectionMethod = AbstractEditPartViewer.class.getDeclaredMethod("primGetSelectedEditParts", new Class[0]);
			selectionMethod.setAccessible(true);
			List selection = (List) selectionMethod.invoke(getViewer());
			selection.clear();
		} catch (Exception e) {
			Activator.logError("Could not clear the selection.", e);
		}
	}
}