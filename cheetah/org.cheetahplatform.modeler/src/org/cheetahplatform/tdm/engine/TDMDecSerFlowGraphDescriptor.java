package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.model.IGraphDescriptor;
import org.cheetahplatform.tdm.daily.action.CreateActivityAction;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IMenuManager;

public class TDMDecSerFlowGraphDescriptor implements IGraphDescriptor {

	@Override
	public void buildContextMenu(GraphEditPart graphEditPart, IMenuManager menu, Point location) {
		menu.add(new CreateActivityAction(graphEditPart, location));
	}
}