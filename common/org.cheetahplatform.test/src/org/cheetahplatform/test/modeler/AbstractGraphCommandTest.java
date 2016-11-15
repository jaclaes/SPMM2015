/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DESCRIPTOR;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.NEW_NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.SOURCE_NODE_DESCRIPTOR;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.SOURCE_NODE_NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.TARGET_NODE_DESCRIPTOR;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.TARGET_NODE_NAME;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.MoveNodeCommand;
import org.cheetahplatform.modeler.graph.command.RenameCommand;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class AbstractGraphCommandTest {

	public static EdgeDescriptor EDGE;
	public static NodeDescriptor NODE;
	public static NodeDescriptor UNNAMED_NODE;
	public static EdgeDescriptor EDGE_WITH_NAME;

	static {
		EDGE = new EdgeDescriptor("", "Edge", "edge_id") {
			@Override
			public boolean hasCustomName() {
				return false;
			}
		};
		EDGE_WITH_NAME = new EdgeDescriptor("", "Edge", "edge_id_2");
		NODE = new DummyNodeDescriptor("Node", "node_id", true);
		UNNAMED_NODE = new DummyNodeDescriptor("Unnamed Node", "unnamed_node_id", false);

		EditorRegistry.registerDescriptor(EDGE);
		EditorRegistry.registerDescriptor(EDGE_WITH_NAME);
		EditorRegistry.registerDescriptor(NODE);
		EditorRegistry.registerDescriptor(UNNAMED_NODE);
	}

	@Test
	public void createEdgeLabelNodeToNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE.getId());
		entry.setAttribute(NAME, "edge name");
		entry.setAttribute(SOURCE_NODE_NAME, "source");
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, NODE.getId());
		entry.setAttribute(TARGET_NODE_NAME, "target");
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, NODE.getId());

		String name = AbstractGraphCommand.getCreateEdgeLabel(entry);
		String expected = "Create Edge from Node 'source' to Node 'target'";
		assertEquals(expected, name);
	}

	@Test
	public void createEdgeLabelNodeToUnnamedNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE.getId());
		entry.setAttribute(NAME, "edge name");
		entry.setAttribute(SOURCE_NODE_NAME, "source");
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, NODE.getId());
		entry.setAttribute(TARGET_NODE_NAME, "target");
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, UNNAMED_NODE.getId());

		String name = AbstractGraphCommand.getCreateEdgeLabel(entry);
		String expected = "Create Edge from Node 'source' to Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void createEdgeWithEdgeWithName() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Edge edge = EDGE_WITH_NAME.createModel(graph);
		Node node = NODE.createModel(graph);
		edge.setSource(node);
		edge.setTarget(node);
		AbstractGraphCommand command = EDGE_WITH_NAME.createCreateEdgeCommand(graph, edge, node, node, null);
		command.execute();

		AuditTrailEntry entry = listener.getEntries().get(0);
		String element = entry.getWorkflowModelElement();
		assertEquals("Edge from Node to Node", element);
	}

	@Test
	public void createNodeLabel() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, NODE.getId());
		entry.setAttribute(NAME, "node name");

		String name = AbstractGraphCommand.getCreateNodeLabel(entry);
		String expected = "Create Node 'node name'";
		assertEquals(expected, name);
	}

	@Test
	public void createNodeLabelWithUnknownLabel() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, UNNAMED_NODE.getId());
		entry.setAttribute(NAME, "node name");

		String name = AbstractGraphCommand.getCreateNodeLabel(entry);
		String expected = "Create Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void deleteEdgeLabelNirvanaToNirvana() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE.getId());
		entry.setAttribute(NAME, "edge name");

		String name = AbstractGraphCommand.getDeleteEdgeLabel(entry);
		String expected = "Delete Edge";
		assertEquals(expected, name);
	}

	@Test
	public void deleteEdgeLabelNirvanaToNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE.getId());
		entry.setAttribute(NAME, "edge name");
		entry.setAttribute(TARGET_NODE_NAME, "target");
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, UNNAMED_NODE.getId());

		String name = AbstractGraphCommand.getDeleteEdgeLabel(entry);
		String expected = "Delete incoming Edge to Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void deleteEdgeLabelNodeToNirvana() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE.getId());
		entry.setAttribute(NAME, "edge name");
		entry.setAttribute(SOURCE_NODE_NAME, "source");
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, NODE.getId());

		String name = AbstractGraphCommand.getDeleteEdgeLabel(entry);
		String expected = "Delete outgoing Edge from Node 'source'";
		assertEquals(expected, name);
	}

	@Test
	public void deleteEdgeLabelNodeToNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE.getId());
		entry.setAttribute(NAME, "edge name");
		entry.setAttribute(SOURCE_NODE_NAME, "source");
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, NODE.getId());
		entry.setAttribute(TARGET_NODE_NAME, "target");
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, UNNAMED_NODE.getId());

		String name = AbstractGraphCommand.getDeleteEdgeLabel(entry);
		String expected = "Delete Edge from Node 'source' to Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void deleteEdgeWithNameButEmptyNameLabelNodeToNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE_WITH_NAME.getId());
		entry.setAttribute(NAME, EDGE_WITH_NAME.getName());
		entry.setAttribute(SOURCE_NODE_NAME, "source");
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, NODE.getId());
		entry.setAttribute(TARGET_NODE_NAME, "target");
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, UNNAMED_NODE.getId());

		String name = AbstractGraphCommand.getDeleteEdgeLabel(entry);
		String expected = "Delete Edge from Node 'source' to Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void deleteEdgeWithNameLabelNodeToNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, EDGE_WITH_NAME.getId());
		entry.setAttribute(NAME, "edge name");
		entry.setAttribute(SOURCE_NODE_NAME, "source");
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, NODE.getId());
		entry.setAttribute(TARGET_NODE_NAME, "target");
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, UNNAMED_NODE.getId());

		String name = AbstractGraphCommand.getDeleteEdgeLabel(entry);
		String expected = "Delete Edge 'edge name' from Node 'source' to Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void deleteNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, NODE.getId());
		entry.setAttribute(NAME, "node name");

		String name = AbstractGraphCommand.getDeleteNodeLabel(entry);
		String expected = "Delete Node 'node name'";
		assertEquals(expected, name);
	}

	@Test
	public void deleteUnnamedNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, UNNAMED_NODE.getId());
		entry.setAttribute(NAME, "node name");

		String name = AbstractGraphCommand.getDeleteNodeLabel(entry);
		String expected = "Delete Unnamed Node";
		assertEquals(expected, name);
	}

	@Test
	public void moveWithName() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		node.setName("A");
		MoveNodeCommand command = new MoveNodeCommand(node, new Point(0, 0));
		command.execute();

		AuditTrailEntry entry = listener.getEntries().get(0);
		String element = entry.getWorkflowModelElement();
		assertEquals("Node 'A'", element);
	}

	@Test
	public void moveWithoutName() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		MoveNodeCommand command = new MoveNodeCommand(node, new Point(0, 0));
		command.execute();

		AuditTrailEntry entry = listener.getEntries().get(0);
		String element = entry.getWorkflowModelElement();
		assertEquals("Node", element);
	}

	@Test
	public void name() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Edge edge = EDGE.createModel(graph);
		Node node = NODE.createModel(graph);
		AbstractGraphCommand command = EDGE.createCreateEdgeCommand(graph, edge, node, node, null);
		command.execute();

		AuditTrailEntry entry = listener.getEntries().get(0);
		String element = entry.getWorkflowModelElement();
		assertEquals("Edge from Node to Node", element);
	}

	@Test
	public void rename() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Edge edge = EDGE_WITH_NAME.createModel(graph);
		Node node = NODE.createModel(graph);
		edge.setSource(node);
		edge.setTarget(node);
		graph.addEdge(edge);
		RenameCommand command = new RenameCommand(edge, "x>0");
		command.execute();

		AuditTrailEntry entry = listener.getEntries().get(0);
		String element = entry.getWorkflowModelElement();
		assertEquals("Edge from Node to Node", element);

		command = new RenameCommand(edge, "");
		command.execute();

		entry = listener.getEntries().get(1);
		element = entry.getWorkflowModelElement();
		assertEquals("Edge 'x>0' from Node to Node", element);
	}

	@Test
	public void renameCommand() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		TestLogListener listener = new TestLogListener();
		graph.addLogListener(listener);
		Node node = NODE.createModel(graph);
		node.setName("A");
		RenameCommand command = new RenameCommand(node, "B");
		command.execute();

		AuditTrailEntry entry = listener.getEntries().get(0);
		String element = entry.getWorkflowModelElement();
		assertEquals("Node 'A'", element);
	}

	@Test
	public void renameNode() throws Exception {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAttribute(DESCRIPTOR, NODE.getId());
		entry.setAttribute(NAME, "node name");
		entry.setAttribute(NEW_NAME, "new node name");

		String name = AbstractGraphCommand.getRenameLabel(entry);
		String expected = "Rename Node 'node name' to 'new node name'";
		assertEquals(expected, name);
	}
}
