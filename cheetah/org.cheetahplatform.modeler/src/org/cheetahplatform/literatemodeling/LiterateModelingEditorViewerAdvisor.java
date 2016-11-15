package org.cheetahplatform.literatemodeling;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphElementCreationFactory;
import org.cheetahplatform.modeler.graph.GraphNodeToolEntry;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.06.2010
 */
public class LiterateModelingEditorViewerAdvisor extends DefaultGraphicalGraphViewerAdvisor {

	/**
	 * @param nodeDescriptors
	 * @param edgeDescriptors
	 */
	public LiterateModelingEditorViewerAdvisor(Graph graph) {
		super(EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN), EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN), graph);
	}

	@Override
	public void fillPalette(GraphicalGraphViewerWithFlyoutPalette viewer,PaletteRoot palette) {
		PaletteGroup creationTools = new PaletteGroup("Objects");
		palette.add(creationTools);

		for (INodeDescriptor descriptor : getNodeDescriptors()) {
			if (descriptor.getId().equals(EditorRegistry.BPMN_ACTIVITY)) {
				continue;
			}

			ImageDescriptor image = descriptor.getIconDescriptor();
			creationTools.add(new GraphNodeToolEntry(descriptor.getName(), image, new GraphElementCreationFactory(descriptor, getGraph())));
		}

		for (IEdgeDescriptor descriptor : getEdgeDescriptors()) {
			ImageDescriptor image = descriptor.getIconDescriptor();
			creationTools.add(new ConnectionCreationToolEntry(descriptor.getName(), descriptor.getName(), new GraphElementCreationFactory(
					descriptor, getGraph()), image, image));
		}
	}
}
