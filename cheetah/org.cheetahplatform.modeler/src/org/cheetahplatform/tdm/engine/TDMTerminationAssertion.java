package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;

public class TDMTerminationAssertion {
	private TerminationAssertion assertion;

	public TDMTerminationAssertion(TerminationAssertion assertion) {
		this.assertion = assertion;
	}

	public boolean canTerminate() {
		return assertion.canTerminate();
	}

	public TerminationAssertion getActivityUiModel() {
		return assertion;
	}

	public DateTime getEndTime() {
		return assertion.getEndTime();
	}

	public DateTime getStartTime() {
		return assertion.getStartTime();
	}

	public boolean toBeCheckedAt(DateTime dateTime) {
		return assertion.getTimeSlot().includes(dateTime);
	}
}
