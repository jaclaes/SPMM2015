package org.cheetahplatform.tdm.daily.action;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.editpart.AbstractPlanningAreaEditPart;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;

public abstract class AbstractTimeSlotAction extends Action {
	protected final AbstractPlanningAreaEditPart planningAreaEditPart;
	private final Point location;

	public AbstractTimeSlotAction(AbstractPlanningAreaEditPart planningAreaEditPart, Point location) {
		this.planningAreaEditPart = planningAreaEditPart;
		this.location = location;
	}

	/**
	 * Carry out the work for the given timeslot.
	 * 
	 * @param timeSelection
	 *            the slot
	 */
	protected abstract void doRun(TimeSlot timeSelection);

	protected Workspace getWorkspace() {
		return planningAreaEditPart.getWorkspace().getModel();
	}

	@Override
	public void run() {
		TimeSlot timeSelection = planningAreaEditPart.getSelection();
		if (timeSelection == null) {
			DateTime startTime = planningAreaEditPart.getWorkspace().getConverter().computeDateTime(location);
			DateTime endTime = new DateTime(startTime, false);
			endTime.plus(new Duration(0, 0, TDMConstants.LOCKING_INTERVAL * 4));
			timeSelection = new TimeSlot(startTime, endTime);
		}

		doRun(timeSelection);
	}
}
