package org.cheetahplatform.literatemodeling.dialog;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class GraphPartSelectionViewerAdvisor extends DefaultGraphicalGraphViewerAdvisor {
	public GraphPartSelectionViewerAdvisor() {
		super(new ArrayList<INodeDescriptor>(), new ArrayList<IEdgeDescriptor>());
	}

	public GraphPartSelectionViewerAdvisor(Graph graph) {
		super(new ArrayList<INodeDescriptor>(), new ArrayList<IEdgeDescriptor>(), graph);
	}

	@Override
	public int getInitialFlyoutPaletteState() {
		return HIDE_PALETTE;
	}
}
