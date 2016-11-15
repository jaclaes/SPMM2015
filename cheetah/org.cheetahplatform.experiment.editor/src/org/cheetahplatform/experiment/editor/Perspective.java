package org.cheetahplatform.experiment.editor;

import org.cheetahplatform.experiment.editor.views.ValidationView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.60f,
				editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addView(ValidationView.ID);
		
	}

}
