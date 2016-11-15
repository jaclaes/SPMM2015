package org.cheetahplatform.modeler.graph.descriptor;

import static org.eclipse.gef.RequestConstants.REQ_DELETE;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.CompoundCommandWithAttributes;
import org.cheetahplatform.modeler.generic.InvisibleHandle;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.command.ResizeNodeCommand;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.modeler.graph.policy.NodeDirectEditPolicy;
import org.cheetahplatform.modeler.graph.policy.NodeEditPolicy;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Default implementation describing node - subclasses may extend.
 * 
 * @author Stefan Zugal
 * 
 */
public abstract class NodeDescriptor extends GraphElementDescriptor implements INodeDescriptor {

	private static Command assembleDeleteNodeAndConnectedEdgesCommand(EditPart editPart) {
		Set<Edge> toDelete = new LinkedHashSet<Edge>();
		Node nodeToDelete = (Node) editPart.getModel();
		toDelete.addAll(nodeToDelete.getSourceConnections());
		toDelete.addAll(nodeToDelete.getTargetConnections());

		CompoundCommandWithAttributes command = new CompoundCommandWithAttributes();
		command.setAttribute(ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME, "delete node and connected edges");
		for (Edge edgeToDelete : toDelete) {
			command.add(new DeleteEdgeCommand(edgeToDelete));
		}

		command.add(new DeleteNodeCommand(nodeToDelete));
		return command;
	}

	public static Command getDeleteCommand(EditPart editPart) {
		if (!CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_DELETION_OF_NODES)) {
			return null;
		}
		if (!CheetahPlatformConfigurator.getBoolean(IConfiguration.DELETE_EDGES_WHEN_DELETING_NODE)) {
			return new DeleteNodeCommand((Node) editPart.getModel());
		}

		return assembleDeleteNodeAndConnectedEdgesCommand(editPart);
	}

	public NodeDescriptor(String imagePath, String name, String id) {
		super(imagePath, name, id);
	}

	@Override
	public boolean allowsResizing() {
		return true;
	}

	@Override
	public void createEditPolicies(EditPart editPart) {
		editPart.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new NodeEditPolicy());
		editPart.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NodeDirectEditPolicy());
	}

	@Override
	public List<MoveHandle> createHandles(NodeEditPart editPart) {
		return Collections.emptyList();
	}

	@Override
	public Node createModel(Graph graph) {
		return new Node(graph, this);
	}

	@Override
	public Node createModel(Graph graph, long id, AuditTrailEntry entry) {
		return new Node(graph, this, id);
	}

	@Override
	public MoveHandle createSelectionHandle(NodeEditPart editPart) {
		return new InvisibleHandle(editPart);
	}

	@Override
	public Object getAdapter(Node node, Class<?> key) {
		return null; // no adapters by default
	}

	@Override
	public Command getCommand(EditPart editPart, Request request) {
		if (REQ_DELETE.equals(request.getType())) {
			return getDeleteCommand(editPart);
		}
		if (RequestConstants.REQ_RESIZE.equals(request.getType())) {
			if (!CheetahPlatformConfigurator.getBoolean(IConfiguration.NODES_RESIZABLE)) {
				return null;
			}

			Node node = (Node) editPart.getModel();
			Dimension delta = ((ChangeBoundsRequest) request).getSizeDelta();
			Dimension newSize = node.getBounds().getSize().getCopy().expand(delta);

			return new ResizeNodeCommand(node.getGraph(), node, newSize);
		}

		return null;
	}

	@Override
	public ConnectionAnchor getConnectionAnchor(NodeEditPart editPart) {
		return new ChopboxAnchor(editPart.getFigure());
	}

	@Override
	public EditPart getTargetEditPart(EditPart editPart, Request request) {
		// no special target edit parts needed yet, subclasses may extend
		return null;
	}

	@Override
	public void performDirectEdit(final NodeEditPart editPart) {
		// disabled direct editing for the moment, if reactivating take into account that scrolling also moves the direct editing area.

		// CellEditorLocator locator = new NodeCellEditorLocator(editPart.getFigure());
		// DirectEditManager manager = new DirectEditManager(editPart, TextCellEditor.class, locator) {
		// @Override
		// protected void initCellEditor() {
		// Node node = (Node) editPart.getModel();
		// String name = "";
		// if (node.getName() != null) {
		// name = node.getName();
		// }
		//
		// getCellEditor().setValue(name);
		// }
		//
		// };
		//
		// manager.show();
	}

	@Override
	public void propertyChanged(EditPart editPart, PropertyChangeEvent event) {
		// nothing to handle
	}

}
