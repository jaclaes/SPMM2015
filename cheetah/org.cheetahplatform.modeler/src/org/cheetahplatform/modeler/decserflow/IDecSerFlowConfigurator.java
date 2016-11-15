package org.cheetahplatform.modeler.decserflow;

import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.eclipse.gef.palette.PaletteRoot;

public interface IDecSerFlowConfigurator {

	void configure(DecSerFlowGraphAdvisor advisor, GraphicalGraphViewerWithFlyoutPalette viewer, PaletteRoot palette);

}
