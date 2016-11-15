package org.cheetahplatform.tdm.daily.model;

import java.util.List;

import org.cheetahplatform.tdm.daily.editpart.ExecutionAssertionAreaEditPart;
import org.eclipse.gef.EditPart;

public class ExecutionAssertionArea extends AbstractPlanningArea {

	public ExecutionAssertionArea(Day parent) {
		super(parent, ExecutionAssertionAreaEditPart.WIDTH);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ExecutionAssertionAreaEditPart(this);
	}

	@Override
	public List<Activity> getActivities() {
		return getWorkspace().getExecutionAssertions(getParent());
	}

	@Override
	public List<? extends Object> getChildren() {
		return getActivities();
	}

}
