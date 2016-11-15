/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.weekly.editpart;

import org.cheetahplatform.modeler.generic.editpart.GenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.weekly.WeeklyPlanningAreaLayout;
import org.cheetahplatform.tdm.weekly.figure.WeeklyPlanningAreaFigure;
import org.cheetahplatform.tdm.weekly.policy.WeeklyPlanningAreaEditPolicy;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;

public class WeeklyPlanningAreaEditPart extends GenericEditPart {

	private final WeeklyPlanningAreaLayout layout;

	public WeeklyPlanningAreaEditPart(IGenericModel model) {
		super(model);

		layout = new WeeklyPlanningAreaLayout(this);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new WeeklyPlanningAreaEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		WeeklyPlanningAreaFigure figure = new WeeklyPlanningAreaFigure();
		figure.setLayoutManager(layout);
		return figure;
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		return this;
	}

	@Override
	public void refresh() {
		super.refresh();

		layout.layout(getFigure());
	}

	@Override
	protected void refreshVisuals() {
		GridData constraint = new GridData();
		constraint.verticalAlignment = SWT.FILL;
		constraint.grabExcessVerticalSpace = true;
		constraint.horizontalAlignment = SWT.FILL;
		constraint.grabExcessHorizontalSpace = true;
		figure.getParent().getLayoutManager().setConstraint(figure, constraint);
	}

}
