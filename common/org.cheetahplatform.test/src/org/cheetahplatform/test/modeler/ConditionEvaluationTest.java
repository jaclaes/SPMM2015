/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ConditionEvaluation;
import org.cheetahplatform.modeler.graph.mapping.EdgeCondition;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class ConditionEvaluationTest extends AbstractBPMNTest {
	@Test
	public void conditionFulfilledTolerance0() throws Exception {
		EdgeCondition condition = new EdgeCondition(1, "Condition 1", null, "");
		Paragraph activity = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ConditionEvaluation evaluation = new ConditionEvaluation(condition, activity);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activity);
		Node activity2 = createActivity(graph, "B");
		Node xorSplit = createXorGateway(graph);
		Node xorJoin = createXorGateway(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		Edge sequenceFlow = createSequenceFlow(graph, xorSplit, activity1);
		provider.associate(sequenceFlow, condition);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void conditionFulfilledTolerance1() throws Exception {
		EdgeCondition condition = new EdgeCondition(1, "Condition 1", null, "");
		Paragraph activity = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ConditionEvaluation evaluation = new ConditionEvaluation(condition, activity);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity0 = createActivity(graph, "X");
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activity);
		Node activity2 = createActivity(graph, "B");
		Node xorSplit = createXorGateway(graph);
		Node xorJoin = createXorGateway(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		Edge sequenceFlow = createSequenceFlow(graph, xorSplit, activity0);
		provider.associate(sequenceFlow, condition);
		createSequenceFlow(graph, activity0, activity1);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void conditionNotFulfilled() throws Exception {
		EdgeCondition condition = new EdgeCondition(1, "Condition 1", null, "");
		Paragraph activity = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ConditionEvaluation evaluation = new ConditionEvaluation(condition, activity);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activity);
		Node activity2 = createActivity(graph, "B");
		Node xorSplit = createXorGateway(graph);
		Node xorJoin = createXorGateway(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		Edge sequenceFlow = createSequenceFlow(graph, xorSplit, activity2);
		provider.associate(sequenceFlow, condition);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

}
