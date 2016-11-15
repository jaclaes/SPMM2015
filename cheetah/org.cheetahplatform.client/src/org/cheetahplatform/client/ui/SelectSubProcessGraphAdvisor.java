package org.cheetahplatform.client.ui;

import java.util.ArrayList;

import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;

public class SelectSubProcessGraphAdvisor extends DefaultGraphicalGraphViewerAdvisor {

	public SelectSubProcessGraphAdvisor() {
		super(new ArrayList<INodeDescriptor>(), new ArrayList<IEdgeDescriptor>());
	}

	public SelectSubProcessGraphAdvisor(Graph graph) {
		super(new ArrayList<INodeDescriptor>(), new ArrayList<IEdgeDescriptor>(), graph);
	}

	@Override
	public int getInitialFlyoutPaletteState() {
		return HIDE_PALETTE;
	}

}
