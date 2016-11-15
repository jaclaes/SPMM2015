package org.cheetahplatform.testarossa;

import org.cheetahplatform.testarossa.view.ReminderView;
import org.cheetahplatform.testarossa.view.WeekView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);

		layout.addStandaloneViewPlaceholder(WeekView.ID, IPageLayout.LEFT, 1.0f, layout.getEditorArea(), false);
		layout.addFastView(ReminderView.id);
	}
}
