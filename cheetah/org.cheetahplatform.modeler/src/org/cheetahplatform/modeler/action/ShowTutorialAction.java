package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.engine.TutorialActivity;
import org.eclipse.jface.action.Action;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.10.2009
 */
public class ShowTutorialAction extends Action {

	public static final String ID = "org.cheetahplatform.modeler.action.MapParagraphAction";

	public ShowTutorialAction() {
		setId(ID);
		setText("Show Tutorial");
	}

	@Override
	public void run() {
		new TutorialActivity().execute();
	}
}
