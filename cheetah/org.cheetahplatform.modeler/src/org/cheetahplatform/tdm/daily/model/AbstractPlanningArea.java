package org.cheetahplatform.tdm.daily.model;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.tdm.GenericTDMModel;

public abstract class AbstractPlanningArea extends GenericTDMModel implements IActivityContainer {
	private int width;

	protected AbstractPlanningArea(Day parent, int width) {
		super(parent);

		this.width = width;
	}

	@Override
	public int getAvailableWidth() {
		return getWidth() - 10;
	}

	@Override
	public Date getDate() {
		return getParent().getDate();
	}

	@Override
	public Day getParent() {
		return (Day) super.getParent();
	}

	/**
	 * Return the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
}
