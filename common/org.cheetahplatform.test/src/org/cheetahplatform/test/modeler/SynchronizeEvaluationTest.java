/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.SynchronizeEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class SynchronizeEvaluationTest extends AbstractBPMNTest {
	@Test
	public void isNotSynchronized() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityC = new Paragraph(1, "dummy", "C", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		SynchronizeEvaluation evaluation = new SynchronizeEvaluation(activityC, activityA, activityB);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activityA);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, activityB);
		Node activity3 = createActivity(graph, "C");
		provider.associate(activity3, activityC);
		Node endEvent = createEndEvent(graph);
		Node andSplit = createAndGateway(graph);
		Node xorJoin = createXorGateway(graph);
		createSequenceFlow(graph, startEvent, andSplit);
		createSequenceFlow(graph, andSplit, activity1);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, activity3);
		createSequenceFlow(graph, activity3, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void isSynchronized() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityC = new Paragraph(1, "dummy", "C", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		SynchronizeEvaluation evaluation = new SynchronizeEvaluation(activityC, activityA, activityB);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activityA);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, activityB);
		Node activity3 = createActivity(graph, "C");
		provider.associate(activity3, activityC);
		Node endEvent = createEndEvent(graph);
		Node andSplit = createAndGateway(graph);
		Node andJoin = createAndGateway(graph);
		createSequenceFlow(graph, startEvent, andSplit);
		createSequenceFlow(graph, andSplit, activity1);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, activity1, andJoin);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, andJoin, activity3);
		createSequenceFlow(graph, activity3, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}
}
