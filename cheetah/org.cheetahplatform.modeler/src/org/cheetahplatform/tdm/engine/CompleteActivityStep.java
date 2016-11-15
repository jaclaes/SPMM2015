package org.cheetahplatform.tdm.engine;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.COMPLETED;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class CompleteActivityStep implements ITDMStep {

	private TDMActivity activity;

	public CompleteActivityStep(TDMActivity activity) {
		this.activity = activity;
	}

	@Override
	public void execute(DeclarativeProcessInstance instance) throws TDMTestFailedException {
		if (activity.getActivityInstance() == null) {
			return; // ignore, possibly launching was not possible
		}

		if (!activity.getActivityInstance().getState().isValidTransition(COMPLETED)) {
			throw new TDMTestFailedException(this);
		}

		activity.getActivityInstance().complete();
	}

	@Override
	public ITDMFailureCause getFailure() {
		return new DummyCause(); // visualized by starting the activity anyway
	}

	@Override
	public Object getSource() {
		return activity.getActivityUiModel();
	}

	@Override
	public DateTime getStartTime() {
		return activity.getEndTime();
	}

}
