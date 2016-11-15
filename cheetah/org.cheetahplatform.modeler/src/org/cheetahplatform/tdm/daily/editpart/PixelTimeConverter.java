/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.editpart;

import static org.cheetahplatform.tdm.daily.editpart.ActivityEditPart.SPACING_X;
import static org.cheetahplatform.tdm.daily.editpart.DayTimeLineEditPart.WIDTH;
import static org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart.DAY_OFFSET_X;
import static org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart.DAY_SPACING;
import static org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure.HOUR_HEIGHT;
import static org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure.START_HOUR;

import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.daily.figure.DayHeaderFigure;
import org.cheetahplatform.tdm.daily.figure.DayTimeLineFigure;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.Day;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class is responsible for translating between pixel information and dates.
 * 
 * @author Stefan Zugal
 * 
 */
public class PixelTimeConverter {

	/**
	 * Convert the given y delta into minutes.
	 * 
	 * @param yDelta
	 *            y delta
	 * @return amount of minutes represented by the delta
	 */
	public static int computeDuration(int yDelta) {
		return (int) (yDelta * (60.0 / DayTimeLineFigure.HOUR_HEIGHT));
	}

	/**
	 * Compute the height of a timeslot. The height also depends on the day, as time slots may span several days.
	 * 
	 * @param day
	 *            the day for which the height should be computed
	 * @param slot
	 *            the slot
	 * @return the height of the slot for the given day
	 */
	public static int computeHeight(Date day, TimeSlot slot) {
		TimeSlot timeSlot = trimToDay(day, slot);
		if (timeSlot == null) {
			return 0; // slot has no duration
		}

		float duration = timeSlot.getDurationInMinutes();
		return (int) ((duration / 60) * HOUR_HEIGHT);
	}

	/**
	 * Compute the height the activity has on the given day - cf. also {@link #computeHeight(Date, TimeSlot)}.
	 * 
	 * @param day
	 *            the day
	 * @param activity
	 *            the activity
	 * @return the height of the activity on the given day
	 */
	public static int computeHeight(Day day, Activity activity) {
		return computeHeight(day.getDate(), activity.getTimeSlot());
	}

	/**
	 * Compute the <b>relative</b> y position of the given time slot.
	 * 
	 * @param day
	 *            the day
	 * @param slot
	 *            the slot
	 * @return the relative y position
	 */
	public static int computeYRelative(Date day, TimeSlot slot) {
		slot = trimToDay(day, slot);
		if (slot == null) {
			return 0;
		}

		float startMinute = (slot.getStart().getHour() - START_HOUR) * 60 + slot.getStart().getMinute();
		return (int) ((startMinute / 60) * HOUR_HEIGHT);
	}

	/**
	 * Convenience function for {@link #computeYRelative(Date, TimeSlot)}.
	 * 
	 * @param day
	 *            the day
	 * @param activity
	 *            the activity
	 * @return the relative y position of the activity for the given day
	 */
	public static int computeYRelative(Day day, Activity activity) {
		return PixelTimeConverter.computeYRelative(day.getDate(), activity.getTimeSlot());
	}

	/**
	 * Compute the intersection of the given day and timeslot.
	 * 
	 * @param day
	 *            the day
	 * @param slot
	 *            the timeslot
	 * @return the intersection of day and timeslot, <code>null</code> if there is none
	 */
	public static TimeSlot trimToDay(Date day, TimeSlot slot) {
		if (!slot.includesDay(day)) {
			return null;
		}

		DateTime start = new DateTime(slot.getStart(), true);
		if (!day.sameDay(start)) {
			start.startOfDay();
		}

		DateTime end = new DateTime(slot.getEnd(), false);
		if (!day.sameDay(end.toInclusive())) {
			end.setDay(day.getDay());
			end.endOfDay();
		}

		start.setDay(day.getDay());

		if (start.compareTo(end) == 0) {
			return null;
		}

		return new TimeSlot(start, end);
	}

	private final WorkspaceEditPart workspace;

