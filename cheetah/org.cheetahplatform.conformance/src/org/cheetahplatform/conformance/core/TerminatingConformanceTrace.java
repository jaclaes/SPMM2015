package org.cheetahplatform.conformance.core;

import java.util.List;

public class TerminatingConformanceTrace {
	private List<ConformanceActivity> activities;
	private boolean canTerminate;

	public TerminatingConformanceTrace(List<ConformanceActivity> activities, boolean canTerminate) {
		this.activities = activities;
		this.canTerminate = canTerminate;
	}

	/**
	 * @return the canTerminate
	 */
	public boolean canTerminate() {
		return canTerminate;
	}

	/**
	 * @return the activities
	 */
	public List<ConformanceActivity> getActivities() {
		return activities;
	}

}
