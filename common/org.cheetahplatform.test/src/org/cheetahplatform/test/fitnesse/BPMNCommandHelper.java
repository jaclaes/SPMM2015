/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CreateNodeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class BPMNCommandHelper {
	private static Graph graph;

	public static boolean containsElement(long id) {
		GraphElement element = graph.getGraphElement(id);
		return element != null;
	}

	public static void createActivity(long id, String name, Point point) {
		ActivityDescriptor activityDescriptor = new ActivityDescriptor();
		CreateNodeCommand command = new CreateNodeCommand(graph, activityDescriptor.createModel(graph, id, null), point);
		command.setName(name);
		command.execute();
		Node node = command.getNode();
		Assert.isNotNull(node);
	}

	public static void createEdge(long id, String name, long fromId, long toId) {
		EdgeDescriptor edgeDescriptor = new EdgeDescriptor(null, name, EditorRegistry.BPMN_SEQUENCE_FLOW);
		Edge edge = edgeDescriptor.createModel(graph, id, null);
		Node source = (Node) getElement(fromId);
		Node target = (Node) getElement(toId);
		AbstractGraphCommand command = edgeDescriptor.createCreateEdgeCommand(graph, edge, source, target, name);
		command.execute();
	}

	public static void deleteEdge(long id) {
		GraphElement element = getElement(id);

		DeleteEdgeCommand deleteNodeCommand = new DeleteEdgeCommand((Edge) element);
		deleteNodeCommand.execute();
	}

	public static void deleteNode(long id) {
		GraphElement element = getElement(id);

		DeleteNodeCommand deleteNodeCommand = new DeleteNodeCommand((Node) element);
		deleteNodeCommand.execute();
	}

	public static void executeAuditTrailEntry(AuditTrailEntry entry) {
		AbstractGraphCommand command = AbstractGraphCommand.createCommand(entry, graph);
		command.execute();
	}

	public static Point getCoordinate(long id) {
		GraphElement element = getElement(id);
		return ((Node) element).getBounds().getLocation();
	}

	public static int getEdgeCount() {
		return graph.getEdges().size();
	}

	private static GraphElement getElement(long id) {
		GraphElement element = graph.getGraphElement(id);
		Assert.isNotNull(element);
		return element;
	}

	public static String getName(long id) {
		GraphElement element = getElement(id);
		return element.getName();
	}

	public static int getNodeCount() {
		return graph.getChildren().size();
	}

	public static long getSourceNodeId(long id) {
		Edge element = (Edge) getElement(id);
		return element.getSource().getId();
	}

	public static long getTargetNodeId(long id) {
		Edge element = (Edge) getElement(id);
		return element.getTarget().getId();
	}

	public static void initialize() {
		graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
	}

	public static void moveNode(long id, Point moveDelta) {
		GraphElement element = getElement(id);
		MoveNodeCommand command = new MoveNodeCommand((Node) element, moveDelta);
		command.execute();
	}

	public static void reconnectEdge(long id, long fromId, long toId) {
		Edge edge = (Edge) getElement(id);
		Node source = (Node) getElement(fromId);
		Node target = (Node) getElement(toId);
		Command command = edge.getDescriptor().createReconnectEdgeCommand(edge, source, target);
		command.execute();
	}

	public static void renameNode(long id, String name) {
		GraphElement element = getElement(id);
		RenameCommand command = new RenameCommand(element, name);
		command.execute();
	}
}
