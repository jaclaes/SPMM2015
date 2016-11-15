package org.cheetahplatform.tdm.daily.policy;

import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.tdm.daily.editpart.ExecutionAssertionEditPolicy;
import org.cheetahplatform.tdm.daily.model.AbstractPlanningArea;
import org.eclipse.gef.commands.Command;

public class ExecutionAreaEditPolicy extends PlanningAreaEditPolicy {

	public ExecutionAreaEditPolicy() {
		super(new ExecutionAssertionEditPolicy());
	}

	@Override
	protected Command createAddActivityCommand(List<DeclarativeActivity> activities, AbstractPlanningArea area, DateTime time) {
		return new AddExecutionAssertionCommand(area.getWorkspace(), time, activities);
	}

}
