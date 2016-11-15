package org.cheetahplatform.tdm.engine;

import java.util.ArrayList;
import java.util.List;

public class TDMProcessInstance {

	private List<TDMActivity> activities;

	public TDMProcessInstance() {
		this.activities = new ArrayList<TDMActivity>();
	}

	public void addActivity(TDMActivity activity) {
		activities.add(activity);
	}

	public List<TDMActivity> getActivities() {
		return activities;
	}

	public void reset() {
		activities.clear();
	}

}
