package org.cheetahplatform.modeler.graph.descriptor;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.handles.MoveHandle;

public interface INodeDescriptor extends IGraphElementDescriptor {

	/**
	 * Resizing behavior in general is controlled by {@link IConfiguration#NODES_RESIZABLE}. However, some nodes might not be resizable at
	 * all.
	 * 
	 * @return <code>true</code> if the node supports resizing, <code>false</code> otherwise
	 */
	boolean allowsResizing();

	/**
	 * Create additional selection handles, if necessary.
	 * 
	 * @param nodeEditPart
	 *            the editpart for which the handles should be created
	 * 
	 * @return the handles
	 */
	List<MoveHandle> createHandles(NodeEditPart nodeEditPart);

	@Override
	Node createModel(Graph graph);;

	@Override
	Node createModel(Graph graph, long id, AuditTrailEntry entry);

	/**
	 * Create a selection handle for the given edit part.
	 * 
	 * @param editPart
	 *            the edit part
	 * @return the selection handle
	 */
	MoveHandle createSelectionHandle(NodeEditPart editPart);

	Object getAdapter(Node node, Class<?> key);

	/**
	 * Determine the connection anchor for the given edit part.
	 * 
	 * @param editPart
	 *            the edit part
	 * @return the connection anchor
	 */
	ConnectionAnchor getConnectionAnchor(NodeEditPart editPart);

	/**
	 * Determine the initial size of the node.
	 * 
	 * @return the initial size
	 */
	Point getInitialSize();

	/**
	 * Determine the target edit part for given request. This method is called whenever the standard implementation of the
	 * {@link NodeEditPart} cannot resolve the target edit part.
	 * 
	 * @param request
	 *            the request
	 * @param editPart
	 *            the edit part for which the request has been issued
	 * @return the target edit part, <code>null</code> if it could not be resolved
	 */
	EditPart getTargetEditPart(EditPart editPart, Request request);

	/**
	 * Perform direct edit on the given edit part described by this descriptor.
	 * 
	 * @param editPart
	 *            the edit part
	 */
	void performDirectEdit(NodeEditPart editPart);

	/**
	 * Updates the name of the figure. When implementing, also consider {@link #createFigure(GraphElement)}.
	 * 
	 * @param figure
	 *            the figure - only figures retrieved from {@link #createFigure(GraphElement)} are passed
	 * @param name
	 *            the name to set
	 */
	void updateName(IFigure figure, String name);

}
