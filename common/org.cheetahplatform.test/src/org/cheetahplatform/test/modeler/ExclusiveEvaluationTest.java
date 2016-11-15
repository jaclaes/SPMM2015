/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ExclusiveEvaluation;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class ExclusiveEvaluationTest extends AbstractBPMNTest {
	@Test
	public void exclusive() throws Exception {
		EdgeCondition condition1 = new EdgeCondition(1, "Condition 1", null, "");
		EdgeCondition condition2 = new EdgeCondition(2, "Condition 2", null, "");
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ExclusiveEvaluation evaluation = new ExclusiveEvaluation(condition1, condition2, activityA, activityB);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		Node activity2 = createActivity(graph, "B");
		Node xorSplit = createXorGateway(graph);
		Node xorJoin = createXorGateway(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void notExclusive() throws Exception {
		EdgeCondition condition1 = new EdgeCondition(1, "Condition 1", null, "");
		EdgeCondition condition2 = new EdgeCondition(2, "Condition 2", null, "");
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ExclusiveEvaluation evaluation = new ExclusiveEvaluation(condition1, condition2, activityA, activityB);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		Node activity2 = createActivity(graph, "B");
		Node andSplit = createAndGateway(graph);
		Node andJoin = createAndGateway(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, andSplit);
		createSequenceFlow(graph, andSplit, activity1);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, activity1, andJoin);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void notExclusive2() throws Exception {
		EdgeCondition condition1 = new EdgeCondition(1, "Condition 1", null, "");
		EdgeCondition condition2 = new EdgeCondition(2, "Condition 2", null, "");
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ExclusiveEvaluation evaluation = new ExclusiveEvaluation(condition1, condition2, activityA, activityB);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		Node activity2 = createActivity(graph, "B");
		Node xorSplit = createXorGateway(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		Edge flow1 = createSequenceFlow(graph, xorSplit, activity1);
		provider.associate(flow1, condition1);
		Edge flow2 = createSequenceFlow(graph, xorSplit, activity2);
		provider.associate(flow2, condition2);
		createSequenceFlow(graph, activity1, activity2);
		createSequenceFlow(graph, activity2, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void notExclusive3() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ExclusiveEvaluation evaluation = new ExclusiveEvaluation(null, null, activityA, activityB);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activityA);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, activityB);
		Node activity3 = createActivity(graph, "C");
		Node xorSplit = createXorGateway(graph);
		xorSplit.setName("XOR 1");
		Node xorSplit2 = createXorGateway(graph);
		xorSplit2.setName("XOR 2");
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		createSequenceFlow(graph, xorSplit, activity3);
		createSequenceFlow(graph, activity3, xorSplit2);
		createSequenceFlow(graph, xorSplit2, activity2);
		createSequenceFlow(graph, activity2, endEvent);
		createSequenceFlow(graph, activity1, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}
}
