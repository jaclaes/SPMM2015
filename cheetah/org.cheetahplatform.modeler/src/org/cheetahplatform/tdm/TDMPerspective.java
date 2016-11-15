/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import org.cheetahplatform.tdm.explorer.TDMProjectExplorerView;
import org.cheetahplatform.tdm.modeler.declarative.TDMDeclarativeModelerView;
import org.cheetahplatform.tdm.problemview.ProblemView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class TDMPerspective implements IPerspectiveFactory {
	public static final String ID = "org.cheetahplatform.tdm.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addView(TDMDeclarativeModelerView.ID, IPageLayout.RIGHT, 0.25f, layout.getEditorArea());
		layout.getViewLayout(TDMDeclarativeModelerView.ID).setCloseable(false);

		layout.addView(TDMProjectExplorerView.ID, IPageLayout.BOTTOM, 0.7f, TDMDeclarativeModelerView.ID);
		layout.getViewLayout(TDMProjectExplorerView.ID).setCloseable(false);

		layout.addView(ProblemView.ID, IPageLayout.RIGHT, 0.45f, TDMProjectExplorerView.ID);
		layout.getViewLayout(ProblemView.ID).setCloseable(false);

		layout.setEditorAreaVisible(true);
	}

}