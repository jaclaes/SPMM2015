/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.common.date.AbstractDate;
import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.editpart.ActivityEditPart;
import org.cheetahplatform.tdm.daily.policy.ActivityChangeListener;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

public class Activity extends AbstractActivity {

	private final Map<AbstractDate, Rectangle> dayToRelativeBounds;
	private ActivityChangeListener listener;

	public Activity(Day day, DeclarativeActivityInstance activity) {
		this(day, activity, IIdentifiableObject.NO_ID);
	}

	public Activity(Day day, DeclarativeActivityInstance activity, long cheetahId) {
		super(day, activity, cheetahId);

		this.dayToRelativeBounds = new HashMap<AbstractDate, Rectangle>();
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ActivityEditPart(this);
	}

	public void delete() {
		getWorkspace().removeActivity(this);

		if (listener != null) {
			listener.detach();
		}
	}

	public DeclarativeActivity getActivityDefinition() {
		return (DeclarativeActivity) activity.getNode();
	}

	public int getHeight(Day day) {
		return dayToRelativeBounds.get(day.getDate()).height;
	}

	public String getName() {
		return activity.getNode().getName();
	}

	public Rectangle getRelativeBounds(Day day) {
		return dayToRelativeBounds.get(day.getDate());
	}

	public int getWidth(Day day) {
		return dayToRelativeBounds.get(day.getDate()).width;
	}

	public int getX(Day day) {
		return dayToRelativeBounds.get(day.getDate()).x;
	}

	public int getY(Day day) {
		return dayToRelativeBounds.get(day.getDate()).y;
	}

	/**
	 * Same as {@link #isParallelTo(Activity, TimeSlot)}, but without restricting the timeslot.
	 */
	public boolean isParallelTo(Activity instance) {
		return getTimeSlot().isParallelTo(instance.getTimeSlot());
	}

	/**
	 * Determine whether the given activity is parallel to this activity within the given time slot.
	 * 
	 * @param toCompare
	 *            the activity to be checked
	 * @param toCheck
	 *            the time slot to be checked
	 * @return <code>true</code> if the activities are parallel within the slot, <code>false</code> otherwise
	 */
	public boolean isParallelTo(Activity toCompare, TimeSlot toCheck) {
		return getTimeSlot().isParallelTo(toCompare.getTimeSlot()) && toCheck.isParallelTo(getTimeSlot())
				&& toCheck.isParallelTo(toCompare.getTimeSlot());
	}

	public boolean occursAt(Day tddDay) {
		DateTime start = activity.getStartTime();
		DateTime end = activity.getEndTime();

		return tddDay.getDate().betweenDays(start.toDate(), end.toDate());
	}

	public void select() {
		firePropertyChanged(TDMConstants.PROPERTY_SELECTION, null, true);
	}

	public void setListener(ActivityChangeListener listener) {
		Assert.isNotNull(this.listener);
		this.listener = listener;
	}

	public void setRelativeBounds(Date parent, Rectangle bounds) {
		dayToRelativeBounds.put(parent, bounds);
		firePropertyChanged(TDMConstants.PROPERTY_LAYOUT);
	}

	@Override
	public String toString() {
		return activity.toString();
	}

	public void unselect() {
		firePropertyChanged(TDMConstants.PROPERTY_SELECTION, null, false);
	}

}
