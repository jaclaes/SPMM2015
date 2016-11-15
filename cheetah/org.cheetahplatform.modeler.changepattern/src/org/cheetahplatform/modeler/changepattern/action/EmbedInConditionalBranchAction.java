package org.cheetahplatform.modeler.changepattern.action;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.EmbedInConditionalBranchChangePattern;
import org.cheetahplatform.modeler.changepattern.model.SESEChecker;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         21.06.2010
 */
public class EmbedInConditionalBranchAction extends AbstractChangePatternAction {

	/**
	 * @param embedInConditionalButton
	 * @param viewer
	 */
	public EmbedInConditionalBranchAction(PlainMultiLineButton embedInConditionalButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(embedInConditionalButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		SESEChecker seseChecker = getSESEChecker(selection);
		Edge incomingEdge = seseChecker.getIncomingEdge();
		Edge outgoingEdge = seseChecker.getOutgoingEdge();

		EmbedInConditionalBranchChangePattern changePattern = new EmbedInConditionalBranchChangePattern(viewer.getGraph(), incomingEdge,
				outgoingEdge);
		return changePattern;
	}
}
