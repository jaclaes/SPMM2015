package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.tdm.daily.model.Activity;

public class TDMActivity {

	private final Activity activity;
	private DeclarativeActivityInstance activityInstance;

	public TDMActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return the activity
	 */
	public DeclarativeActivity getActivity() {
		return (DeclarativeActivity) activity.getActivity().getNode();
	}

	/**
	 * @return the activityInstance
	 */
	public DeclarativeActivityInstance getActivityInstance() {
		return activityInstance;
	}

	public Activity getActivityUiModel() {
		return activity;
	}

	public DateTime getEndTime() {
		return activity.getEndTime();
	}

	public DateTime getStartTime() {
		return activity.getStartTime();
	}

	/**
	 * @param activityInstance
	 *            the activityInstance to set
	 */
	public void setActivityInstance(DeclarativeActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}

}
