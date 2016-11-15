package org.cheetahplatform.modeler.graph.editpart;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_LAYOUT;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_NAME;

import java.beans.PropertyChangeEvent;

import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.EdgeLabelConnectionEndPointLocator;
import org.cheetahplatform.modeler.graph.action.DeleteConditionAction;
import org.cheetahplatform.modeler.graph.command.EditConditionAction;
import org.cheetahplatform.modeler.graph.model.EdgeLabel;
import org.cheetahplatform.modeler.graph.policy.EdgeLabelEditPolicy;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.action.IMenuManager;

public class EdgeLabelEditPart extends GenericEditPart {
	private EdgeLabelConnectionEndPointLocator labelLocator;
	private EdgeLabelFigure edgeLabelFigure;

	public EdgeLabelEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		GraphEditDomain domain = (GraphEditDomain) getViewer().getEditDomain();
		if (!domain.isEditable()) {
			return;
		}

		menu.add(new EditConditionAction((EdgeEditPart) getParent()));
		menu.add(new DeleteConditionAction(this));
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new EdgeLabelEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		edgeLabelFigure = new EdgeLabelFigure();
		EdgeLabel model = (EdgeLabel) getModel();
		labelLocator = new EdgeLabelConnectionEndPointLocator((Connection) ((AbstractGraphicalEditPart) getParent()).getFigure(), false);
		labelLocator.setUDistance(0);
		labelLocator.setVDistance(-10);
		labelLocator.setOffset(model.getOffset());
		return edgeLabelFigure;
	}

	@Override
	public void performRequest(Request req) {
		if (REQ_OPEN.equals(req.getType())) {
			new EditConditionAction((EdgeEditPart) getParent()).run();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (PROPERTY_LAYOUT.equals(property) || PROPERTY_NAME.equals(property)) {
			update();
		}
	}

	@Override
	protected void refreshVisuals() {
		EdgeLabel model = (EdgeLabel) getModel();
		String text = model.getText();
		IGenericEditPart parent = (IGenericEditPart) getParent();
		if (parent == null) {
			return;
		}
		IFigure parentFigure = parent.getFigure();

		if (text == null) {
			if (parentFigure.getChildren().contains(getFigure())) {
				parentFigure.remove(getFigure());
			}

			setSelected(SELECTED_NONE);
		} else {
			if (!parentFigure.getChildren().contains(getFigure())) {
				parentFigure.add(getFigure());
			}

			parentFigure.setConstraint(figure, labelLocator);
		}

		edgeLabelFigure.setText(text);
	}

	private void update() {
		EdgeLabel model = (EdgeLabel) getModel();
		Point offset = model.getOffset();
		labelLocator.setOffset(offset);

		refresh();
	}
}
