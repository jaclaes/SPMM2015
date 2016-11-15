package org.cheetahplatform.core.declarative.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

public abstract class MultiActivityConstraint extends AbstractDeclarativeConstraint {

	private static final long serialVersionUID = -436058054769428964L;

	private List<DeclarativeActivity> startActivities;
	private List<DeclarativeActivity> endActivities;

	protected MultiActivityConstraint() {
		this(new ArrayList<DeclarativeActivity>(), new ArrayList<DeclarativeActivity>());
	}

	protected MultiActivityConstraint(DeclarativeActivity startActivity, List<DeclarativeActivity> endActivities) {
		this(Arrays.asList(new DeclarativeActivity[] { startActivity }), endActivities);
	}

	protected MultiActivityConstraint(List<DeclarativeActivity> startActivities, DeclarativeActivity endActivity) {
		this(startActivities, Arrays.asList(new DeclarativeActivity[] { endActivity }));
	}

	protected MultiActivityConstraint(List<DeclarativeActivity> startActivities, List<DeclarativeActivity> endActivities) {
		this.startActivities = new ArrayList<DeclarativeActivity>();
		this.endActivities = new ArrayList<DeclarativeActivity>();

		setStartActivities(startActivities);
		setEndActivities(endActivities);
	}

	@Override
	protected void addActivities(List<DeclarativeActivity> activities) {
		activities.addAll(startActivities);
		activities.addAll(endActivities);
	}

	@Override
	protected void addEndActivities(List<DeclarativeActivity> activities) {
		activities.addAll(endActivities);
	}

	public void addEndActivity(DeclarativeActivity activity) {
		endActivities.add(activity);
	}

	@Override
	protected void addStartActivities(List<DeclarativeActivity> activities) {
		activities.addAll(startActivities);
	}

	public void addStartActivity(DeclarativeActivity activity) {
		startActivities.add(activity);
	}

	public void clearEndActivities() {
		endActivities.clear();
	}

	public void clearStartActivities() {
		startActivities.clear();
	}

	public void setEndActivities(List<DeclarativeActivity> activities) {
		endActivities.clear();
		endActivities.addAll(activities);
	}

	public void setStartActivities(List<DeclarativeActivity> activities) {
		startActivities.clear();
		startActivities.addAll(activities);
	}

}
