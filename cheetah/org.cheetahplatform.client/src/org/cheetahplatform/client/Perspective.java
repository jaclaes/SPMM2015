package org.cheetahplatform.client;

import org.cheetahplatform.client.ui.Worklist;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.addView(Worklist.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		layout.addView(IPageLayout.ID_PROGRESS_VIEW, IPageLayout.BOTTOM, 0.8f, layout.getEditorArea());
	}
}
