package org.cheetahplatform.modeler.graph;

import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.resource.ImageDescriptor;

public class GraphNodeToolEntry extends ToolEntry {

	public GraphNodeToolEntry(String label, ImageDescriptor image, CreationFactory factory) {
		super(label, label, image, image, GraphElementCreationTool.class);

		setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, factory);
	}

}
