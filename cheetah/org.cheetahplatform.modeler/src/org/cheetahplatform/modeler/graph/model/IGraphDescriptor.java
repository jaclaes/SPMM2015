package org.cheetahplatform.modeler.graph.model;

import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IMenuManager;

public interface IGraphDescriptor {
	void buildContextMenu(GraphEditPart graphEditPart, IMenuManager menu, Point location);
}
