package org.cheetahplatform.tdm.daily.policy;

import org.cheetahplatform.tdm.daily.model.Activity;
import org.eclipse.gef.commands.Command;

public class DeleteActivityCommand extends Command {

	private final Activity activity;

	public DeleteActivityCommand(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void execute() {
		activity.delete();
	}

}
