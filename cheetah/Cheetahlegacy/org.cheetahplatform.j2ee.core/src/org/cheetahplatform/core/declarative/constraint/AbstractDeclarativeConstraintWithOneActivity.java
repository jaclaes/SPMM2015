package org.cheetahplatform.core.declarative.constraint;

import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

/**
 * Abstract implementation for all constraints involving a single activity.
 * 
 * @author user
 * 
 */
public abstract class AbstractDeclarativeConstraintWithOneActivity extends AbstractDeclarativeConstraint {
	private static final long serialVersionUID = 7201211962901784075L;

	protected DeclarativeActivity activity;

	public AbstractDeclarativeConstraintWithOneActivity(DeclarativeActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void addActivities(List<DeclarativeActivity> activities) {
		activities.add(activity);
	}

	@Override
	protected void addEndActivities(List<DeclarativeActivity> activities) {
		activities.add(activity);
	}

	@Override
	protected void addStartActivities(List<DeclarativeActivity> activities) {
		activities.add(activity);
	}

	/**
	 * Return the activity.
	 * 
	 * @return the activity
	 */
	public DeclarativeActivity getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(DeclarativeActivity activity) {
		this.activity = activity;
	}

}
