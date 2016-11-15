package org.cheetahplatform.modeler.changepattern.action;

import java.util.Set;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.DeleteProcessFragmentChangePattern;
import org.cheetahplatform.modeler.changepattern.model.SESEChecker;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         18.06.2010
 */
public class DeleteProcessFragmentAction extends AbstractChangePatternAction {

	public DeleteProcessFragmentAction(PlainMultiLineButton deleteButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(deleteButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (!MessageDialog.openQuestion(shell, "Delete Process Fragment", "Do you really want to delete the selected process fragment?")) {
			return null;
		}

		SESEChecker seseChecker = getSESEChecker(selection);
		Edge incomingEdge = seseChecker.getIncomingEdge();
		Edge outgoingEdge = seseChecker.getOutgoingEdge();
		Set<Node> nodes = seseChecker.getNodes();

		DeleteProcessFragmentChangePattern changePattern = new DeleteProcessFragmentChangePattern(viewer.getGraph(), incomingEdge,
				outgoingEdge, nodes);
		return changePattern;
	}
}
