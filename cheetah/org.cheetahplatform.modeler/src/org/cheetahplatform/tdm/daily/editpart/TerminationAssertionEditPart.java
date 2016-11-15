package org.cheetahplatform.tdm.daily.editpart;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.tdm.daily.action.ChangeTerminationAssertionTypeAction;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.DeleteActivityAction;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.policy.MoveActivityEditPolicy;
import org.cheetahplatform.tdm.daily.policy.TerminationAssertionEditPolicy;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.action.IMenuManager;

public class TerminationAssertionEditPart extends ActivityEditPart {

	public TerminationAssertionEditPart(Activity assertion) {
		super(assertion);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			menu.add(new ChangeTerminationAssertionTypeAction(getModel(), !getModel().canTerminate()));
			DeleteActivityAction action = new DeleteActivityAction(getModel());
			action.setText("Delete Termination Assertion");
			menu.add(action);
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TerminationAssertionEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MoveActivityEditPolicy(new TerminationAssertionEditPolicy()));
	}

	@Override
	public TerminationAssertion getModel() {
		return (TerminationAssertion) super.getModel();
	}

}
