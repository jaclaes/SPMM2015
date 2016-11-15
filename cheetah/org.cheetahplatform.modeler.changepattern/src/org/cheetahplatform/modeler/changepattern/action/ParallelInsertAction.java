package org.cheetahplatform.modeler.changepattern.action;

import java.util.Date;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.ParallelInsertChangePattern;
import org.cheetahplatform.modeler.changepattern.model.SESEChecker;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         17.06.2010
 */
public class ParallelInsertAction extends AbstractChangePatternAction {
	public ParallelInsertAction(PlainMultiLineButton parallelInsertButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(parallelInsertButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		SESEChecker seseChecker = getSESEChecker(selection);
		Edge incomingEdge = seseChecker.getIncomingEdge();
		Edge outgoingEdge = seseChecker.getOutgoingEdge();

		Date startTime = new Date();
		String name = getActivityName(null);
		if (name == null) {
			return null;
		}

		ParallelInsertChangePattern changePattern = new ParallelInsertChangePattern(viewer.getGraph(), incomingEdge, outgoingEdge, name, startTime);
		return changePattern;
	}
}
