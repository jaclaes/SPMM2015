package org.cheetahplatform.tdm.daily.editpart;

import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.tdm.daily.action.ChangeExecutionAssertionTypeAction;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.policy.MoveActivityEditPolicy;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.action.IMenuManager;

public class ExecutionAssertionEditPart extends ActivityEditPart {

	public ExecutionAssertionEditPart(Activity tddActivityInstance) {
		super(tddActivityInstance);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			menu.add(new ChangeExecutionAssertionTypeAction(getModel(), !getModel().isExecutable()));
		}

		super.buildContextMenu(menu, dropLocation);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ExecutionAssertionEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new MoveActivityEditPolicy(new ExecutionAssertionEditPolicy()));
	}

	@Override
	public ExecutionAssertion getModel() {
		return (ExecutionAssertion) super.getModel();
	}

}
