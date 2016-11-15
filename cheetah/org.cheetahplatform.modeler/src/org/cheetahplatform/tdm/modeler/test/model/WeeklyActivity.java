/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState;
import org.cheetahplatform.tdm.MessageLookup;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.model.AbstractActivity;
import org.cheetahplatform.tdm.weekly.RelativeBounds;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyActivityEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

public class WeeklyActivity extends AbstractActivity {
	public static RelativeBounds defaultBounds() {
		return new RelativeBounds(0, 0, WeeklyActivityEditPart.WIDTH, WeeklyActivityEditPart.HEIGHT);
	}

	private RelativeBounds activityBounds;

	/**
	 * <code>true</code> if the user has changed the position of the object, <code>false</code> if not.
	 */
	private boolean customLayout;

	public WeeklyActivity(MultiWeek parent, DeclarativeActivityInstance instance) {
		super(parent, instance);
		Assert.isNotNull(parent);
		Assert.isNotNull(instance);

		setBounds(defaultBounds());
	}

	public void adaptMilestone() {
		getWorkspace().getWeekly().adaptMilestone(this);
	}

	/**
	 * Clear the activity's location information. Ensure that you add the corresponding bounds afterwards.
	 */
	public void clearBounds() {
		customLayout = false;
		activityBounds = null;
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WeeklyActivityEditPart(this);
	}

	public RelativeBounds getActivityBounds() {
		return activityBounds;
	}

	public Rectangle getBounds() {
		int weekWidth = ((MultiWeek) getParent()).getWidth();
		int weekHeight = ((MultiWeek) getParent()).getHeight();

		return activityBounds.toAbsolute(weekWidth, weekHeight);
	}

	public String getLabel() {
		return activity.getNode().getName();
	}

	public String getMessage() {
		return MessageLookup.getInstance().getMessage(activity);
	}

	@Override
	public List<? extends Object> getSourceConnections() {
		return getWorkspace().getOutgoingConstraints(this);
	}

	@Override
	public List<? extends Object> getTargetConnections() {
		return getWorkspace().getIncomingConstraints(this);
	}

	public String getToolTip() {
		String label = getLabel();
		Role role = RoleLookup.getInstance().getRole(activity);

		if (role != null) {
			label += "\n\nMitarbeiter: " + role.getName();
		}

		return label;

	}

	public boolean hasCustomLayout() {
		return customLayout;
	}

	public boolean isLaunched() {
		return INodeInstanceState.LAUNCHED.equals(activity.getState());
	}

	public boolean isVisible() {
		Role role = RoleLookup.getInstance().getRole(activity);
		if (!getWorkspace().isShowRole(role)) {
			return false;
		}

		if (getWorkspace().isShowCompletedActivities()) {
			return true;
		}

		return !activity.isInFinalState();
	}

	/**
	 * Logs that the activity has been moved to another week, if necessary (if the activity has been moved).
	 * 
	 * @param oldStart
	 *            the old start time of the activity
	 * @param newStart
	 *            the new start time of the activity
	 */
	public void logMovedToOtherWeekIfNecessary(DateTime oldStart, DateTime newStart) {
		boolean movedToOtherWeek = !newStart.sameWeek(oldStart);

		if (movedToOtherWeek) {
			Duration moveDeltaInWeeks = null;
			int moveDirection = 0;

			if (oldStart.compareTo(newStart) < 0) {
				moveDeltaInWeeks = new Duration(oldStart, newStart);
				moveDirection = 1;
			} else {
				moveDeltaInWeeks = new Duration(newStart, oldStart);
				moveDirection = -1;
			}

			AuditTrailEntry entry = new AuditTrailEntry(TDMConstants.LOG_EVENT_ACTIVITY_MOVE, String.valueOf(activity.getCheetahId()));
			entry.setAttribute(TDMConstants.ATTRIBUTE_ACTIVITY_MOVE_OFFSET_IN_WEEKS, moveDirection * moveDeltaInWeeks.getDurationInWeeks());
			getWorkspace().logInProcessInstance(entry);
		}
	}

	public void move(MultiWeek targetWeek, Point delta, int weekOffset) {
		int width = ((MultiWeek) getParent()).getWidth();
		int height = ((MultiWeek) getParent()).getHeight();
		activityBounds.translate(delta, width, height);

		DateTime newStart = new DateTime(targetWeek.getStartTime());
		newStart.plus(new Duration(weekOffset * 7, 0, 0, true));

		DateTime oldStart = new DateTime(activity.getStartTime());
		activity.setEndTime(null);
		activity.setStartTime(newStart);

		firePropertyChanged(TDMConstants.PROPERTY_LAYOUT);
		firePropertyChanged(TDMConstants.PROPERTY_MOVE_TO_TOP);

		logMovedToOtherWeekIfNecessary(oldStart, newStart);
	}

	public void setBounds(Rectangle bounds) {
		MultiWeek week = (MultiWeek) getParent();
		int width = week.getWidth();
		int height = week.getHeight();
		if (width == 0 || height == 0) {
			return;
		}

		activityBounds = new RelativeBounds(bounds.x, width, bounds.y, height, bounds.width, bounds.height);
	}

	public void setBounds(RelativeBounds bounds) {
		activityBounds = bounds;

		firePropertyChanged(TDMConstants.PROPERTY_LAYOUT);
	}

	/**
	 * Set the customLayout.
	 * 
	 * @param customLayout
	 *            the customLayout to set
	 */
	public void setCustomLayout(boolean customLayout) {
		this.customLayout = customLayout;
	}

	public void setEndTime(DateTime time) {
		activity.setEndTime(time);
	}

	public void setLocation(MultiWeek targetModel, Point newLocation) {
		int width = targetModel.getWidth();
		int height = targetModel.getHeight();
		RelativeBounds bounds = defaultBounds();
		bounds.setRelativeX(newLocation.x, width);
		bounds.setRelativeY(newLocation.y, height);
		activityBounds = bounds;
	}

	public void setMessage(String message) {
		MessageLookup.getInstance().assignMessage(activity, message);

		firePropertyChanged(TDMConstants.PROPERTY_MESSAGE);
	}

	@Override
	public String toString() {
		return activity.toString();
	}

}
