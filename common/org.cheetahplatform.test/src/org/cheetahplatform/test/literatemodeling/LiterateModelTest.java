/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import static org.cheetahplatform.modeler.EditorRegistry.BPMN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.literatemodeling.model.ILiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.MultiElementLiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.NodeLiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.report.EdgeLiterateModelingAssociation;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.command.DeleteEdgeCommand;
import org.cheetahplatform.modeler.graph.command.DeleteNodeCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.junit.Test;

public class LiterateModelTest {

	@Test
	public void associatedGraphElement() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		assertFalse(model.isAssociationDefined(new DummyNode()));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);
		assertFalse(model.isAssociationDefined(new DummyNode()));
		assertTrue(model.isAssociationDefined(element));
	}

	@Test
	public void changeLengthOfAssociationOverlappingAtBeginning() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 10, element);
		model.addAssociation(association);
		model.udpateAssociations(10, 0, 15);
		assertEquals(5, association.getOffset());
		assertEquals(5, association.getLength());
	}

	@Test
	public void changeLengthOfAssociationOverlappingAtEnd() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 10, element);
		model.addAssociation(association);
		model.udpateAssociations(23, 0, 15);
		assertEquals(20, association.getOffset());
		assertEquals(3, association.getLength());
	}

	private Graph createGraph() {
		return new Graph(new ArrayList<IGraphElementDescriptor>());
	}

	@Test
	public void deleteAfterAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		model.udpateAssociations(26, 0, 10);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(20, returnedAssociation.getOffset());
		assertEquals(5, returnedAssociation.getLength());
	}

	@Test
	public void deleteBeforeAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		model.udpateAssociations(10, 0, 5);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(15, returnedAssociation.getOffset());
		assertEquals(5, returnedAssociation.getLength());
	}

	@Test
	public void deleteWithinAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 10, element);
		model.addAssociation(association);

		model.udpateAssociations(22, 0, 5);
		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(20, returnedAssociation.getOffset());
		assertEquals(5, returnedAssociation.getLength());
	}

	@Test
	public void getAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);
		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertSame(association, returnedAssociation);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getAssociations() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		model.addAssociation(new NodeLiterateModelingAssociation(20, 10, element));
		assertEquals(1, model.getAssociations().size());
		model.getAssociations().add(new NodeLiterateModelingAssociation(20, 10, element));
	}

	@Test
	public void getStyleRange() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		Color color = new Color(null, 255, 0, 0);
		StyleRange styleRange = model.getStyleRange(element, color, null);
		assertEquals(color, styleRange.foreground);
		assertNull(styleRange.background);
		assertEquals(20, styleRange.start);
		assertEquals(5, styleRange.length);

		color.dispose();
	}

	@Test(expected = IllegalArgumentException.class)
	public void getStyleRangeForUndefinedAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		model.getStyleRange(new DummyNode(), null, null);
	}

	@Test
	public void getTextForAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		model.setDocument(new Document("This is the description\nline 2"));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(5, 2, element);
		model.addAssociation(association);

		String textForAssociation = model.getTextForAssociation(association);
		assertEquals("is", textForAssociation);
		association = new NodeLiterateModelingAssociation(24, 4, element);
		model.addAssociation(association);
		textForAssociation = model.getTextForAssociation(association);
		assertEquals("line", textForAssociation);
	}

	@Test(expected = AssertionFailedException.class)
	public void illegalGraph() {
		new LiterateModel(null);
	}

	@Test
	public void insertAfterAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		model.udpateAssociations(26, 10, 0);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(20, returnedAssociation.getOffset());
		assertEquals(5, returnedAssociation.getLength());
	}

	@Test
	public void insertIntoAssociationRange() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		model.udpateAssociations(22, 10, 0);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(20, returnedAssociation.getOffset());
		assertEquals(15, returnedAssociation.getLength());
	}

	@Test
	public void insertTextBeforeAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		model.udpateAssociations(10, 5, 0);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(25, returnedAssociation.getOffset());
		assertEquals(5, returnedAssociation.getLength());
	}

	@Test(expected = AssertionFailedException.class)
	public void nullAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		model.addAssociation(null);
	}

	@Test
	public void removeAssociationExactMatch() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);

		model.udpateAssociations(20, 0, 5);
		assertFalse(model.isAssociationDefined(element));
	}

	@Test()
	public void removeEdge() {
		Graph graph = createGraph();
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyEdge element = new DummyEdge();
		element.setParent(graph);
		graph.addEdge(element);
		EdgeLiterateModelingAssociation association = new EdgeLiterateModelingAssociation(20, 10, element);
		model.addAssociation(association);

		assertNotNull(model.getAssociation(element));
		DeleteEdgeCommand deleteNodeCommand = new DeleteEdgeCommand(element);
		deleteNodeCommand.execute();

		try {
			model.getAssociation(element);
			fail();
		} catch (IllegalArgumentException e) {
			// perfect
		}
	}

	@Test
	public void removeInFrontOfAssociationOverlapping() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(30, 5, element);
		model.addAssociation(association);
		model.udpateAssociations(20, 0, 20);
		assertFalse(model.isAssociationDefined(element));
	}

	@Test
	public void removeMoreThanAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 5, element);
		model.addAssociation(association);
		model.udpateAssociations(20, 0, 15);
		assertFalse(model.isAssociationDefined(element));
	}

	@Test()
	public void removeNode() {
		Graph graph = createGraph();
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		element.setParent(graph);
		graph.addNode(element);
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 10, element);
		model.addAssociation(association);

		assertNotNull(model.getAssociation(element));
		DeleteNodeCommand deleteNodeCommand = new DeleteNodeCommand(element);
		deleteNodeCommand.execute();

		try {
			model.getAssociation(element);
			fail();
		} catch (IllegalArgumentException e) {
			// perfect
		}
	}

	@Test()
	public void removeNodeFromMultiAssociation() {
		Graph graph = createGraph();
		List<GraphElement> nodes = new ArrayList<GraphElement>();
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		element.setParent(graph);
		DummyNode element2 = new DummyNode();
		element2.setParent(graph);
		graph.addNode(element);
		nodes.add(element);
		nodes.add(element2);
		MultiElementLiterateModelingAssociation association = new MultiElementLiterateModelingAssociation("comment", 20, 10, nodes);
		model.addAssociation(association);

		assertNotNull(model.getAssociation(element));
		DeleteNodeCommand deleteNodeCommand = new DeleteNodeCommand(element);
		deleteNodeCommand.execute();
		assertNotNull(model.getAssociation(element2));
		deleteNodeCommand = new DeleteNodeCommand(element2);
		deleteNodeCommand.execute();

		try {
			model.getAssociation(element);
			fail();
		} catch (IllegalArgumentException e) {
			// perfect
		}
	}

	@Test
	public void replaceAfterAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 8, element);
		model.addAssociation(association);
		model.udpateAssociations(29, 10, 5);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(20, returnedAssociation.getOffset());
		assertEquals(8, returnedAssociation.getLength());
	}

	@Test
	public void replaceInFrontOfAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 8, element);
		model.addAssociation(association);
		model.udpateAssociations(10, 10, 5);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(25, returnedAssociation.getOffset());
		assertEquals(8, returnedAssociation.getLength());
	}

	@Test
	public void replaceWithinAssociation() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		DummyNode element = new DummyNode();
		NodeLiterateModelingAssociation association = new NodeLiterateModelingAssociation(20, 8, element);
		model.addAssociation(association);
		model.udpateAssociations(22, 10, 5);

		ILiterateModelingAssociation returnedAssociation = model.getAssociation(element);
		assertEquals(20, returnedAssociation.getOffset());
		assertEquals(13, returnedAssociation.getLength());
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAssociationsIllegalDeletionLength() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		model.udpateAssociations(0, 0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAssociationsIllegalInsertionLength() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		model.udpateAssociations(0, -1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAssociationsIllegalOffset() {
		LiterateModel model = new LiterateModel(EditorRegistry.getDescriptors(BPMN));
		model.udpateAssociations(-1, 0, 0);
	}
}
