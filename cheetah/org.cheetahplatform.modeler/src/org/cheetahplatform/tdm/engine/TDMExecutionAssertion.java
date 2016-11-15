package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;

public class TDMExecutionAssertion {

	private final ExecutionAssertion assertion;

	public TDMExecutionAssertion(ExecutionAssertion assertion) {
		this.assertion = assertion;
	}

	public DeclarativeActivity getActivity() {
		return assertion.getActivityDefinition();
	}

	public ExecutionAssertion getActivityUiModel() {
		return assertion;
	}

	public DateTime getEndTime() {
		return assertion.getEndTime();
	}

	public DateTime getStartTime() {
		return assertion.getStartTime();
	}

	public boolean isExecutable() {
		return assertion.isExecutable();
	}

	public boolean toBeCheckedAt(DateTime dateTime) {
		return assertion.getTimeSlot().includes(dateTime);
	}

}
