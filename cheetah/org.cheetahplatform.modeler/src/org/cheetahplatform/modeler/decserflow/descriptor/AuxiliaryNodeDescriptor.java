package org.cheetahplatform.modeler.decserflow.descriptor;

import static org.eclipse.gef.RequestConstants.REQ_DELETE;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.GraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import com.swtdesigner.SWTResourceManager;

public class AuxiliaryNodeDescriptor extends NodeDescriptor {

	public AuxiliaryNodeDescriptor() {
		super("", "Auxiliary Node", EditorRegistry.DECSERFLOW_AUXILIARY_NODE);
	}

	protected AuxiliaryNodeDescriptor(String imagePath, String name, String id) {
		super(imagePath, name, id);
	}

	@Override
	public boolean allowsResizing() {
		return false;
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		GraphElementFigure figure = new GraphElementFigure();
		figure.setBackgroundColor(SWTResourceManager.getColor(0, 0, 0));
		figure.setOpaque(true);

		return figure;
	}

	@Override
	public Command getCommand(EditPart editPart, Request request) {
		if (REQ_DELETE.equals(request.getType())) {
			return null;// auxiliary nodes can only be deleted by deleting the edges
		}

		return super.getCommand(editPart, request);
	}

	@Override
	public Point getInitialSize() {
		return new Point(1, 1);
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void updateName(IFigure figure, String name) {
		// ignore
	}

}
