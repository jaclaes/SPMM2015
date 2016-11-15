package org.cheetahplatform.modeler.graph.editpart;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_BACKGROUND_COLOR;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_BENDPOINT;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_DELETED;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_NAME;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.editpart.GenericConnectionEditPart;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection.PointPair;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.action.DeleteEdgeAction;
import org.cheetahplatform.modeler.graph.command.EditConditionAction;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class EdgeEditPart extends GenericConnectionEditPart {
	private boolean disableRemoveNotify;
	private EditPart parent;
	private ConnectionAnchor sourceConnectionAnchor;
	private ConnectionAnchor targetConnectionAnchor;

	public EdgeEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		GraphEditDomain domain = (GraphEditDomain) getViewer().getEditDomain();
		if (!domain.isDirectEditingEnabled()) {
			return;
		}

		Edge edge = getModel();
		menu.add(new DeleteEdgeAction(this));
		if (edge.getDescriptor().hasCustomName()) {
			menu.add(new EditConditionAction(this));
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.DELETE_BEND_POINTS)) {
			menu.add(new DeleteBendPointsAction(this));
		}

		edge.getDescriptor().buildContextMenu(this, menu, dropLocation);
	}

	@Override
	protected void createEditPolicies() {
		getModel().getDescriptor().createEditPolicies(this);
	}

	@Override
	protected IFigure createFigure() {
		Edge edge = getModel();
		return edge.getDescriptor().createFigure(edge);
	}

	@Override
	public void deactivate() {
		Edge model = getModel();
		if (!model.getGraph().canRemoveEdge(model)) {
			return;
		}

		List c = getChildren();
		for (int i = 0; i < c.size(); i++) {
			((EditPart) c.get(i)).deactivate();
		}

		deactivateEditPolicies();

		setFlag(FLAG_ACTIVE, false);
		fireDeactivated();
	}

	@Override
	public Edge getModel() {
		return (Edge) super.getModel();
	}

	@Override
	public RootEditPart getRoot() {
		if (getParent() == null) {
			return parent.getRoot();
		}

		return super.getRoot();
	}

	@Override
	protected ConnectionAnchor getSourceConnectionAnchor() {
		if (getSource() instanceof NodeEditPart) {
			NodeEditPart editPart = (NodeEditPart) getSource();
			// cache the anchor as we also allow lone standing edges
			sourceConnectionAnchor = editPart.getSourceConnectionAnchor(this);
		} else if (getSource() == null) {
			PointPair points = SelectablePolylineConnection.getPoints(getModel());
			Point location = points.getSource().getCopy();
			IFigure parentFigure = ((AbstractGraphicalEditPart) parent.getChildren().get(0)).getFigure();
			parentFigure.translateToAbsolute(location);
			sourceConnectionAnchor = new XYAnchor(location);
		}

		return sourceConnectionAnchor;
	}

	@Override
	protected ConnectionAnchor getTargetConnectionAnchor() {
		if (getTarget() instanceof NodeEditPart) {
			NodeEditPart editPart = (NodeEditPart) getTarget();
			// cache the anchor as we also allow lone standing edges
			targetConnectionAnchor = editPart.getTargetConnectionAnchor(this);
		} else if (getTarget() == null) {
			PointPair points = SelectablePolylineConnection.getPoints(getModel());
			Point location = points.getTarget().getCopy();
			IFigure parentFigure = ((AbstractGraphicalEditPart) parent.getChildren().get(0)).getFigure();
			parentFigure.translateToAbsolute(location);
			targetConnectionAnchor = new XYAnchor(location);
		}

		return targetConnectionAnchor;
	}

	@Override
	public EditPartViewer getViewer() {
		return parent.getViewer();
	}

	@Override
	public void performRequest(Request reqeuest) {
		getModel().getDescriptor().performRequest(this, reqeuest);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();

		if (PROPERTY_DELETED.equals(property)) {
			deactivate();
			disableRemoveNotify = false;
			removeNotify();
		}
		if (PROPERTY_NAME.equals(property)) {
			((IGraphElementFigure) getFigure()).setName(((GraphElement) getModel()).getName());
			refresh(true);
		}
		if (PROPERTY_BENDPOINT.equals(property)) {
			setLayout();
		}
		if (PROPERTY_BACKGROUND_COLOR.equals(property)) {
			RGB rgb = (RGB) getModel().getProperty(PROPERTY_BACKGROUND_COLOR);
			Color color = SWTResourceManager.getColor(0, 0, 0);
			if (rgb != null) {
				color = SWTResourceManager.getColor(rgb);
			}
			getFigure().setBackgroundColor(color);
		}

		getModel().getDescriptor().propertyChanged(this, event);
	}

	@Override
	protected void refreshVisuals() {
		Connection connection = (Connection) getFigure();
		connection.getConnectionRouter().setConstraint(connection, (getModel()).getBendPoints());
	}

	@Override
	public void removeNotify() {
		if (disableRemoveNotify) {
			return;
		}

		disableRemoveNotify = false;
		if (getLayer(LayerConstants.CONNECTION_LAYER).getChildren().contains(getFigure())) {
			super.removeNotify();
		}
	}

	private void setLayout() {
		PolylineConnection connection = (PolylineConnection) getFigure();
		connection.getConnectionRouter().setConstraint(connection, (getModel()).getBendPoints());
		connection.layout();
	}

	@Override
	public void setParent(EditPart parent) {
		if (parent != null) {
			this.parent = parent;
		}

		boolean wasNull = getParent() == null;
		boolean becomingNull = parent == null;
		if (becomingNull && !wasNull) {
			// extend to avoid the removing of edges which are not explicitly deleted by the user
			Edge model = getModel();
			disableRemoveNotify = !model.getGraph().canRemoveEdge(model);
		}
		super.setParent(parent);
		if (wasNull && !becomingNull) {
			addNotify();
		}
	}

}
