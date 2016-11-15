package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.engine.LayoutTutorialActivity;
import org.eclipse.jface.action.Action;

public class ShowLayoutTutorialAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.action.ShowLayoutTutorialAction";

	public ShowLayoutTutorialAction() {
		setId(ID);
		setText("Show Layout Tutorial");
	}

	@Override
	public void run() {
		new LayoutTutorialActivity().execute();
	}
}
