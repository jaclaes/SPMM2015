/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly;

import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.modeler.generic.GenericEditPartFactory;
import org.cheetahplatform.modeler.generic.GenericMenuManager;
import org.cheetahplatform.tdm.modeler.test.model.Weekly;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;

public class WeeklyController {
	private final GraphicalViewer viewer;

	public WeeklyController(GraphicalViewer viewer) {
		this.viewer = viewer;
		Viewport content = (Viewport) ((ScalableRootEditPart) viewer.getRootEditPart()).getFigure();
		LayeredPane layeredPane = (LayeredPane) content.getContents();
		Layer old = layeredPane.getLayer(LayerConstants.FEEDBACK_LAYER);
		layeredPane.remove(old);
		layeredPane.add(new Layer() {
			{
				setEnabled(false);
			}

			@Override
			public Dimension getPreferredSize(int wHint, int hHint) {
				return new Dimension(0, 0);
			}

		}, LayerConstants.FEEDBACK_LAYER);

		initialize();
	}

	private void initialize() {
		viewer.setEditDomain(new CustomEditDomain());
		viewer.setEditPartFactory(new GenericEditPartFactory());
		viewer.setContextMenu(new GenericMenuManager(viewer));
	}

	public void setInput(Weekly weekly) {
		viewer.setContents(weekly);
	}

}
