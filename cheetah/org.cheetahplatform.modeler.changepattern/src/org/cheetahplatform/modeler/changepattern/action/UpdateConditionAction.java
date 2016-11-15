package org.cheetahplatform.modeler.changepattern.action;

import java.util.Date;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.UpdateConditionChangePattern;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         18.06.2010
 */
public class UpdateConditionAction extends AbstractChangePatternAction {

	/**
	 * @param updateConditionButton
	 * @param viewer
	 */
	public UpdateConditionAction(PlainMultiLineButton updateConditionButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(updateConditionButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		EdgeEditPart edgeEditPart = (EdgeEditPart) selection.getFirstElement();
		Edge edge = edgeEditPart.getModel();

		Date startTime = new Date();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		InputDialog dialog = new InputDialog(shell, "Condition", "Please enter a valid condition.", edge.getName(), null);
		if (!(dialog.open() == Window.OK)) {
			return null;
		}
		String condition = dialog.getValue();

		UpdateConditionChangePattern updateConditionChangePattern = new UpdateConditionChangePattern(viewer.getGraph(), edge, condition,
				startTime);
		return updateConditionChangePattern;
	}

	@Override
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		if (selection.size() != 1) {
			return false;
		}

		Object element = selection.getFirstElement();
		return element instanceof EdgeEditPart;
	}

}
