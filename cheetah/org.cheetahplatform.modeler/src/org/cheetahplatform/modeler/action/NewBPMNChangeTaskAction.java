package org.cheetahplatform.modeler.action;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         31.05.2010
 */
public class NewBPMNChangeTaskAction extends AbstractModelingAction {
	public static final String ID = "org.cheetahplatform.modeler.action.NewBPMNChangeTaskAction";

	public NewBPMNChangeTaskAction() {
		super(2);

		setId(ID);
		setText("BPMN Change Task");
	}

}
