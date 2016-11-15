package org.cheetahplatform.modeler.graph;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.jface.resource.ImageDescriptor;

public class DefaultGraphicalGraphViewerAdvisor implements IGraphicalGraphViewerAdvisor {
	/** state for hidden flyout palette - constant provided in FlyoutPaletteComposite is not public unfortunately **/
	public static final int HIDE_PALETTE = 8;

	public static Graph createGraph(List<INodeDescriptor> nodeDescriptors, List<IEdgeDescriptor> edgeDescriptors) {
		List<IGraphElementDescriptor> descriptors = new ArrayList<IGraphElementDescriptor>();
		descriptors.addAll(nodeDescriptors);
		descriptors.addAll(edgeDescriptors);

		return new Graph(descriptors);
	}

	private Graph graph;
	private final List<INodeDescriptor> nodeDescriptors;
	private final List<IEdgeDescriptor> edgeDescriptors;

	public DefaultGraphicalGraphViewerAdvisor(List<INodeDescriptor> nodeDescriptors, List<IEdgeDescriptor> edgeDescriptors) {
		this(nodeDescriptors, edgeDescriptors, createGraph(nodeDescriptors, edgeDescriptors));
	}

	public DefaultGraphicalGraphViewerAdvisor(List<INodeDescriptor> nodeDescriptors, List<IEdgeDescriptor> edgeDescriptors, Graph graph) {
		this.nodeDescriptors = nodeDescriptors;
		this.edgeDescriptors = edgeDescriptors;
		this.graph = graph;
	}

	@Override
	public void fillPalette(GraphicalGraphViewerWithFlyoutPalette viewer, PaletteRoot palette) {
		PaletteGroup creationTools = new PaletteGroup("Objects");
		palette.add(creationTools);

		for (INodeDescriptor descriptor : nodeDescriptors) {
			ImageDescriptor image = descriptor.getIconDescriptor();
			creationTools.add(new GraphNodeToolEntry(descriptor.getName(), image, new GraphElementCreationFactory(descriptor, graph)));
		}

		for (IEdgeDescriptor descriptor : edgeDescriptors) {
			ImageDescriptor image = descriptor.getIconDescriptor();
			creationTools.add(new ConnectionCreationToolEntry(descriptor.getName(), descriptor.getName(), new GraphElementCreationFactory(
					descriptor, graph), image, image));
		}

		// add custom tools to the palette.
		IGraphViewerConfigurator configurator = (IGraphViewerConfigurator) CheetahPlatformConfigurator
				.getObject(IConfiguration.GRAPH_EDITOR_TOOL_CONTRIBUTOR);
		if (configurator != null) {
			configurator.configure(this, viewer, palette);
		}
	}

	@Override
	public List<IEdgeDescriptor> getEdgeDescriptors() {
		return edgeDescriptors;
	}

	@Override
	public Graph getGraph() {
		return graph;
	}

	@Override
	public int getInitialFlyoutPaletteState() {
		return FlyoutPaletteComposite.STATE_PINNED_OPEN;
	}

	@Override
	public List<INodeDescriptor> getNodeDescriptors() {
		return nodeDescriptors;
	}

	@Override
	public boolean isDirectEditEnabled() {
		return true;
	}

}
