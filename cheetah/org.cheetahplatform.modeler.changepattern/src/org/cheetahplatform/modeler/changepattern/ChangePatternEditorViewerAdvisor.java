package org.cheetahplatform.modeler.changepattern;

import java.util.List;

import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.06.2010
 */
public class ChangePatternEditorViewerAdvisor extends DefaultGraphicalGraphViewerAdvisor {

	/**
	 * @param nodeDescriptors
	 * @param edgeDescriptors
	 * @param graph
	 */
	public ChangePatternEditorViewerAdvisor(List<INodeDescriptor> nodeDescriptors, List<IEdgeDescriptor> edgeDescriptors, Graph graph) {
		super(nodeDescriptors, edgeDescriptors, graph);
	}

	@Override
	public int getInitialFlyoutPaletteState() {
		return HIDE_PALETTE;
	}

	@Override
	public boolean isDirectEditEnabled() {
		return false;
	}
}
