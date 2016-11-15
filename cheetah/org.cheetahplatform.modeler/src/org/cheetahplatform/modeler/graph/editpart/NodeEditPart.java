package org.cheetahplatform.modeler.graph.editpart;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_BACKGROUND_COLOR;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_LAYOUT;
import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_NAME;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.action.DeleteNodeAction;
import org.cheetahplatform.modeler.graph.action.RenameGraphElementAction;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.swtdesigner.SWTResourceManager;

public class NodeEditPart extends GenericEditPart implements org.eclipse.gef.NodeEditPart {

	public NodeEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		GraphEditDomain domain = (GraphEditDomain) getViewer().getEditDomain();
		if (!domain.isDirectEditingEnabled()) {
			return;
		}

		Node model = getModel();
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_DELETION_OF_NODES)) {
			menu.add(new DeleteNodeAction(this));
		}
		if (model.getDescriptor().hasCustomName() && CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_RENAMING_OF_NODES)) {
			menu.add(new RenameGraphElementAction(this));
		}

		model.getDescriptor().buildContextMenu(this, menu, dropLocation);
	}

	@Override
	protected void createEditPolicies() {
		getModel().getDescriptor().createEditPolicies(this);
	}

	@Override
	protected IFigure createFigure() {
		Node model = getModel();
		IFigure figure = model.getDescriptor().createFigure(model);
		figure.addMouseListener(new MouseListener.Stub() {

			@Override
			public void mousePressed(MouseEvent me) {
				moveToTop();
			}

		});

		addEditPartListener(new EditPartListener.Stub() {
			@Override
			public void selectedStateChanged(EditPart part) {
				NodeEditPart.this.selectedStateChanged();
			}

		});

		return figure;
	}

	protected void editName() {
		new RenameGraphElementAction(this).run();
	}

	@Override
	public Object getAdapter(Class key) {
		return getModel().getDescriptor().getAdapter(getModel(), key);
	}

	private ConnectionAnchor getConnectionAnchor() {
		Node model = getModel();
		return model.getDescriptor().getConnectionAnchor(this);
	}

	public INodeDescriptor getDescriptor() {
		return (INodeDescriptor) ((GraphElement) getModel()).getDescriptor();
	}

	@Override
	public Node getModel() {
		return (Node) super.getModel();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	@SuppressWarnings("unchecked")
	private void moveToTop() {
		List children = figure.getParent().getChildren();
		children.remove(figure);
		children.add(Math.max(0, children.size()), figure);
	}

	@Override
	public void performRequest(Request req) {
		Node model = getModel();

		if (REQ_DIRECT_EDIT.equals(req.getType())) {
			(model.getDescriptor()).performDirectEdit(this);
		}
		if (REQ_OPEN.equals(req.getType()) && model.getDescriptor().hasCustomName()) {
			GraphEditDomain editDomain = (GraphEditDomain) getViewer().getEditDomain();
			if (editDomain.isDirectEditingEnabled()) {
				editName();
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (PROPERTY_LAYOUT.equals(event.getPropertyName())) {
			updateLayout();
		} else if (PROPERTY_NAME.equals(event.getPropertyName())) {
			updateName();
		}
		if (PROPERTY_BACKGROUND_COLOR.equals(event.getPropertyName())) {
			updateBackgroundColor();
		}

		((GraphElement) getModel()).getDescriptor().propertyChanged(this, event);
	}

	@Override
	protected void refreshVisuals() {
		updateLayout();
	}

	protected void selectedStateChanged() {
		int selectionState = getSelected();
		boolean selected = (selectionState == SELECTED) || (selectionState == SELECTED_PRIMARY);
		((IGraphElementFigure) getFigure()).setSelected(selected);
	}

	private void updateBackgroundColor() {
		RGB rgb = (RGB) getModel().getProperty(PROPERTY_BACKGROUND_COLOR);
		Color color = null;
		if (rgb != null) {
			color = SWTResourceManager.getColor(rgb);
		}
		getFigure().setBackgroundColor(color);
	}

	private void updateLayout() {
		Node model = getModel();
		figure.getParent().setConstraint(figure, model.getBounds());
	}

	private void updateName() {
		Node model = getModel();
		(model.getDescriptor()).updateName(figure, model.getName());
	}
}
