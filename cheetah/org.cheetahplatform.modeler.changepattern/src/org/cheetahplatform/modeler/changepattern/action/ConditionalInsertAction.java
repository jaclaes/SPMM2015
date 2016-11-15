package org.cheetahplatform.modeler.changepattern.action;

import java.util.Date;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.ConditionalInsertChangePattern;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         17.06.2010
 */
public class ConditionalInsertAction extends AbstractChangePatternAction {

	public ConditionalInsertAction(PlainMultiLineButton conditionalInsertButton, GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(conditionalInsertButton, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		Edge edge = extractConnection(selection);

		Date startTime = new Date();
		String name = getActivityName(null);
		if (name == null) {
			return null;
		}

		ConditionalInsertChangePattern changePattern = new ConditionalInsertChangePattern(viewer.getGraph(), edge, name, startTime);
		return changePattern;
	}

	@Override
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		return extractConnection(selection) != null;
	}
}
