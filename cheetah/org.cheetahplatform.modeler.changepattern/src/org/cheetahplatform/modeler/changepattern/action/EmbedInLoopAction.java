package org.cheetahplatform.modeler.changepattern.action;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.EmbedInLoopChangePattern;
import org.cheetahplatform.modeler.changepattern.model.SESEChecker;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         21.06.2010
 */
public class EmbedInLoopAction extends AbstractChangePatternAction {
	public EmbedInLoopAction(PlainMultiLineButton embedInLoopButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(embedInLoopButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		SESEChecker seseChecker = getSESEChecker(selection);
		Edge incomingEdge = seseChecker.getIncomingEdge();
		Edge outgoingEdge = seseChecker.getOutgoingEdge();

		EmbedInLoopChangePattern changePattern = new EmbedInLoopChangePattern(viewer.getGraph(), incomingEdge, outgoingEdge);
		return changePattern;
	}
}
