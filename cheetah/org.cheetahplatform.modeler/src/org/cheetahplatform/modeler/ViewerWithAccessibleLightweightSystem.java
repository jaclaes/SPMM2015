package org.cheetahplatform.modeler;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;

public class ViewerWithAccessibleLightweightSystem extends GraphicalViewerImpl {
	@Override
	public LightweightSystem getLightweightSystem() {
		return super.getLightweightSystem();
	}
}