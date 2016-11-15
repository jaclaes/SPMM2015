/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.LoopEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class LoopEvaluationTest extends AbstractBPMNTest {
	@Test
	public void noLoop() throws Exception {
		Paragraph firstActivity = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph secondActivity = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		LoopEvaluation evaluation = new LoopEvaluation(firstActivity, secondActivity);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, firstActivity);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, secondActivity);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, activity1);
		createSequenceFlow(graph, activity1, activity2);
		createSequenceFlow(graph, activity2, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void simpleLoop() throws Exception {
		Paragraph firstActivity = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph secondActivity = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		LoopEvaluation evaluation = new LoopEvaluation(firstActivity, secondActivity);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, firstActivity);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, secondActivity);
		Node endEvent = createEndEvent(graph);
		Node xorSplit = createXorGateway(graph);
		Node xorJoin = createXorGateway(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, xorJoin, activity2);
		createSequenceFlow(graph, activity2, xorSplit);
		createSequenceFlow(graph, xorJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void simpleLoop2() throws Exception {
		Paragraph firstActivity = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph secondActivity = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		LoopEvaluation evaluation = new LoopEvaluation(firstActivity, secondActivity);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, firstActivity);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, secondActivity);
		Node endEvent = createEndEvent(graph);
		Node xorSplit = createXorGateway(graph);
		createSequenceFlow(graph, startEvent, activity1);
		createSequenceFlow(graph, activity1, xorSplit);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, activity2, activity1);
		createSequenceFlow(graph, xorSplit, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}
}
