/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ParallelEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class ParallelEvaluationTest extends AbstractBPMNTest {
	@Test
	public void isParallel() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityC = new Paragraph(1, "dummy", "C", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ParallelEvaluation evaluation = new ParallelEvaluation(activityA, activityB, activityC);
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
		createSequenceFlow(graph, andSplit, activity3);
		createSequenceFlow(graph, activity1, andJoin);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, activity3, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void isParallelSplittingActivity() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityC = new Paragraph(1, "dummy", "C", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ParallelEvaluation evaluation = new ParallelEvaluation(activityA, activityB, activityC);
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
		Node splittingActivity = createActivity(graph);
		Node andJoin = createAndGateway(graph);
		createSequenceFlow(graph, startEvent, splittingActivity);
		createSequenceFlow(graph, splittingActivity, activity1);
		createSequenceFlow(graph, splittingActivity, activity2);
		createSequenceFlow(graph, splittingActivity, activity3);
		createSequenceFlow(graph, activity1, andJoin);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, activity3, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void notAllInParallel() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityC = new Paragraph(1, "dummy", "C", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ParallelEvaluation evaluation = new ParallelEvaluation(activityA, activityB, activityC);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node startEvent = createStartEvent(graph);
		Node activity1 = createActivity(graph, "A");
		provider.associate(activity1, activityA);
		Node activity2 = createActivity(graph, "B");
		provider.associate(activity2, activityB);
		Node activity3 = createActivity(graph, "D");
		Node endEvent = createEndEvent(graph);
		Node andSplit = createAndGateway(graph);
		Node andJoin = createAndGateway(graph);
		createSequenceFlow(graph, startEvent, andSplit);
		createSequenceFlow(graph, andSplit, activity1);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, andSplit, activity3);
		createSequenceFlow(graph, activity1, andJoin);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, activity3, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void notInParallel() throws Exception {
		Paragraph activityA = new Paragraph(1, "dummy", "A", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityB = new Paragraph(1, "dummy", "B", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph activityC = new Paragraph(1, "dummy", "C", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ParallelEvaluation evaluation = new ParallelEvaluation(activityA, activityB, activityC);
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
		Node xorSplit = createXorGateway(graph);
		Node xorJoin = createXorGateway(graph);
		createSequenceFlow(graph, startEvent, xorSplit);
		createSequenceFlow(graph, xorSplit, activity1);
		createSequenceFlow(graph, xorSplit, activity2);
		createSequenceFlow(graph, xorSplit, activity3);
		createSequenceFlow(graph, activity1, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, activity2, xorJoin);
		createSequenceFlow(graph, xorJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}
}
