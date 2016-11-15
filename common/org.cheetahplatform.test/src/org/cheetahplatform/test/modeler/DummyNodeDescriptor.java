package org.cheetahplatform.test.modeler;

import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public class DummyNodeDescriptor extends NodeDescriptor {

	private final boolean hasCustomName;

	public DummyNodeDescriptor(String name, String id, boolean hasCustomName) {
		super("", name, id);

		this.hasCustomName = hasCustomName;
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		// ignore
		return null;
	}

	@Override
	public Point getInitialSize() {
		return new Point(60, 60);
	}

	@Override
	public boolean hasCustomName() {
		return hasCustomName;
	}

	@Override
	public void updateName(IFigure figure, String name) {
		// ignore
	}

}