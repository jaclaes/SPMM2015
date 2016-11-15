/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.literatemodeling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.literatemodeling.model.MultiElementLiterateModelingAssociation;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;

public class MultiElementLiterateModelingAssociationTest {
	@Test
	public void getEdges() {
		List<GraphElement> list = new ArrayList<GraphElement>();
		DummyNode element1 = new DummyNode();
		DummyNode element2 = new DummyNode();
		DummyEdge edge1 = new DummyEdge();
		DummyEdge edge2 = new DummyEdge();
		DummyEdge edge3 = new DummyEdge();
		list.add(element1);
		list.add(edge1);
		list.add(element2);
		list.add(edge2);
		list.add(edge3);

		MultiElementLiterateModelingAssociation association = new MultiElementLiterateModelingAssociation("name", 2, 2, list);

		List<Edge> edges = association.getEdges();
		assertEquals(3, edges.size());
		assertTrue(edges.contains(edge1));
		assertTrue(edges.contains(edge2));
		assertTrue(edges.contains(edge3));
	}

	@Test
	public void getNodes() {
		List<GraphElement> list = new ArrayList<GraphElement>();
		DummyNode element1 = new DummyNode();
		DummyNode element2 = new DummyNode();
		DummyNode element3 = new DummyNode();
		DummyEdge edge1 = new DummyEdge();
		DummyEdge edge2 = new DummyEdge();
		DummyEdge edge3 = new DummyEdge();
		list.add(element1);
		list.add(edge1);
		list.add(element2);
		list.add(edge2);
		list.add(element3);
		list.add(edge3);

		MultiElementLiterateModelingAssociation association = new MultiElementLiterateModelingAssociation("name", 2, 2, list);

		List<Node> nodes = association.getNodes();
		assertEquals(3, nodes.size());
		assertTrue(nodes.contains(element1));
		assertTrue(nodes.contains(element2));
		assertTrue(nodes.contains(element3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalName() {
		new MultiElementLiterateModelingAssociation(" ", 2, 2, new ArrayList<GraphElement>());
	}

	@Test
	public void matches() {
		List<GraphElement> list = new ArrayList<GraphElement>();
		DummyNode element1 = new DummyNode();
		DummyNode element2 = new DummyNode();
		DummyNode element3 = new DummyNode();
		DummyEdge edge1 = new DummyEdge();
		DummyEdge edge2 = new DummyEdge();
		DummyEdge edge3 = new DummyEdge();
		list.add(element1);
		list.add(edge1);
		list.add(element2);
		list.add(edge2);
		list.add(element3);
		list.add(edge3);
		MultiElementLiterateModelingAssociation association = new MultiElementLiterateModelingAssociation("name", 2, 2, list);
		assertTrue(association.matches(element1));
		assertTrue(association.matches(element2));
		assertTrue(association.matches(element3));
		assertFalse(association.matches(new DummyNode()));
	}

	@Test(expected = AssertionFailedException.class)
	public void nullName() {
		new MultiElementLiterateModelingAssociation(null, 2, 2, new ArrayList<GraphElement>());
	}
}
