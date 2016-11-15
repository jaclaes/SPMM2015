/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.modeler;

import static org.cheetahplatform.test.modeler.AbstractGraphCommandTest.NODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.decserflow.SelectionConstraintEdge;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.junit.Test;

public class SelectionConstraintEdgeTest {
	@Test
	public void hasMaximumOccurrence() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		SelectionConstraintEdge constraint = new SelectionConstraintEdge(graph, EditorRegistry
				.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION));

		assertFalse(constraint.hasMaximum());
		constraint.setMaximum(1);

		assertTrue(constraint.hasMaximum());
	}

	@Test
	public void hasMinimumOccurrence() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		SelectionConstraintEdge constraint = new SelectionConstraintEdge(graph, EditorRegistry
				.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION));

		assertFalse(constraint.hasMinimum());
		constraint.setMinimum(1);

		assertTrue(constraint.hasMinimum());
	}

	@Test
	public void setTarget() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		SelectionConstraintEdge edge = new SelectionConstraintEdge(graph, EditorRegistry.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION));
		Node node1 = NODE.createModel(graph);

		assertEquals(null, edge.getSource());
		assertEquals(null, edge.getTarget());

		edge.setSource(node1);

		assertEquals(node1, edge.getSource());
		assertEquals(node1, edge.getTarget());
	}

	@Test
	public void unsetMaximum() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		SelectionConstraintEdge constraint = new SelectionConstraintEdge(graph, EditorRegistry
				.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION));
		constraint.setMaximum(1);

		assertTrue(constraint.hasMaximum());
		constraint.unsetMaximum();
		assertFalse(constraint.hasMaximum());
	}

	@Test
	public void unsetMinimum() throws Exception {
		Graph graph = new Graph(new ArrayList<IGraphElementDescriptor>());
		SelectionConstraintEdge constraint = new SelectionConstraintEdge(graph, EditorRegistry
				.getDescriptor(EditorRegistry.DECSERFLOW_SELECTION));
		constraint.setMinimum(1);

		assertTrue(constraint.hasMinimum());
		constraint.unsetMinimum();
		assertFalse(constraint.hasMinimum());
	}
}
