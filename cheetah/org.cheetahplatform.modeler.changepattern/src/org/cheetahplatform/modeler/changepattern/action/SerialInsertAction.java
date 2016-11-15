package org.cheetahplatform.modeler.changepattern.action;

import java.util.Date;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.changepattern.model.AbstractChangePattern;
import org.cheetahplatform.modeler.changepattern.model.SerialInsertChangePattern;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.06.2010
 */
public class SerialInsertAction extends AbstractChangePatternAction {
	public SerialInsertAction(PlainMultiLineButton button, final GraphicalGraphViewerWithFlyoutPalette viewer) {
		super(button, viewer);
	}

	@Override
	protected AbstractChangePattern createChangePattern(IStructuredSelection selection) {
		Edge edge = extractConnection(selection);

		Date startTime = new Date();
		String name = getActivityName(null);
		if (name == null) {
			return null;
		}

		AbstractChangePattern changePattern = new SerialInsertChangePattern(viewer.getGraph(), edge, name, startTime);
		return changePattern;
	}

	@Override
	protected boolean isChangePatternExecutable(IStructuredSelection selection) {
		Edge connection = extractConnection(selection);
		return connection != null;
	}
}
