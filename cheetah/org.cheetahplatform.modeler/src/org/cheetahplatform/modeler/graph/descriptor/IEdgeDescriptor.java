package org.cheetahplatform.modeler.graph.descriptor;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;

public interface IEdgeDescriptor extends IGraphElementDescriptor {
	/**
	 * Create a command for creating an edge of this type.
	 * 
	 * @param graph
	 *            the graph in which the edge should be created
	 * @param entry
	 *            the entry describing the edge to be created
	 * @return the command for creating the edge
	 */
	AbstractGraphCommand createCreateEdgeCommand(Graph graph, AuditTrailEntry entry);

	/**
	 * Create a command for creating this the type of edge specified by this descriptor.
	 * 
	 * @param graph
	 *            the graph to which the edge should be added
	 * @param edge
	 *            the edge to be added
	 * @param source
	 *            the edges's source
	 * @param target
	 *            the edge's target
	 * @param name
	 *            the edge's name
	 * @return the corresponding command, <code>null</code> if the given parameters are invalid for creating the edge
	 */
	AbstractGraphCommand createCreateEdgeCommand(Graph graph, Edge edge, Node source, Node target, String name);

	@Override
	Edge createModel(Graph graph);

	@Override
	Edge createModel(Graph graph, long id, AuditTrailEntry entry);

	/**
	 * Create a command for reconnecting the edge to given source and target.
	 * 
	 * @param edge
	 *            the edge to be reconnected
	 * @param source
	 *            the new source
	 * @param target
	 *            the new target
	 * @return the command
	 */
	AbstractGraphCommand createReconnectEdgeCommand(Edge edge, Node source, Node target);

	/**
	 * Perform the request.
	 * 
	 * @param editPart
	 *            the edit part on which the request should be performed
	 * @param reqeuest
	 *            the request to be performed
	 */
	void performRequest(EditPart editPart, Request reqeuest);

}
