package org.cheetahplatform.modeler.action;

import org.cheetahplatform.modeler.engine.TDMModelingActivity;

public class TDMAction extends AbstractModelingAction {
	public static final String ID = "org.cheetahplatform.modeler.action.TDMAction";

	public TDMAction() {
		super(4);

		setId(ID);
		setText("New TDM Process");
	}

	@Override
	public void run() {
		// clean up all mess
		TDMModelingActivity.cleanUpTDM();

		super.run();
	}

}
