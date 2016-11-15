package org.cheetahplatform.modeler.changepattern.action;

import org.cheetahplatform.modeler.changepattern.ChangePatternTutorialActivity;
import org.eclipse.jface.action.Action;

public class ShowChangePatternTutorialAction extends Action {
	public static final String ID = "org.cheetahplatform.modeler.action.ShowChangePatternTutorialAction";

	public ShowChangePatternTutorialAction() {
		setId(ID);
		setText("Show Change Pattern Tutorial");
	}

	@Override
	public void run() {
		new ChangePatternTutorialActivity().execute();
	}
}
