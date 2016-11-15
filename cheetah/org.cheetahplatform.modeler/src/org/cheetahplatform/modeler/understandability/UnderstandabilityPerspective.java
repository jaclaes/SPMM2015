package org.cheetahplatform.modeler.understandability;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class UnderstandabilityPerspective implements IPerspectiveFactory {

	public static final String ID = "org.cheetahplatform.modeler.understandability.UnderstandabilityPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(UnderstandabilityView.ID, false, IPageLayout.TOP, 0.65f, layout.getEditorArea());
		layout.addStandaloneView(ImageView.ID, false, IPageLayout.BOTTOM, 0.35f, UnderstandabilityView.ID);
	}

}
