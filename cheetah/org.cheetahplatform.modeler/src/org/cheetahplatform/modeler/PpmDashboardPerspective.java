package org.cheetahplatform.modeler;

import org.cheetahplatform.modeler.dialog.ModelingPhaseDiagramView;
import org.cheetahplatform.modeler.dialog.PpmMetricsView;
import org.cheetahplatform.modeler.dialog.PpmNotesView;
import org.cheetahplatform.modeler.graph.dialog.ModelingTranscriptsView;
import org.cheetahplatform.modeler.graph.dialog.PpmIterationsView;
import org.cheetahplatform.modeler.graph.dialog.ReplayView;
import org.cheetahplatform.modeler.graph.dialog.StepsView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PpmDashboardPerspective implements IPerspectiveFactory {
	public static final String ID = "org.cheetahplatform.modeler.perspective.ppmdashboard";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView(ModelingPhaseDiagramView.ID, IPageLayout.LEFT, 0.5f, layout.getEditorArea());
		layout.addView(PpmMetricsView.ID, IPageLayout.RIGHT, 0.6f, ModelingPhaseDiagramView.ID);
		layout.addView(ReplayView.ID, IPageLayout.BOTTOM, 0.85f, ModelingPhaseDiagramView.ID);

		IFolderLayout folder = layout.createFolder("rightFolder", IPageLayout.BOTTOM, 0.4f, PpmMetricsView.ID);
		folder.addView(StepsView.ID);
		folder.addView(ModelingTranscriptsView.ID);
		folder.addView(PpmNotesView.ID);
		folder.addView(PpmIterationsView.ID);
	}
}
