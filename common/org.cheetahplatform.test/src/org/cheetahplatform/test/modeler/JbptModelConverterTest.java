/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.bpmn.AndGatewayDescriptor;
import org.cheetahplatform.modeler.bpmn.EndEventDescriptor;
import org.cheetahplatform.modeler.bpmn.StartEventDescriptor;
import org.cheetahplatform.modeler.bpmn.XorGatewayDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.JbptModelConverter;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.jbpt.pm.Activity;
import org.jbpt.pm.ControlFlow;
import org.jbpt.pm.FlowNode;
import org.jbpt.pm.ProcessModel;
import org.junit.Before;
import org.junit.Test;

public class JbptModelConverterTest {

	private Graph graph;

	public void addEdge(Node a1, Node xor) {
		Edge edge = new Edge(graph, EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN).get(0));
		edge.setSource(a1);
		edge.setTarget(xor);
		graph.addEdge(edge);
	}

	public Node createActivity(String name) {
		Node node = new Node(graph, new ActivityDescriptor() {
			@Override
			public Point getInitialSize() {
				return new Point(100, 100);
			}
		});
		node.setName(name);
		return node;
	}

	@Test
	public void endEventGraph() {
		Node end = new Node(graph, new EndEventDescriptor() {
			@Override
			public Point getInitialSize() {
				return new Point(100, 100);
			}
		});
		graph.addNode(end);
		Node a1 = createActivity("name");
		graph.addNode(a1);
		addEdge(end, a1);

		ProcessModel model = new JbptModelConverter(graph).convert();

		Collection<Activity> activities = model.getActivities();
		assertEquals(1, activities.size());

		Collection<FlowNode> flowNodes = model.getFlowNodes();
		assertEquals(2, flowNodes.size());
	}

	@Test
	public void graphWithAnd() {
		Node a1 = createActivity("1");
		graph.addNode(a1);
		Node a2 = createActivity("2.1");
		graph.addNode(a2);
		Node a3 = createActivity("2.2");
		graph.addNode(a3);
		Node xor = new Node(graph, new XorGatewayDescriptor() {
			@Override
			public Point getInitialSize() {
				return new Point(10, 10);
			}
		});
		graph.addNode(xor);
		addEdge(a1, xor);
		addEdge(xor, a2);
		addEdge(xor, a3);

		ProcessModel model = new JbptModelConverter(graph).convert();
		Collection<Activity> activities = model.getActivities();
		assertEquals(3, activities.size());

		Collection<FlowNode> flowNodes = model.getFlowNodes();
		assertEquals(4, flowNodes.size());

		Collection<ControlFlow<FlowNode>> controlFlow = model.getControlFlow();
		assertEquals(3, controlFlow.size());
	}

	@Test
	public void graphWithXor() {
		Node a1 = createActivity("1");
		graph.addNode(a1);
		Node a2 = createActivity("2.1");
		graph.addNode(a2);
		Node a3 = createActivity("2.2");
		graph.addNode(a3);
		Node xor = new Node(graph, new AndGatewayDescriptor() {
			@Override
			public Point getInitialSize() {
				return new Point(10, 10);
			}
		});
		graph.addNode(xor);
		addEdge(a1, xor);
		addEdge(xor, a2);
		addEdge(xor, a3);

		ProcessModel model = new JbptModelConverter(graph).convert();
		Collection<Activity> activities = model.getActivities();
		assertEquals(3, activities.size());

		Collection<FlowNode> flowNodes = model.getFlowNodes();
		assertEquals(4, flowNodes.size());

		Collection<ControlFlow<FlowNode>> controlFlow = model.getControlFlow();
		assertEquals(3, controlFlow.size());
	}

	@Test
	public void oneActivityGraph() {
		graph.addNode(createActivity("name"));

		ProcessModel model = new JbptModelConverter(graph).convert();

		Collection<Activity> activities = model.getActivities();
		assertEquals(1, activities.size());
		Activity activity = activities.iterator().next();
		assertEquals("name", activity.getName());
	}

	@Before
	public void setUp() {
		graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
	}

	@Test
	public void startEventGraph() {
		Node start = new Node(graph, new StartEventDescriptor() {
			@Override
			public Point getInitialSize() {
				return new Point(100, 100);
			}
		});
		graph.addNode(start);
		Node a1 = createActivity("name");
		graph.addNode(a1);
		addEdge(start, a1);

		ProcessModel model = new JbptModelConverter(graph).convert();

		Collection<Activity> activities = model.getActivities();
		assertEquals(1, activities.size());

		Collection<FlowNode> flowNodes = model.getFlowNodes();
		assertEquals(2, flowNodes.size());
	}
}
