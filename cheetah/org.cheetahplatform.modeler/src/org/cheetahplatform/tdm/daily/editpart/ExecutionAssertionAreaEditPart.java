package org.cheetahplatform.tdm.daily.editpart;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.daily.action.AddExecutionAssertionAction;
import org.cheetahplatform.tdm.daily.action.CreateExecutionAssertionAction;
import org.cheetahplatform.tdm.daily.figure.PlanningAreaFigure;
import org.cheetahplatform.tdm.daily.policy.ExecutionAreaEditPolicy;
import org.cheetahplatform.tdm.daily.policy.PlanningAreaSelectionEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.action.IMenuManager;

public class ExecutionAssertionAreaEditPart extends AbstractPlanningAreaEditPart {

	public static final int WIDTH = PlanningAreaEditPart.WIDTH;

	public ExecutionAssertionAreaEditPart(IGenericModel model) {
		super(model);
	}

	@Override
	public void buildContextMenu(IMenuManager menu, Point dropLocation) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			menu.add(new AddExecutionAssertionAction(this, dropLocation));
			menu.add(new CreateExecutionAssertionAction(this, dropLocation));
		}
	}

	@Override
	protected void createEditPolicies() {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ALLOW_MODIFICATION_OF_TESTS)) {
			installEditPolicy(EditPolicy.CONTAINER_ROLE, new ExecutionAreaEditPolicy());
			installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new PlanningAreaSelectionEditPolicy());
		}
	}

	@Override
	protected IFigure createFigure() {
		PlanningAreaFigure figure = (PlanningAreaFigure) super.createFigure();
		figure.setBackgroundText("Right-click to add execution assertion");
		return figure;
	}

	@Override
	public TimeSlot getSelection() {
		return getWorkspace().getModel().getExecutionAreaSelection();
	}

	@Override
	public void setSelection(TimeSlot slot) {
		getWorkspace().getModel().setExecutionAreaSelection(slot);
	}
}
