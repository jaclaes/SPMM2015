package org.cheetahplatform.modeler.action;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         30.06.2010
 */
public class ChangePatternModificationAction extends AbstractModelingAction {
	public static final String ID = "org.cheetahplatform.modeler.action.changePatternModiciationAction";

	public ChangePatternModificationAction() {
		super(6);

		setId(ID);
		setText("New Change Pattern Modification Task");
	}

}
