/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.InitialActivityEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class InitialActivityEvaluationTest extends AbstractBPMNTest {
	@Test
	public void initialActivityCorrect() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		InitialActivityEvaluation evaluation = new InitialActivityEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity = createActivity(graph);
		provider.associate(activity, paragraph);
		Node startEvent = createStartEvent(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, activity);
		createSequenceFlow(graph, activity, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void initialActivityCorrectComplex() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		InitialActivityEvaluation evaluation = new InitialActivityEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity = createActivity(graph);
		Node activityWithoutIncomingEdge = createActivity(graph);
		provider.associate(activity, paragraph);
		Node startEvent = createStartEvent(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, activity);
		createSequenceFlow(graph, activity, endEvent);
		createSequenceFlow(graph, activityWithoutIncomingEdge, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void initialActivityInvalid() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		InitialActivityEvaluation evaluation = new InitialActivityEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity = createActivity(graph);
		Node startEvent = createStartEvent(graph);
		Node endEvent = createEndEvent(graph);
		createSequenceFlow(graph, startEvent, activity);
		createSequenceFlow(graph, activity, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void initialActivityInvalidComplex() throws Exception {
		Paragraph paragraph1 = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		Paragraph paragraph2 = new Paragraph(2, "dummy", "other description ", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		InitialActivityEvaluation evaluation = new InitialActivityEvaluation(paragraph1);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity1 = createActivity(graph);
		Node activity2 = createActivity(graph);
		provider.associate(activity1, paragraph1);
		provider.associate(activity2, paragraph2);
		Node startEvent = createStartEvent(graph);
		Node endEvent = createEndEvent(graph);
		Node andSplit = createAndGateway(graph);
		Node andJoin = createAndGateway(graph);
		createSequenceFlow(graph, startEvent, andSplit);
		createSequenceFlow(graph, andSplit, activity1);
		createSequenceFlow(graph, andSplit, activity2);
		createSequenceFlow(graph, activity1, andJoin);
		createSequenceFlow(graph, activity2, andJoin);
		createSequenceFlow(graph, andJoin, endEvent);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}
}
