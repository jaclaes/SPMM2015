package org.cheetahplatform.tdm.command;

import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Workspace;

public class RemoveActivityInstanceCommand extends TDMCommand {

	private long testId;
	private final long activityId;

	public RemoveActivityInstanceCommand(long testId, long activityId) {
		this.testId = testId;
		this.activityId = activityId;
	}

	@Override
	public void execute() {
		Workspace workspace = getWorkspace(testId);
		Activity activity = workspace.getActivity(activityId);
		workspace = activity.getWorkspace();
		activity.delete();
	}

	@Override
	public void undo() {
		Workspace workspace = getWorkspace(testId);
		Activity activity = workspace.getActivity(activityId);
		workspace.addActivityInstance(activity);
	}

}
