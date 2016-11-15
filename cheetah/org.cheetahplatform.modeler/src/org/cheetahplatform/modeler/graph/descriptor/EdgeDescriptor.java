package org.cheetahplatform.modeler.graph.descriptor;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.ID;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.SOURCE_NODE_ID;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.TARGET_NODE_ID;
import static org.eclipse.gef.RequestConstants.REQ_OPEN;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.figure.FilledArrowFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.generic.figure.SelectablePolylineConnection;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.EditConditionAction;
import org.cheetahplatform.modeler.graph.command.ReconnectEdgeCommand;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.graph.policy.EdgeBendPointEditPolicy;
import org.cheetahplatform.modeler.graph.policy.EdgeEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class EdgeDescriptor extends GraphElementDescriptor implements IEdgeDescriptor {

	public EdgeDescriptor(String imagePath, String name, String id) {
		super(imagePath, name, id);
	}

	@Override
	public AbstractGraphCommand createCreateEdgeCommand(Graph graph, AuditTrailEntry entry) {
		Edge edge = (Edge) AbstractGraphCommand.createGraphElement(entry, graph);
		Node source = (Node) graph.getGraphElement(entry.getLongAttribute(SOURCE_NODE_ID));
		Node target = (Node) graph.getGraphElement(entry.getLongAttribute(TARGET_NODE_ID));
		String name = entry.getAttribute(NAME);

		if (edge.getDescriptor().getName().equals(name)) {
			name = null;
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
			String add = " (" + entry.getAttribute(ID) + ")";
			if (name == null || name.equals("null")) {
				name = entry.getAttribute(ID);
			} else if (!name.equals(entry.getAttribute(ID)) && !name.contains(add)) {
				name += add;
			}
		} else if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)) {
			name = entry.getAttribute(ID);
		}

		return new CreateEdgeCommand(graph, edge, source, target, name);
	}

	@Override
	public AbstractGraphCommand createCreateEdgeCommand(Graph graph, Edge edge, Node source, Node target, String name) {
		return new CreateEdgeCommand(graph, edge, source, target, name);
	}

	@Override
	public void createEditPolicies(EditPart editPart) {
		GraphEditDomain editDomain = (GraphEditDomain) editPart.getViewer().getEditDomain();
		if (editDomain.isDirectEditingEnabled()) {
			editPart.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
			editPart.installEditPolicy(EditPolicy.CONNECTION_ROLE, new EdgeEditPolicy());
		}
		editPart.installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new EdgeBendPointEditPolicy());
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		SelectablePolylineConnection connection = new SelectablePolylineConnection((Edge) element);
		connection.setAntialias(SWT.ON);
		connection.setTargetDecoration(new FilledArrowFigure());
		connection.setBackgroundColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		return connection;
	}

	@Override
	public Edge createModel(Graph graph) {
		return new Edge(graph, this);
	}

	@Override
	public Edge createModel(Graph graph, long id, AuditTrailEntry entry) {
		return new Edge(graph, this, id);
	}

	@Override
	public AbstractGraphCommand createReconnectEdgeCommand(Edge edge, Node source, Node target) {
		return new ReconnectEdgeCommand(edge, source, target);
	}

	@Override
	public Command getCommand(EditPart editPart, Request request) {
		if (RequestConstants.REQ_DELETE.equals(request.getType())) {
			return new DeleteEdgeCommand((Edge) editPart.getModel());
		}

		return null;
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public void performRequest(EditPart editPart, Request reqeuest) {
		Edge model = (Edge) editPart.getModel();

		if (REQ_OPEN.equals(reqeuest.getType()) && model.getDescriptor().hasCustomName()) {
			GraphEditDomain editDomain = (GraphEditDomain) editPart.getViewer().getEditDomain();
			if (editDomain.isDirectEditingEnabled()) {
				new EditConditionAction((EdgeEditPart) editPart).run();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChanged(EditPart editPart, PropertyChangeEvent event) {
		String property = event.getPropertyName();

		if (ModelerConstants.PROPERTY_BACKGROUND_COLOR.equals(property)) {
			Edge element = (Edge) editPart.getModel();
			RGB rgb = (RGB) element.getProperty(property);
			Color color = SWTResourceManager.getColor(0, 0, 0);
			if (rgb != null) {
				color = SWTResourceManager.getColor(rgb);
			}
			IFigure figure = ((AbstractGraphicalEditPart) editPart).getFigure();
			figure.setForegroundColor(color);
			figure.setBackgroundColor(color);
			List<IFigure> children = figure.getChildren();
			for (IFigure childFigure : children) {
				childFigure.setForegroundColor(color);
				childFigure.setBackgroundColor(color);
			}
			editPart.setSelected(EditPart.SELECTED_NONE);
		}
	}
}
