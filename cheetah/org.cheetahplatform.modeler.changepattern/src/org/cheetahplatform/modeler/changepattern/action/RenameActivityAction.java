package org.cheetahplatform.modeler.changepattern.action;

import java.util.Date;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.RenameActivityChangePattern;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         18.06.2010
 */
public class RenameActivityAction extends AbstractChangePatternAction {

	/**
	 * @param renameActivityButton
	 * @param viewer
	 */
	public RenameActivityAction(PlainMultiLineButton renameActivityButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(renameActivityButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {

		NodeEditPart element = (NodeEditPart) selection.getFirstElement();

		Date startTime = new Date();
		String activityName = getActivityName(element.getModel().getName());
		if (activityName == null) {
			return null;
		}

		RenameActivityChangePattern changePattern = new RenameActivityChangePattern(viewer.getGraph(), element.getModel(), activityName,
				startTime);
		return changePattern;
	}

	@Override
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		if (selection.size() != 1) {
			return false;
		}

		Object element = selection.getFirstElement();
		if (!(element instanceof NodeEditPart)) {
			return false;
		}

		NodeEditPart nodeEditPart = (NodeEditPart) element;
		Node node = nodeEditPart.getModel();
		String type = node.getDescriptor().getId();
		return EditorRegistry.BPMN_ACTIVITY.equals(type);
	}
}
