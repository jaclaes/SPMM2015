package org.cheetahplatform.tdm.daily.editpart;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.daily.action.CreateTerminationAssertionAction;
import org.cheetahplatform.tdm.daily.policy.PlanningAreaSelectionEditPolicy;
import org.cheetahplatform.tdm.daily.policy.TerminationAssertionAreaEditPolicy;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.action.IMenuManager;

public class TerminationAssertionAreaEditPart extends AbstractPlanningAreaEditPart {

	public static final int WIDTH = 50;

	public TerminationAssertionAreaEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			menu.add(new CreateTerminationAssertionAction(this, dropLocation));
		}
	}

	@Override
	protected void createEditPolicies() {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			installEditPolicy(EditPolicy.CONTAINER_ROLE, new TerminationAssertionAreaEditPolicy());
			installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new PlanningAreaSelectionEditPolicy());
		}
	}

	@Override
	public TimeSlot getSelection() {
		return getWorkspace().getModel().getTerminationAreaSelection();
	}

	@Override
	public void setSelection(TimeSlot slot) {
		getWorkspace().getModel().setTerminationAreaSelection(slot);
	}

}
