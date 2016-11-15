package org.cheetahplatform.tdm.daily.model;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.daily.editpart.TerminationAssertionEditPart;
import org.eclipse.gef.EditPart;

public class TerminationAssertion extends Assertion {

	private static final DeclarativeActivity DUMMY_ACTIVITY = new DeclarativeActivity("");

	public TerminationAssertion(Day day, TimeSlot slot) {
		this(day, slot, IIdentifiableObject.NO_ID);
	}

	public TerminationAssertion(Day day, TimeSlot slot, long cheetahId) {
		super(day, slot, DUMMY_ACTIVITY, cheetahId);
	}

	public boolean canTerminate() {
		return isPositive();
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new TerminationAssertionEditPart(this);
	}

	@Override
	public void delete() {
		getWorkspace().removeAssertion(this);
	}

	@Override
	protected String getChangeTimeSlotCommand() {
		return TDMCommand.COMMAND_CHANGE_TERMINATION_ASSERTION_TIME_SLOT;
	}

	@Override
	protected String getSwitchExpectationStateCommand() {
		return TDMCommand.COMMAND_SWITCH_TERMINATION_ASSERTION_EXPECTATION_STATE;
	}

	public void setCanTerminate(boolean canTerminate) {
		setPositive(canTerminate);
	}

}
