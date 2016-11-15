package org.cheetahplatform.modeler.graph;

import org.eclipse.gef.palette.PaletteRoot;

public interface IGraphViewerConfigurator {
	void configure(IGraphicalGraphViewerAdvisor advisor, GraphicalGraphViewerWithFlyoutPalette viewer, PaletteRoot palette);
}
