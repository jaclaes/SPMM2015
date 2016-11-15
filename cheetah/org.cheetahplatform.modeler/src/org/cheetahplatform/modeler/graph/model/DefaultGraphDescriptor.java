package org.cheetahplatform.modeler.graph.model;

import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IMenuManager;

public class DefaultGraphDescriptor implements IGraphDescriptor {

	@Override
	public void buildContextMenu(GraphEditPart graphEditPart, IMenuManager menu, Point location) {
		// no context menu by default
	}

}
