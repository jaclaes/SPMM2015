/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.junit.BeforeClass;

public class AbstractBPMNTest {
	public static IGraphElementDescriptor START_EVENT_DESCRIPTOR;
	public static IGraphElementDescriptor END_EVENT_DESCRIPTOR;
	public static IGraphElementDescriptor SEQUENCE_FLOW_DESCRIPTOR;
	public static IGraphElementDescriptor ACTIVITY_DESCRIPTOR;
	public static IGraphElementDescriptor XOR_DESCRIPTOR;
	public static IGraphElementDescriptor AND_DESCRIPTOR;

	@BeforeClass
	public static void beforeClass() {
		CheetahPlatformConfigurator configuration = CheetahPlatformConfigurator.getInstance();
		configuration.set(IConfiguration.INITIAL_ACTIVITIY_SIZE, new Point(85, 63));
		configuration.set(IConfiguration.INITIAL_GATEWAY_SIZE, new Point(42, 42));
		configuration.set(IConfiguration.INITIAL_EVENT_SIZE, new Point(31, 31));

		START_EVENT_DESCRIPTOR = EditorRegistry.getDescriptor(EditorRegistry.BPMN_START_EVENT);
		END_EVENT_DESCRIPTOR = EditorRegistry.getDescriptor(EditorRegistry.BPMN_END_EVENT);
		SEQUENCE_FLOW_DESCRIPTOR = EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW);
		ACTIVITY_DESCRIPTOR = EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY);
		XOR_DESCRIPTOR = EditorRegistry.getDescriptor(EditorRegistry.BPMN_XOR_GATEWAY);
		AND_DESCRIPTOR = EditorRegistry.getDescriptor(EditorRegistry.BPMN_AND_GATEWAY);
	}

	public Node createActivity(Graph graph) {
		return createActivity(graph, "");
	}

	public Node createActivity(Graph graph, String name) {
		Node activity = (Node) ACTIVITY_DESCRIPTOR.createModel(graph);
		graph.addNode(activity);
		activity.setName(name);
		return activity;
	}

	public Node createAndGateway(Graph graph) {
		Node andGateway = (Node) AND_DESCRIPTOR.createModel(graph);
		graph.addNode(andGateway);
		return andGateway;
	}

	public Node createEndEvent(Graph graph) {
		Node endEvent = (Node) END_EVENT_DESCRIPTOR.createModel(graph);
		graph.addNode(endEvent);
		return endEvent;
	}

	public Edge createSequenceFlow(Graph graph, Node source, Node target) {
		Edge connection = (Edge) SEQUENCE_FLOW_DESCRIPTOR.createModel(graph);
		graph.addEdge(connection);
		connection.setSource(source);
		connection.setTarget(target);

		return connection;
	}

	public Node createStartEvent(Graph graph) {
		Node startEvent = (Node) START_EVENT_DESCRIPTOR.createModel(graph);
		graph.addNode(startEvent);
		return startEvent;
	}

	public Node createXorGateway(Graph graph) {
		Node xorGateway = (Node) XOR_DESCRIPTOR.createModel(graph);
		graph.addNode(xorGateway);
		return xorGateway;
	}
}
