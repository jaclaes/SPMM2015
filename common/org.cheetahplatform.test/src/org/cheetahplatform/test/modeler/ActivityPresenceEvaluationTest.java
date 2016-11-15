/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.junit.Assert.assertEquals;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.ActivityPresenceEvaluation;
import org.cheetahplatform.modeler.graph.mapping.Paragraph;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.test.TestParagraphProvider;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class ActivityPresenceEvaluationTest extends AbstractBPMNTest {
	@Test
	public void activityIsPresent() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ActivityPresenceEvaluation evaluation = new ActivityPresenceEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity1 = createActivity(graph);
		createActivity(graph);
		createActivity(graph);
		provider.associate(activity1, paragraph);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.0001);
	}

	@Test
	public void activityNotPresent() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ActivityPresenceEvaluation evaluation = new ActivityPresenceEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		createActivity(graph);
		createActivity(graph);
		createActivity(graph);

		double result = evaluation.evaluate(graph, null);
		assertEquals(0, result, 0.0001);
	}

	@Test
	public void semanticallyIncorrect() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", ActivityPresenceEvaluation.SEMANTICALLY_INCORRECT_ACTIVITY, new RGB(0, 0, 0),
				Paragraph.NO_ELEMENT_ID);
		ActivityPresenceEvaluation evaluation = new ActivityPresenceEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity = createActivity(graph);
		provider.associate(activity, paragraph);
		createActivity(graph);
		createActivity(graph);

		double result = evaluation.evaluate(graph, null);
		assertEquals(-1, result, 0.00001);
	}

	@Test
	public void startEventIsPresent() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", "description", new RGB(0, 0, 0), Paragraph.NO_ELEMENT_ID);
		ActivityPresenceEvaluation evaluation = new ActivityPresenceEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN_START_EVENT));
		Node startEvent = createStartEvent(graph);
		createActivity(graph);
		createActivity(graph);
		provider.associate(startEvent, paragraph);

		double result = evaluation.evaluate(graph, null);
		assertEquals(1, result, 0.00001);
	}

	@Test
	public void superfluousActivityPresent() throws Exception {
		Paragraph paragraph = new Paragraph(1, "dummy", ActivityPresenceEvaluation.SUPERFLUOUS_ACTIVITY, new RGB(0, 0, 0),
				Paragraph.NO_ELEMENT_ID);
		ActivityPresenceEvaluation evaluation = new ActivityPresenceEvaluation(paragraph);
		TestParagraphProvider provider = new TestParagraphProvider();
		evaluation.setMappingProvider(provider);

		Graph graph = new Graph(EditorRegistry.getDescriptors(EditorRegistry.BPMN));
		Node activity = createActivity(graph);
		provider.associate(activity, paragraph);
		createActivity(graph);
		createActivity(graph);

		double result = evaluation.evaluate(graph, null);
		assertEquals(-1, result, 0.00001);
	}
}
