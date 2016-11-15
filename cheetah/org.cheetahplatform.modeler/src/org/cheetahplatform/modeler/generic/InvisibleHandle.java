package org.cheetahplatform.modeler.generic;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;

public class InvisibleHandle extends MoveHandle {

	public InvisibleHandle(GraphicalEditPart owner) {
		super(owner);
	}

	@Override
	public void paint(Graphics graphics) {
		// ignore
	}

}
