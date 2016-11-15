package org.cheetahplatform.modeler.action;

public class NewBPMNModelAction extends AbstractModelingAction {
	public static final String ID = "org.cheetahplatform.modeler.action.NewBPMNModelAction";

	public NewBPMNModelAction() {
		super(1);

		setId(ID);
		setText("BPMN");
	}
}
