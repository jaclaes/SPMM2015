package org.cheetahplatform.modeler.dialog;

import org.cheetahplatform.modeler.generic.GenericEditPartFactory;
import org.cheetahplatform.modeler.generic.GenericMenuManager;
import org.cheetahplatform.modeler.graph.model.ModelingPhaseDiagramChartModel;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ModelingPhaseDiagramView extends ViewPart {

	public static final String ID = "org.cheetahplatform.view.modelingphasediagram";

	@Override
	public void createPartControl(Composite parent) {
		EditDomain domain = new EditDomain();
		ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
		viewer.setEditDomain(domain);
		viewer.setEditPartFactory(new GenericEditPartFactory());
		viewer.setContextMenu(new GenericMenuManager(viewer));
		viewer.createControl(parent);
		viewer.setContents(new ModelingPhaseDiagramChartModel());
	}

	@Override
	public void setFocus() {
		// do nothing
	}
}
