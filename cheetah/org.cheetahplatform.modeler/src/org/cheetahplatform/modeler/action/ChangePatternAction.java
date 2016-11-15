package org.cheetahplatform.modeler.action;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.06.2010
 */
public class ChangePatternAction extends AbstractModelingAction {
	public static final String ID = "org.cheetahplatform.modeler.action.changePatternAction";

	public ChangePatternAction() {
		super(5);

		setId(ID);
		setText("New Change Pattern Process");
	}

}
