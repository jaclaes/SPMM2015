package org.cheetahplatform.tdm.daily.model;

import org.cheetahplatform.common.date.TimeSlot;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.daily.editpart.ExecutionAssertionEditPart;
import org.eclipse.gef.EditPart;

public class ExecutionAssertion extends Assertion {

	public ExecutionAssertion(Day day, TimeSlot slot, DeclarativeActivity activity) {
		this(day, slot, activity, IIdentifiableObject.NO_ID);
	}

	public ExecutionAssertion(Day day, TimeSlot slot, DeclarativeActivity activity, long cheetahId) {
		super(day, slot, activity, cheetahId);

		Services.getModificationService().addListener(this, activity);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new ExecutionAssertionEditPart(this);
	}

	@Override
	public void delete() {
		getWorkspace().removeAssertion(this);
		Services.getModificationService().removeListener(this, getActivityDefinition());
	}

	@Override
	protected String getChangeTimeSlotCommand() {
		return TDMCommand.COMMAND_CHANGE_EXECUTION_ASSERTION_TIME_SLOT;
	}

	@Override
	protected String getSwitchExpectationStateCommand() {
		return TDMCommand.COMMAND_SWITCH_EXECUTION_ASSERTION_EXPECTATION_STATE;
	}

	public boolean isExecutable() {
		return positive;
	}

	/**
	 * @param executable
	 *            the executable to set
	 */
	public void setExecutable(boolean executable) {
		setPositive(executable);
	}

}