	public PixelTimeConverter(WorkspaceEditPart workspace) {
		this.workspace = workspace;
	}

	public DateTime computeDateTime(Point absoluteLocation) {
		return computeDateTime(absoluteLocation, true);
	}

	/**
	 * Compute the time at a certain <b>absolute</b> location.
	 * 
	 * @param absoluteLocation
	 *            the location
	 * @return the time for the given location
	 */
	public DateTime computeDateTime(Point absoluteLocation, boolean lockInterval) {
		int x = absoluteLocation.x + workspace.getScrolledX();
		int dayOffset = computeDayOffset().y;
		int y = absoluteLocation.y + workspace.getScrolledY() - dayOffset;
		if (lockInterval) {
			y = DayTimeLineFigure.lockToQuarterHour(y);
		}

		int minute = (int) (y * (60.0 / DayTimeLineFigure.HOUR_HEIGHT));
		int index = Math.min(workspace.getChildren().size() - 1, getDayIndeX(x));
		index = Math.max(0, index);

		DayEditPart editpart = (DayEditPart) workspace.getChildren().get(index);
		Date date = ((Day) editpart.getModel()).getDate();
		if (y < 0) {
			return new DateTime(date, true);
		}

		DateTime dateTime = new DateTime(date, true);
		dateTime.setHour(0);
		dateTime.setMinute(0);
		dateTime.increaseMinutesBy(minute);
		return dateTime;
	}

	/**
	 * Determine the offset of the {@link DayEditPart} child.
	 * 
	 * @return the offset
	 */
	private Point computeDayOffset() {
		Point offset = new Point();
		IFigure day = (IFigure) workspace.getContent().getChildren().get(0);
		Rectangle dayBounds = day.getBounds().getCopy();
		// add the height of the header
		dayBounds.y += ((IFigure) day.getChildren().get(0)).getBounds().height;

		if (workspace.getScrolledY() == 0) {
			offset.y = dayBounds.y;
		} else {
			offset.y = WorkspaceEditPart.DAY_OFFSET_Y + DayHeaderFigure.HEIGHT;
		}

		if (workspace.getScrolledX() == 0) {
			offset.x = dayBounds.x;
		} else {
			offset.x = WorkspaceEditPart.DAY_OFFSET_X;
		}

		return offset;
	}

	/**
	 * Compute the <b>absolute</b> x position for the given date. This may be necessary for displaying feedback figures which rely on
	 * absolute positions.
	 * 
	 * @param date
	 *            the date
	 * @return the absolute x position of the date
	 */
	@SuppressWarnings("unchecked")
	public int computeXAbsolute(Date date) {
		List<Day> children = workspace.getModelChildren();
		int dayIndex = -1;

		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getDate().sameDay(date)) {
				dayIndex = i;
				break;
			}
		}

		// no day created yet for the date
		if (dayIndex == -1) {
			return Integer.MAX_VALUE;
		}

		return computeDayOffset().x + SPACING_X * 2 + WIDTH + (DayEditPart.WIDTH + DAY_SPACING) * dayIndex;
	}

	/**
	 * Compute the <b>absolute</b> y position for the given timeslot on the given day.
	 * 
	 * @param date
	 *            the day
	 * @param slot
	 *            the slot
	 * @return the absolute y positions
	 */
	public int computeYAbsolute(Date date, TimeSlot slot) {
		int y = computeYRelative(date, slot);
		y -= workspace.getScrolledY() - workspace.getConverter().computeDayOffset().y;
		// y += WorkspaceEditPart.DAY_OFFSET_Y;
		return y;
	}

	public int computeYIncludingScrolling(Day day, Activity activity) {
		int absoluteY = PixelTimeConverter.computeYRelative(day, activity);
		absoluteY -= workspace.getScrolledY();
		return absoluteY;
	}

	private int getDayIndeX(int x) {
		return (x - DAY_OFFSET_X) / (DayEditPart.WIDTH + DAY_SPACING);
	}
}
