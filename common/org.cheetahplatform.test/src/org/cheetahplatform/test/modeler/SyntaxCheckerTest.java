/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.ISyntaxError;
import org.cheetahplatform.modeler.graph.SyntaxCheckResponse;
import org.cheetahplatform.modeler.graph.SyntaxChecker;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.Test;

public class SyntaxCheckerTest {
	@Test
	public void andWithNoIncomingAndTwoOutgoingEdges() throws Exception {
		Graph graph = initializeGraph();
		Node andSplit = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity2);
		Node andJoin = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, activity1, andJoin);
		createEdge(graph, activity2, andJoin);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andJoin, activity3);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void andWithNoOutgoingAndTwoIncomingEdges() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node andSplit = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, startNode, andSplit);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity2);
		Node andJoin = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, activity1, andJoin);
		createEdge(graph, activity2, andJoin);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void andWithTwoEdges() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node andGateway = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, startNode, andGateway);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, andGateway, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	protected void createEdge(Graph graph, Node startNode, Node activityNode) {
		Edge edge1 = new Edge(graph, EditorRegistry.getDescriptor(EditorRegistry.BPMN));
		edge1.setSource(startNode);
		edge1.setTarget(activityNode);
		graph.addEdge(edge1);
	}

	protected Node createNode(Graph graph, String type) {
		Node startNode = new Node(graph, (INodeDescriptor) EditorRegistry.getDescriptor(type));
		graph.addNode(startNode);
		return startNode;
	}

	@Test
	public void endNodeWithMultipleIncomingEdges() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node andSplit = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, startNode, andSplit);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity2);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activity1, endNode);
		createEdge(graph, activity2, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(0, syntaxErrors.size());
	}

	@Test
	public void illegalEndNode() throws Exception {
		Graph graph = initializeGraph();
		Node startNode1 = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, startNode1, activity1);
		createEdge(graph, activity1, endNode);
		createEdge(graph, endNode, activity2);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void illegalStartNode() throws Exception {
		Graph graph = initializeGraph();
		Node startNode1 = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node startNode2 = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		createEdge(graph, startNode1, activity1);
		createEdge(graph, activity1, startNode2);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void implicitAndSplit() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node andJoin = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, startNode, activity1);
		createEdge(graph, activity1, activity2);
		createEdge(graph, activity1, activity3);
		createEdge(graph, activity2, andJoin);
		createEdge(graph, activity3, andJoin);
		createEdge(graph, andJoin, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(0, syntaxErrors.size());
	}

	@Test
	public void implicitXorJoin() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node xorSplit = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, startNode, xorSplit);
		createEdge(graph, xorSplit, activity1);
		createEdge(graph, xorSplit, activity2);
		createEdge(graph, activity1, activity3);
		createEdge(graph, activity2, activity3);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(0, syntaxErrors.size());
	}

	protected Graph initializeGraph() {
		CheetahPlatformConfigurator configuration = CheetahPlatformConfigurator.getInstance();
		configuration.set(IConfiguration.INITIAL_ACTIVITIY_SIZE, new Point(85, 63));
		configuration.set(IConfiguration.INITIAL_GATEWAY_SIZE, new Point(42, 42));
		configuration.set(IConfiguration.INITIAL_EVENT_SIZE, new Point(31, 31));

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		return graph;
	}

	@Test
	public void invokeServiceCorrectGraph() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activityNode = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, startNode, activityNode);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activityNode, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertTrue(syntaxErrors.isEmpty());
	}

	@Test
	public void invokeServiceWrongJoin() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node xorSplitNode = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, startNode, xorSplitNode);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity2);
		Node andJoin = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, activity1, andJoin);
		createEdge(graph, activity2, andJoin);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, andJoin, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void lackOfSyncAfterimplicitAndSplit() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node xorJoin = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, startNode, activity1);
		createEdge(graph, activity1, activity2);
		createEdge(graph, activity1, activity3);
		createEdge(graph, activity2, xorJoin);
		createEdge(graph, activity3, xorJoin);
		createEdge(graph, xorJoin, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void lackOfSynchronizationBeforeImplicitXorJoin() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node andSplit = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, startNode, andSplit);
		createEdge(graph, andSplit, activity1);
		createEdge(graph, andSplit, activity2);
		createEdge(graph, activity1, activity3);
		createEdge(graph, activity2, activity3);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void missingAndJoin() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node andSplit = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, startNode, andSplit);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity2);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, activity1, activity3);
		createEdge(graph, activity2, activity3);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void singleActivity() throws Exception {
		Graph graph = initializeGraph();
		createNode(graph, EditorRegistry.BPMN_ACTIVITY);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void singleEndNode() throws Exception {
		Graph graph = initializeGraph();
		createNode(graph, EditorRegistry.BPMN_END_EVENT);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void singleStartNode() throws Exception {
		Graph graph = initializeGraph();
		createNode(graph, EditorRegistry.BPMN_START_EVENT);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void unconnectedAndGraph() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node andSplit = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, startNode, andSplit);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andSplit, activity2);
		Node andJoin = createNode(graph, EditorRegistry.BPMN_AND_GATEWAY);
		createEdge(graph, activity2, andJoin);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, andJoin, activity3);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void unconnectedEdge() throws Exception {
		Graph graph = initializeGraph();
		Node startNode1 = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		createEdge(graph, startNode1, null);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void unconnectedSequence() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, startNode, activity1);
		createEdge(graph, activity2, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void unconnectedXorGraph() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node xorSplitNode = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, startNode, xorSplitNode);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity2);
		Node xorJoin = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, activity2, xorJoin);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorJoin, activity3);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void weirdConstruct() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, startNode, activity1);
		createEdge(graph, activity1, activity2);
		createEdge(graph, activity2, activity1);
		createEdge(graph, activity1, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}

	@Test
	public void xorWithNoIncomingAndTwoOutgoingEdges() throws Exception {
		Graph graph = initializeGraph();
		Node xorSplitNode = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity2);
		Node xorJoin = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, activity1, xorJoin);
		createEdge(graph, activity2, xorJoin);
		Node activity3 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorJoin, activity3);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, activity3, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void xorWithNoOutgoingAndTwoIncomingEdges() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node xorSplitNode = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, startNode, xorSplitNode);
		Node activity1 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity1);
		Node activity2 = createNode(graph, EditorRegistry.BPMN_ACTIVITY);
		createEdge(graph, xorSplitNode, activity2);
		Node xorJoin = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, activity1, xorJoin);
		createEdge(graph, activity2, xorJoin);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(2, syntaxErrors.size());
	}

	@Test
	public void xorWithTwoEdges() throws Exception {
		Graph graph = initializeGraph();
		Node startNode = createNode(graph, EditorRegistry.BPMN_START_EVENT);
		Node xorGateway = createNode(graph, EditorRegistry.BPMN_XOR_GATEWAY);
		createEdge(graph, startNode, xorGateway);
		Node endNode = createNode(graph, EditorRegistry.BPMN_END_EVENT);
		createEdge(graph, xorGateway, endNode);

		SyntaxChecker controlFlowAnalysis = new SyntaxChecker();
		SyntaxCheckResponse response = controlFlowAnalysis.invoke(graph);
		assertNull(response.getErrorMessage());
		List<ISyntaxError> syntaxErrors = response.getSyntaxErrors();
		assertEquals(1, syntaxErrors.size());
	}
}
