package org.cheetahplatform.core.declarative.constraint;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

public abstract class AbstractDeclarativeConstraintWithTwoActivities extends AbstractDeclarativeConstraint {

	private static final long serialVersionUID = -6372821059932345559L;

	protected DeclarativeActivity activity1;
	protected DeclarativeActivity activity2;

	protected AbstractDeclarativeConstraintWithTwoActivities() {
		super();
	}

	protected AbstractDeclarativeConstraintWithTwoActivities(DeclarativeActivity activity1, DeclarativeActivity activity2) {
		Assert.isNotNull(activity1);
		Assert.isNotNull(activity2);

		this.activity1 = activity1;
		this.activity2 = activity2;
	}

	@Override
	protected void addActivities(List<DeclarativeActivity> activities) {
		activities.add(activity1);
		activities.add(activity2);
	}

	@Override
	protected void addEndActivities(List<DeclarativeActivity> activities) {
		activities.add(activity2);
	}

	@Override
	protected void addStartActivities(List<DeclarativeActivity> activities) {
		activities.add(activity1);
	}

	/**
	 * @param activity1
	 *            the activity1 to set
	 */
	public void setActivity1(DeclarativeActivity activity1) {
		this.activity1 = activity1;
	}

	/**
	 * @param activity2
	 *            the activity2 to set
	 */
	public void setActivity2(DeclarativeActivity activity2) {
		this.activity2 = activity2;
	}

}
