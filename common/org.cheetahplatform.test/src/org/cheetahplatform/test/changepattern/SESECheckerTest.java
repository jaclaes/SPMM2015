/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.changepattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.cheetahplatform.modeler.changepattern.model.SESEChecker;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.literatemodeling.DummyEdge;
import org.cheetahplatform.test.literatemodeling.DummyNode;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class SESECheckerTest {
	private void createEdge(Graph graph, DummyNode node1, DummyNode node2) {
		DummyEdge dummyEdge = new DummyEdge();
		dummyEdge.setSource(node1);
		dummyEdge.setTarget(node2);
		graph.addEdge(dummyEdge);
	}

	private Graph createGraph() {
		return new Graph(new ArrayList<IGraphElementDescriptor>());
	}

	private DummyNode createNode(Graph graph) {
		DummyNode node1 = new DummyNode();
		graph.addNode(node1);
		node1.setParent(graph);
		return node1;
	}

	@Test
	public void endNode() {
		Graph graph = createGraph();

		DummyNode node0 = createNode(graph);
		DummyNode node1 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node1);

		createEdge(graph, node0, node1);

		assertFalse(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void fragmentWithinParallelStructure() {
		Graph graph = createGraph();

		DummyNode node0 = createNode(graph);
		DummyNode node1 = createNode(graph);
		DummyNode node21a = createNode(graph);
		DummyNode node21b = createNode(graph);
		DummyNode node22 = createNode(graph);
		DummyNode node3 = createNode(graph);
		DummyNode node4 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node21a);
		nodes.add(node21b);

		createEdge(graph, node0, node1);
		createEdge(graph, node1, node21a);
		createEdge(graph, node21a, node21b);
		createEdge(graph, node1, node22);
		createEdge(graph, node21b, node3);
		createEdge(graph, node22, node3);
		createEdge(graph, node3, node4);

		assertTrue(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void fragmentWithParallelisation() {
		Graph graph = createGraph();

		DummyNode node0 = createNode(graph);
		DummyNode node1 = createNode(graph);
		DummyNode node21 = createNode(graph);
		DummyNode node22 = createNode(graph);
		DummyNode node3 = createNode(graph);
		DummyNode node4 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node1);
		nodes.add(node21);
		nodes.add(node22);
		nodes.add(node3);

		createEdge(graph, node0, node1);
		createEdge(graph, node1, node21);
		createEdge(graph, node1, node22);
		createEdge(graph, node21, node3);
		createEdge(graph, node22, node3);
		createEdge(graph, node3, node4);

		assertTrue(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void illegalFragmentWithParallelisation() {
		Graph graph = createGraph();

		DummyNode node0 = createNode(graph);
		DummyNode node1 = createNode(graph);
		DummyNode node21 = createNode(graph);
		DummyNode node22 = createNode(graph);
		DummyNode node3 = createNode(graph);
		DummyNode node4 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node21);
		nodes.add(node22);
		nodes.add(node3);

		createEdge(graph, node0, node1);
		createEdge(graph, node1, node21);
		createEdge(graph, node1, node22);
		createEdge(graph, node21, node3);
		createEdge(graph, node22, node3);
		createEdge(graph, node3, node4);

		assertFalse(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void illegalFragmentWithParallelisation2() {
		Graph graph = createGraph();

		DummyNode node0 = createNode(graph);
		DummyNode node1 = createNode(graph);
		DummyNode node21 = createNode(graph);
		DummyNode node22 = createNode(graph);
		DummyNode node3 = createNode(graph);
		DummyNode node4 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node1);
		nodes.add(node22);
		nodes.add(node3);

		createEdge(graph, node0, node1);
		createEdge(graph, node1, node21);
		createEdge(graph, node1, node22);
		createEdge(graph, node21, node3);
		createEdge(graph, node22, node3);
		createEdge(graph, node3, node4);

		assertFalse(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void illegalSequence() {
		Graph graph = createGraph();

		DummyNode node1 = createNode(graph);
		DummyNode node2 = createNode(graph);
		DummyNode node3 = createNode(graph);
		DummyNode node4 = createNode(graph);
		DummyNode node5 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node2);
		nodes.add(node4);

		createEdge(graph, node1, node2);
		createEdge(graph, node2, node3);
		createEdge(graph, node3, node4);
		createEdge(graph, node4, node5);

		assertFalse(new SESEChecker(nodes).isSESEFragment());
	}

	@Test(expected = AssertionFailedException.class)
	public void nullList() {
		new SESEChecker(null).isSESEFragment();
	}

	@Test
	public void selectedSequence() {
		Graph graph = createGraph();

		DummyNode node1 = createNode(graph);
		DummyNode node2 = createNode(graph);
		DummyNode node3 = createNode(graph);
		DummyNode node4 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node2);
		nodes.add(node3);

		createEdge(graph, node1, node2);
		createEdge(graph, node2, node3);
		createEdge(graph, node3, node4);

		assertTrue(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void singleNode() {
		Graph graph = createGraph();

		DummyNode node1 = createNode(graph);
		DummyNode node2 = createNode(graph);
		DummyNode node3 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node2);

		createEdge(graph, node1, node2);
		createEdge(graph, node2, node3);

		assertTrue(new SESEChecker(nodes).isSESEFragment());
	}

	@Test
	public void startNode() {
		Graph graph = createGraph();

		DummyNode node0 = createNode(graph);
		DummyNode node1 = createNode(graph);

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(node0);

		createEdge(graph, node0, node1);

		assertFalse(new SESEChecker(nodes).isSESEFragment());
	}
}
