package org.cheetahplatform.modeler.graph;

import java.util.List;

import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.palette.PaletteRoot;

public interface IGraphicalGraphViewerAdvisor {
	/**
	 * Fill the viewer's palette, typically by adding tools for editing the graph, e.g., creation of nodes.
	 * 
	 * @param viewer
	 *            the viewer whose palette should be filled
	 * @param palette
	 *            the palette to be extended
	 */
	void fillPalette(GraphicalGraphViewerWithFlyoutPalette viewer, PaletteRoot palette);

	/**
	 * Return the edge descriptors describing the graph's edges.
	 * 
	 * @return the edge descriptors
	 */
	List<IEdgeDescriptor> getEdgeDescriptors();

	/**
	 * Return the graph to be edited.
	 * 
	 * @return the graph
	 */
	Graph getGraph();

	/**
	 * Allows to adapt the initial flyout palette state, e.g., open/close/hide it.
	 * 
	 * @return the initial state
	 */
	int getInitialFlyoutPaletteState();

	/**
	 * Return the node descriptors describing the graph's nodes.
	 * 
	 * @return the node descriptors
	 */
	List<INodeDescriptor> getNodeDescriptors();

	boolean isDirectEditEnabled();

}
