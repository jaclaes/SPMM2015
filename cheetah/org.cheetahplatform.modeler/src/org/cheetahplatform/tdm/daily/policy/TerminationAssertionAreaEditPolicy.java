package org.cheetahplatform.tdm.daily.policy;

import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.tdm.daily.model.AbstractPlanningArea;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

public class TerminationAssertionAreaEditPolicy extends PlanningAreaEditPolicy {
	public TerminationAssertionAreaEditPolicy() {
		super(new TerminationAssertionEditPolicy());
	}

	@Override
	protected Command createAddActivityCommand(List<DeclarativeActivity> activities, AbstractPlanningArea area, DateTime time) {
		return null;
	}

	@Override
	public void showTargetFeedback(Request request) {
		// ignore
	}
}
