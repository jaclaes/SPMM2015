/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.Trace;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.TraceCalculator;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class TraceCalculatorTest extends AbstractBPMNTest {

	@Test
	public void simpleAndGateway() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		Node startEvent = (Node) START_EVENT_DESCRIPTOR.createModel(graph);
		Node endEvent = (Node) END_EVENT_DESCRIPTOR.createModel(graph);
		Node activity1 = (Node) ACTIVITY_DESCRIPTOR.createModel(graph);
		Node activity2 = (Node) ACTIVITY_DESCRIPTOR.createModel(graph);
		Node xorSplit = (Node) AND_DESCRIPTOR.createModel(graph);
		Node xorJoin = (Node) AND_DESCRIPTOR.createModel(graph);
		graph.addNode(startEvent);
		graph.addNode(endEvent);
		graph.addNode(activity1);
		graph.addNode(activity2);
		graph.addNode(xorSplit);
		graph.addNode(xorJoin);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		TraceCalculator calculator = new TraceCalculator(graph);
		List<Trace> traces = calculator.calculateTraces();
		assertEquals(2, traces.size());

		Trace trace1 = traces.get(0);
		assertEquals(5, trace1.size());
		assertEquals(startEvent, trace1.get(0));
		assertEquals(xorSplit, trace1.get(1));
		assertEquals(activity1, trace1.get(2));
		assertEquals(xorJoin, trace1.get(3));
		assertEquals(endEvent, trace1.get(4));

		Trace trace2 = traces.get(1);
		assertEquals(5, trace2.size());
		assertEquals(startEvent, trace2.get(0));
		assertEquals(xorSplit, trace2.get(1));
		assertEquals(activity2, trace2.get(2));
		assertEquals(xorJoin, trace2.get(3));
		assertEquals(endEvent, trace2.get(4));
	}

	@Test
	public void simpleXorGateway() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		Node startEvent = (Node) START_EVENT_DESCRIPTOR.createModel(graph);
		Node endEvent = (Node) END_EVENT_DESCRIPTOR.createModel(graph);
		Node activity1 = (Node) ACTIVITY_DESCRIPTOR.createModel(graph);
		Node activity2 = (Node) ACTIVITY_DESCRIPTOR.createModel(graph);
		Node xorSplit = (Node) XOR_DESCRIPTOR.createModel(graph);
		Node xorJoin = (Node) XOR_DESCRIPTOR.createModel(graph);
		graph.addNode(startEvent);
		graph.addNode(endEvent);
		graph.addNode(activity1);
		graph.addNode(activity2);
		graph.addNode(xorSplit);
		graph.addNode(xorJoin);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		TraceCalculator calculator = new TraceCalculator(graph);
		List<Trace> traces = calculator.calculateTraces();
		assertEquals(2, traces.size());

		Trace trace1 = traces.get(0);
		assertEquals(5, trace1.size());
		assertEquals(startEvent, trace1.get(0));
		assertEquals(xorSplit, trace1.get(1));
		assertEquals(activity1, trace1.get(2));
		assertEquals(xorJoin, trace1.get(3));
		assertEquals(endEvent, trace1.get(4));

		Trace trace2 = traces.get(1);
		assertEquals(5, trace2.size());
		assertEquals(startEvent, trace2.get(0));
		assertEquals(xorSplit, trace2.get(1));
		assertEquals(activity2, trace2.get(2));
		assertEquals(xorJoin, trace2.get(3));
		assertEquals(endEvent, trace2.get(4));
	}

	@Test
	public void singleStartEvent() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		Node startEvent = (Node) START_EVENT_DESCRIPTOR.createModel(graph);
		graph.addNode(startEvent);

		TraceCalculator calculator = new TraceCalculator(graph);
		List<Trace> traces = calculator.calculateTraces();
		assertEquals(1, traces.size());

		Trace trace = traces.get(0);
		assertEquals(1, trace.size());
		assertEquals(startEvent, trace.get(0));
	}

	@Test
	public void startEndEvent() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		Node startEvent = (Node) START_EVENT_DESCRIPTOR.createModel(graph);
		Node endEvent = (Node) END_EVENT_DESCRIPTOR.createModel(graph);
		Edge connection = (Edge) SEQUENCE_FLOW_DESCRIPTOR.createModel(graph);
		graph.addNode(startEvent);
		graph.addEdge(connection);
		connection.setSource(startEvent);
		connection.setTarget(endEvent);

		TraceCalculator calculator = new TraceCalculator(graph);
		List<Trace> traces = calculator.calculateTraces();
		assertEquals(1, traces.size());

		Trace trace = traces.get(0);
		assertEquals(2, trace.size());
		assertEquals(startEvent, trace.get(0));
		assertEquals(endEvent, trace.get(1));
	}

	@Test
	public void startEventActivityEndEvent() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		Node startEvent = (Node) START_EVENT_DESCRIPTOR.createModel(graph);
		Node endEvent = (Node) END_EVENT_DESCRIPTOR.createModel(graph);
		Edge connection1 = (Edge) SEQUENCE_FLOW_DESCRIPTOR.createModel(graph);
		Edge connection2 = (Edge) SEQUENCE_FLOW_DESCRIPTOR.createModel(graph);
		Node activity = (Node) ACTIVITY_DESCRIPTOR.createModel(graph);
		graph.addNode(startEvent);
		graph.addEdge(connection1);
		graph.addEdge(connection2);
		connection1.setSource(startEvent);
		connection1.setTarget(activity);
		connection2.setSource(activity);
		connection2.setTarget(endEvent);

		TraceCalculator calculator = new TraceCalculator(graph);
		List<Trace> traces = calculator.calculateTraces();
		assertEquals(1, traces.size());

		Trace trace = traces.get(0);
		assertEquals(3, trace.size());
		assertEquals(startEvent, trace.get(0));
		assertEquals(activity, trace.get(1));
		assertEquals(endEvent, trace.get(2));
	}
}
