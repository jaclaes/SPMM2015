package org.cheetahplatform.modeler.hierarchical;

import org.cheetahplatform.modeler.understandability.UnderstandabilityView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class HierarchicalPerspective implements IPerspectiveFactory {

	public static final String ID = "org.cheetahplatform.modeler.hierarchical.HierarchicalPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(UnderstandabilityView.ID, false, IPageLayout.TOP, 0.8f, layout.getEditorArea());
		layout.addStandaloneView(HierarchicalView.ID, false, IPageLayout.BOTTOM, 0.2f, UnderstandabilityView.ID);
		layout.addView(HierarchicalOutlineView.ID, IPageLayout.LEFT, 0.2f, UnderstandabilityView.ID);
		layout.getViewLayout(HierarchicalOutlineView.ID).setCloseable(false);
		layout.getViewLayout(HierarchicalOutlineView.ID).setMoveable(false);
	}

}
