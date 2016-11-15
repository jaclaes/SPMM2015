package org.cheetahplatform.shared;

public class Recommendation {
	private ActivityInstanceHandle activity;
	private double doValue;
	private double dontValue;

	public Recommendation(ActivityInstanceHandle activity, double doValue, double dontValue) {
		this.activity = activity;
		this.doValue = doValue;
		this.dontValue = dontValue;
	}

	public ActivityInstanceHandle getActivity() {
		return activity;
	}

	public double getDontValue() {
		return dontValue;
	}

	public double getDoValue() {
		return doValue;
	}
}
