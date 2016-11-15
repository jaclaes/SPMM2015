package org.cheetahplatform.modeler.bpmn;

import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public abstract class ConnectorDescriptor extends NodeDescriptor {

	protected ConnectorDescriptor(String imagePath, String name, String id) {
		super(imagePath, name, id);
	}

	@Override
	public ConnectionAnchor getConnectionAnchor(NodeEditPart editPart) {
		return new PathFigureAnchor(editPart.getFigure());
	}

	@Override
	public Point getInitialSize() {
		return new Point(30, 30);
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void performDirectEdit(NodeEditPart editPart) {
		// do not allow direct editing of gateways
	}

	@Override
	public void updateName(IFigure figure, String name) {
		// ignore, as connectors do not have a name
	}

}
