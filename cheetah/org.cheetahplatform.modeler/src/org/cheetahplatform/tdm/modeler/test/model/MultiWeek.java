/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.weekly.editpart.MultiWeekEditPart;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyEditPart;
import org.eclipse.gef.EditPart;

public class MultiWeek extends GenericModel {
	private final WeeklyPlanningArea area;
	private final WeeklyHeader header;
	private TimeSlot slot;

	/**
	 * Cached as needed for layouting.
	 */
	private int width;
	private int height;

	public MultiWeek(IGenericModel parent, DateTime start) {
		super(parent);

		DateTime end = new DateTime(start, false);
		end.plus(new Duration(WeeklyEditPart.getWeeksPerRow() * 7, 0, 0));

		this.slot = new TimeSlot(start, end);
		this.area = new WeeklyPlanningArea(this);
		this.header = new WeeklyHeader(this);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new MultiWeekEditPart(this);
	}

	public WeeklyPlanningArea getArea() {
		return area;
	}

	@Override
	public List<? extends Object> getChildren() {
		List<Object> list = new ArrayList<Object>();
		list.add(header);
		list.add(area);

		return list;
	}

	/**
	 * Return the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	public TimeSlot getSlot() {
		return slot;
	}

	public DateTime getStartTime() {
		return slot.getStart();
	}

	/**
	 * Return the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the height.
	 * 
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Set the width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
