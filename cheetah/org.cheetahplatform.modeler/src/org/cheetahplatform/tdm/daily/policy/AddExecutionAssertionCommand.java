package org.cheetahplatform.tdm.daily.policy;

import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.gef.commands.Command;

public class AddExecutionAssertionCommand extends Command {

	private final DateTime start;
	private final List<DeclarativeActivity> activities;
	private final Workspace workspace;

	public AddExecutionAssertionCommand(Workspace workspace, DateTime start, List<DeclarativeActivity> activities) {
		this.workspace = workspace;
		this.start = start;
		this.activities = activities;
	}

	@Override
	public void execute() {
		Day day = workspace.getDay(start);
		DateTime end = new DateTime(start, false);
		end.plus(new Duration(new DateTime(1, 0, false)));
		TimeSlot slot = new TimeSlot(start, end);

		for (DeclarativeActivity activity : activities) {
			workspace.addAssertion(new ExecutionAssertion(day, slot, activity));
		}
	}

}
