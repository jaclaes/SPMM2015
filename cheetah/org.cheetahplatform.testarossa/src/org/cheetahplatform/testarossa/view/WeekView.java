package org.cheetahplatform.testarossa.view;

import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.weekly.WeeklyController;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.cheetahplatform.testarossa.persistence.PersistentModel;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class WeekView extends ViewPart {

	public static final String ID = "org.cheetahplatform.testarossa.WeekView";
	private WeeklyController controller;

	@Override
	public void createPartControl(Composite parent) {
		GraphicalViewerImpl viewer = new GraphicalViewerImpl();
		viewer.createControl(parent);

		controller = new WeeklyController(viewer);
		refresh();
	}

	public void refresh() {
		DeclarativeProcessInstance instance = TestaRossaModel.getInstance().getCurrentInstance();

		if (instance != null) {
			Workspace workspace = PersistentModel.getInstance().getWorkspace(instance);
			controller.setInput(workspace.getWeekly());
			String name = "Testa Rossa Process Support - " + instance.getName();
			setPartName(name);
			getSite().getShell().setText(name);
		}
	}

	@Override
	public void setFocus() {
		// do nothing
	}

}
