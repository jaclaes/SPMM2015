package org.cheetahplatform.modeler.graph.editpart;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_EDGES;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_NODES;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.policy.GraphEditPolicy;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.action.IMenuManager;

import com.swtdesigner.SWTResourceManager;

public class GraphEditPart extends GenericEditPart {

	public GraphEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		getModel().getDescriptor().buildContextMenu(this, menu, dropLocation);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new GraphEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setBackgroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLayoutManager(new XYLayout());

		return figure;
	}

	@Override
	public Graph getModel() {
		return (Graph) super.getModel();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (PROPERTY_NODES.equals(name)) {
			refresh(true);
		}
		if (PROPERTY_EDGES.equals(name)) {
			refresh(true);
		}
	}

}
