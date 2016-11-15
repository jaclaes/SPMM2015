/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.DirectSuccessionEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class DirectSuccessionEvaluationTest extends AbstractBPMNTest {
	@Test
	public void activityDirectlySucceeded() throws Exception {
		Paragraph predecessor = new Paragraph(1, "dummy", "predecessor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph successor = new Paragraph(1, "dummy", "successor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		DirectSuccessionEvaluation evaluation = new DirectSuccessionEvaluation(predecessor, successor);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph);
		provider.associate(activity1, predecessor);
		Node activity2 = createActivity(graph);
		provider.associate(activity2, successor);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, activity1);
		createSequenceFlow(graph, activity1, activity2);
		createSequenceFlow(graph, activity2, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void activityDirectlySucceededByAndSplit() throws Exception {
		Paragraph predecessor = new Paragraph(1, "dummy", "predecessor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph successor = new Paragraph(1, "dummy", "successor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		DirectSuccessionEvaluation evaluation = new DirectSuccessionEvaluation(predecessor, successor);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph);
		provider.associate(activity1, predecessor);
		Node activity2 = createActivity(graph);
		provider.associate(activity2, successor);
		Node activity3 = createActivity(graph);
		provider.associate(activity3, successor);
		Node endEvent = createEndEvent(graph);
		Node andSplit = createAndGateway(graph);
		Node andJoin = createAndGateway(graph);

		createSequenceFlow(graph, startEvent, activity1);
		createSequenceFlow(graph, activity1, andSplit);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, andSplit, activity3);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, activity3, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void activityNotDirectlySucceeded() throws Exception {
		Paragraph predecessor = new Paragraph(1, "dummy", "predecessor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph successor = new Paragraph(1, "dummy", "successor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		DirectSuccessionEvaluation evaluation = new DirectSuccessionEvaluation(predecessor, successor);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph);
		provider.associate(activity1, predecessor);
		Node activity2 = createActivity(graph);
		provider.associate(activity2, successor);
		Node activity3 = createActivity(graph);
		Node endEvent = createEndEvent(graph);
		Node andSplit = createAndGateway(graph);
		Node andJoin = createAndGateway(graph);

		createSequenceFlow(graph, startEvent, activity1);
		createSequenceFlow(graph, activity1, andSplit);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, andSplit, activity3);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, activity3, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void activityNotPresent() throws Exception {
		Paragraph predecessor = new Paragraph(1, "dummy", "predecessor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph successor = new Paragraph(1, "dummy", "successor", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		DirectSuccessionEvaluation evaluation = new DirectSuccessionEvaluation(predecessor, successor);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph);
		Node activity2 = createActivity(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, activity1);
		createSequenceFlow(graph, activity1, activity2);
		createSequenceFlow(graph, activity2, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}
}
