package org.cheetahplatform.literatemodeling;

import org.cheetahplatform.literatemodeling.views.AssociationsView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class LiterateModelingPerspective implements IPerspectiveFactory {

	public static final String ID = "org.cheetahplatform.literatemodeling.literatemodelingperspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		String editorArea = layout.getEditorArea();

		layout.addView(AssociationsView.ID, IPageLayout.BOTTOM, 0.60f, editorArea);
	}
}
