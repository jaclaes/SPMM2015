package org.cheetahplatform.modeler;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "org.cheetahplatform.modeler.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
	}
}
