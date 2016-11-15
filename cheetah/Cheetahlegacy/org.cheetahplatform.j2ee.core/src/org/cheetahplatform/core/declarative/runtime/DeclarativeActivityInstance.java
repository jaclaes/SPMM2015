/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.declarative.runtime;

import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.ACTIVATED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.LAUNCHED;
import static org.cheetahplatform.core.imperative.runtime.state.INodeInstanceState.SKIPPED;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.modeling.IActivityInstance;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;

public class DeclarativeActivityInstance extends AbstractDeclarativeNodeInstance implements IActivityInstance {
	private static final long serialVersionUID = 7322029746284580179L;
	private TimeSlot slot;

	public DeclarativeActivityInstance(DeclarativeProcessInstance instance, DeclarativeActivity activity) {
		super(instance, activity);

		this.slot = new TimeSlot();
	}

	public void cancel() {
		Assert.isTrue(state.equals(LAUNCHED));
		// do not allow declarative activities to be switched back to activated, as we would have to check the constraints again
		testAndSet(ACTIVATED);
		testAndSet(SKIPPED);
		log(EventType.CANCEL);
	}

	@Override
	public void complete() {
		super.complete();
		setEndTime(DateTimeProvider.getDateTimeSource().getCurrentTime(false));
	}

	public DateTime getEndTime() {
		return slot.getEnd();
	}

	/**
	 * Returns the slot.
	 * 
	 * @return the slot
	 */
	public TimeSlot getSlot() {
		return slot;
	}

	public DateTime getStartTime() {
		return slot.getStart();
	}

	@Override
	public void launch() {
		super.launch();
		slot.setEnd(null);
		slot.setStart(DateTimeProvider.getDateTimeSource().getCurrentTime(true));
	}

	/**
	 * Sets the endTime.
	 * 
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(DateTime endTime) {
		DateTime start = getStartTime();
		if (endTime != null && start != null && start.compareTo(endTime) > 0) {
			endTime.setTimeInMilliseconds(start.getTimeInMilliseconds() + 1);
		}
		slot.setEnd(endTime);
	}

	/**
	 * Sets the startTime.
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(DateTime startTime) {
		slot.setStart(startTime);
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		slot = timeSlot;
	}

	@Override
	public String toString() {
		return getNode().getName() + ", " + slot.toString();
	}
}
